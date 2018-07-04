package com.orion.regression.report;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.orion.regression.bean.ObjectTuple;
import com.orion.regression.bean.OutcomeDetail;
import com.orion.regression.bean.Redundancy;
import com.orion.regression.bean.RelationTuple;
import com.orion.regression.bean.Tuple;

public class RelationReport extends Report {
	private final static String RELATION_OUTPUT_PATH = "output.relation.xls.path";

	static final int INDEX1_COLUMN = 1;
	static final int INDEX2_COLUMN = 2;
	static final int UNIQUE_NAME1_COLUMN = 3;
	static final int UNIQUE_NAME2_COLUMN = 4;
	static final int TYPE_ID1_COLUMN = 5;
	static final int TYPE_ID2_COLUMN = 6;
	static final int SRC_ID1_COLUMN = 7;
	static final int SRC_ID2_COLUMN = 8;
	static final int TGT_ID1_COLUMN = 9;
	static final int TGT_ID2_COLUMN = 10;
	static final int DESC1_COLUMN = 11;
	static final int DESC2_COLUMN = 12;
	static final int VALUE_ID1_COLUMN = 13;
	static final int VALUE_ID2_COLUMN = 14;
	static final int WEIGHT1_COLUMN = 15;
	static final int WEIGHT2_COLUMN = 16;
	static final int TXT1_COLUMN = 17;
	static final int TXT2_COLUMN = 18;
	static final int SOURCE1_COLUMN = 19;
	static final int SOURCE2_COLUMN = 20;
	static final int TARGET1_COLUMN = 21;
	static final int TARGET2_COLUMN = 22;
	static final int ERROR1_COLUMN = 23;

	public RelationReport(CommandLine cmd) {
		if (cmd.hasOption("rpt")) {
			this.output_path = cmd.getOptionValue("rpt") + cmd.getOptionValue("pfm") + "_TestResults_Relations_"
					+ CURR_DATE_STR + ".xls";
		} else {
			this.output_path = "./" + cmd.getOptionValue("pfm") + "_TestResults_Relations_" + CURR_DATE_STR + ".xls";
		}
	}

	public RelationReport(Properties prop) {
		this.output_path = prop.getProperty(RELATION_OUTPUT_PATH) + "TestResults_Relations_" + CURR_DATE_STR + ".xls";
	}

	protected void logAddSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		RelationTuple relationTuple2 = (RelationTuple) detail.getTuple2();
		logText(sheet, rownum, 2, "NULL/" + relationTuple2.getSrc_id());
		logText(sheet, rownum, 3, "NULL/" + relationTuple2.getTgt_id());

	}

	protected void logDelSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		RelationTuple relationTuple1 = (RelationTuple) detail.getTuple1();
		logText(sheet, rownum, 2, relationTuple1.getSrc_id() + "/NULL");
		logText(sheet, rownum, 3, relationTuple1.getTgt_id() + "/NULL");
	}

	protected void logModSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		RelationTuple relationTuple1 = (RelationTuple) detail.getTuple1();
		RelationTuple relationTuple2 = (RelationTuple) detail.getTuple2();
		logText(sheet, rownum, 2, relationTuple1.getSrc_id() + "/" + relationTuple2.getSrc_id());
		logText(sheet, rownum, 3, relationTuple1.getTgt_id() + "/" + relationTuple2.getTgt_id());
	}		
	
	public void logTupleCompare(HSSFSheet sheet, int rownum, Tuple tuple1, Tuple tuple2) {		
		if (tuple1 != null) {
			RelationTuple relationTuple1 = (RelationTuple)tuple1;
			logText(sheet, rownum, INDEX1_COLUMN, new Integer(relationTuple1.getIndex()).toString());
			logText(sheet, rownum, UNIQUE_NAME1_COLUMN, relationTuple1.getUniqueName());
			logText(sheet, rownum, TYPE_ID1_COLUMN, relationTuple1.getType_id());
			logText(sheet, rownum, SRC_ID1_COLUMN, relationTuple1.getSrc_id());
			logText(sheet, rownum, TGT_ID1_COLUMN, relationTuple1.getTgt_id());
			logText(sheet, rownum, DESC1_COLUMN, relationTuple1.getDesc());
			logText(sheet, rownum, VALUE_ID1_COLUMN, relationTuple1.getValue_id());
			logText(sheet, rownum, WEIGHT1_COLUMN, relationTuple1.getWeight());
			logText(sheet, rownum, TXT1_COLUMN, relationTuple1.getTxt());
			logText(sheet, rownum, SOURCE1_COLUMN, relationTuple1.getSource());
			logText(sheet, rownum, TARGET1_COLUMN, relationTuple1.getTarget());
			logText(sheet, rownum, ERROR1_COLUMN, relationTuple1.getError());
		}
		if (tuple2 != null) {
			RelationTuple relationTuple2 = (RelationTuple)tuple2;
			logText(sheet, rownum, INDEX2_COLUMN, new Integer(relationTuple2.getIndex()).toString());
			logText(sheet, rownum, UNIQUE_NAME2_COLUMN, relationTuple2.getUniqueName());
			logText(sheet, rownum, TYPE_ID2_COLUMN, relationTuple2.getType_id());
			logText(sheet, rownum, SRC_ID2_COLUMN, relationTuple2.getSrc_id());
			logText(sheet, rownum, TGT_ID2_COLUMN, relationTuple2.getTgt_id());
			logText(sheet, rownum, DESC2_COLUMN, relationTuple2.getDesc());
			logText(sheet, rownum, VALUE_ID2_COLUMN, relationTuple2.getValue_id());
			logText(sheet, rownum, WEIGHT2_COLUMN, relationTuple2.getWeight());
			logText(sheet, rownum, TXT2_COLUMN, relationTuple2.getTxt());
			logText(sheet, rownum, SOURCE2_COLUMN, relationTuple2.getSource());
			logText(sheet, rownum, TARGET2_COLUMN, relationTuple2.getTarget());
		}
	}
	

	protected String[] getSummaryHeading() {
		return new String[] { "UNIQUE NAME", "COMMENTS", "GOLD vs NEW SRC ID", "GOLD vs NEW TGT ID" };
	}	

	protected String[] getResultHeading() {
		return new String[] { "OBSERVATION", "LINENUM1", "LINENUM2", "UNIQUENAME1", "UNIQUENAME2", "TYPE_ID1",
				"TYPE_ID2", "SRC_ID1", "SRC_ID2", "TGT_ID1", "TGT_ID2", "DESC1", "DESC2", "VALUE1", "VALUE2", "WEIGHT1",
				"WEIGHT2", "TXT1", "TXT2", "SOURCE1", "SOURCE2", "TARGET1", "TARGET2", "ERROR DETAILS" };
	}
}
