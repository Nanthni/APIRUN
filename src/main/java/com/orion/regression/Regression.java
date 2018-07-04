package com.orion.regression;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.cli.CommandLine;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.orion.regression.bean.FileTransferBean;
import com.orion.regression.bean.Outcome;
import com.orion.regression.bean.OutcomeDetail;
import com.orion.regression.bean.ParseOutcome;
import com.orion.regression.bean.Tuple;
import com.orion.regression.http.HTTPExecutor;
import com.orion.regression.parser.JSONParser;
import com.orion.regression.report.ObjectReport;
import com.orion.regression.report.RelationReport;
import com.orion.regression.report.Report;
import com.orion.regression.report.Style;

/**
 * The purpose of this class is to download, parse and compare the "Objects" and
 * "Relations" between the "Stable" and "Next" server
 * 
 * @author Vijay Anand
 *
 */
public class Regression {
	private static final Logger log = Logger.getLogger(Regression.class.getName());
	private final static String OBJ_DOWNLOAD_URL_TEMPLATE = "https://{0}/api/v1/objects?include=parent*&limit=50000&filter[origin_nm]={1}";
	private final static String REL_DOWNLOAD_URL_TEMPLATE = "https://{0}//api/v1/relations?dir=any&filter[relation_type_cd]=11,12,13&include=source.path,related.path&limit=500000&filter[source.origin_nm]={1}&filter[target.origin_nm]={1}";
	private final static String OBSERVATION_ADD = "ADD";
	private final static String OBSERVATION_MOD = "MOD";
	private final static String OBSERVATION_DEL = "DEL";
	private final static String OBSERVATION_PASS = "PASS";
	private final static String OBSERVATION_FAIL = "FAIL";

	/**
	 * Download, parse and compare the "Object" meta data JSON between "Stable" and
	 * "Next" Harvester
	 * 
	 * @param cmd
	 * @param nextBean
	 * @param stableBean
	 * @throws Exception
	 */
	public static void performObjectRegression(CommandLine cmd, FileTransferBean nextBean, FileTransferBean stableBean)
			throws Exception {
		String downloadUrlTemplate = OBJ_DOWNLOAD_URL_TEMPLATE;
		JSONParser jsonParser = new JSONParser("com.orion.regression.bean.ObjectTuple");
		Report report = new ObjectReport(cmd);
		performRegression(downloadUrlTemplate, jsonParser, report, nextBean, stableBean);
	}

	/**
	 * Download, parse and compare the "Relation" meta data JSON between "Stable"
	 * and "Next" Harvester
	 * 
	 * @param cmd
	 * @param nextBean
	 * @param stableBean
	 * @throws Exception
	 */
	public static void performRelationRegression(CommandLine cmd, FileTransferBean nextBean,
			FileTransferBean stableBean) throws Exception {
		String downloadUrlTemplate = REL_DOWNLOAD_URL_TEMPLATE;
		JSONParser jsonParser = new JSONParser("com.orion.regression.bean.RelationTuple");
		Report report = new RelationReport(cmd);
		performRegression(downloadUrlTemplate, jsonParser, report, nextBean, stableBean);
	}

	/**
	 * Download, parse and compare meta data and logs the result in spreadsheet
	 * 
	 * @param downloadUrlTemplate
	 * @param jsonParser
	 * @param report
	 * @param nextBean
	 * @param stableBean
	 * @throws Exception
	 */
	static void performRegression(String downloadUrlTemplate, JSONParser jsonParser, Report report,
			FileTransferBean nextBean, FileTransferBean stableBean) throws Exception {
		setDownloadURL(nextBean, downloadUrlTemplate);
		setDownloadURL(stableBean, downloadUrlTemplate);
		log.log(Level.INFO, "Invoking JSON download from next server -> "+nextBean.getHostName());
		String nextJson = HTTPExecutor.downloadJSON(nextBean);
		log.log(Level.INFO, "Invoking JSON download from stable server -> "+stableBean.getHostName());
		String stableJson = HTTPExecutor.downloadJSON(stableBean);

		log.log(Level.INFO, "Parsing Next JSON ..");
		ParseOutcome nextParseOutcome = jsonParser.parseJsonStr(nextJson);
		log.log(Level.INFO, "Parsing Stable JSON ..");
		ParseOutcome stableParseOutcome = jsonParser.parseJsonStr(stableJson);

		log.log(Level.INFO, "Invoking JSON compare ..");
		Outcome outcome = compare(nextParseOutcome, stableParseOutcome);
		report.logResults(outcome);
	}

	/**
	 * Binds the download URL to the bean
	 * 
	 * @param bean
	 * @param downloadUrlTemplate
	 */
	private static void setDownloadURL(FileTransferBean bean, String downloadUrlTemplate) {
		Object[] curlArgs = { bean.getHostName(), bean.getConnectionName() };
		MessageFormat messageFormat = new MessageFormat(downloadUrlTemplate);
		bean.setDownloadUrl(messageFormat.format(curlArgs));
	}

	/**
	 * Compares the "next" representation with the "stable" representation
	 * 
	 * @param nextParseOutcome
	 * @param stableParseOutcome
	 * @return
	 */
	public static Outcome compare(ParseOutcome nextParseOutcome, ParseOutcome stableParseOutcome) {
		Map<String, Tuple> tupleMap1 = nextParseOutcome.getParseMap();
		Map<String, Tuple> tupleMap2 = stableParseOutcome.getParseMap();
		Outcome outcome = new Outcome();
		outcome.setStableRedundancies(stableParseOutcome.getRedundancies());
		outcome.setNextRedundancies(nextParseOutcome.getRedundancies());
		Set<String> keySetAll = new TreeSet<String>();
		keySetAll.addAll(tupleMap1.keySet());
		keySetAll.addAll(tupleMap2.keySet());
		for (String key : keySetAll) {
			Tuple tuple1 = tupleMap1.get(key);
			Tuple tuple2 = tupleMap2.get(key);
			OutcomeDetail outcomeDetail = new OutcomeDetail(tuple1, tuple2);
			if (tuple1 != null && tuple2 != null) {
				if (tuple1.getDigest_id().equals(tuple2.getDigest_id())) {
					if (tuple1.equals(tuple2)) {
						outcomeDetail.setObservation(OBSERVATION_PASS);
						outcomeDetail.setStyle(Style.PASS);
					} else {
						outcomeDetail.setObservation(OBSERVATION_MOD);
						outcomeDetail.setStyle(Style.WARN);
					}
				} else {
					outcomeDetail.setObservation(OBSERVATION_FAIL);
					outcomeDetail.setStyle(Style.FAIL);
				}
			} else if (tuple1 == null) {
				outcomeDetail.setObservation(OBSERVATION_ADD);
				outcomeDetail.setStyle(Style.FAIL);
			} else if (tuple2 == null) {
				outcomeDetail.setObservation(OBSERVATION_DEL);
				outcomeDetail.setStyle(Style.FAIL);
			}
			outcome.addDetail(outcomeDetail);
		}
		return outcome;
	}
}
