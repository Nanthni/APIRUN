package com.orion.regression.report;

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.cli.CommandLine;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.orion.regression.bean.ObjectTuple;
import com.orion.regression.bean.OutcomeDetail;
import com.orion.regression.bean.Redundancy;
import com.orion.regression.bean.Tuple;

public class ObjectReport extends Report {
	private final static String OBJECT_OUTPUT_PATH = "output.object.xls.path";

	static final int INDEX1_COLUMN = 1;
	static final int INDEX2_COLUMN = 2;
	static final int UNIQUE_NAME1_COLUMN = 3;
	static final int UNIQUE_NAME2_COLUMN = 4;
	static final int ID1_COLUMN = 5;
	static final int ID2_COLUMN = 6;
	static final int PARENT_ID1_COLUMN = 7;
	static final int PARENT_ID2_COLUMN = 8;
	static final int TYPE_ID1_COLUMN = 9;
	static final int TYPE_ID2_COLUMN = 10;
	static final int NAME1_COLUMN = 11;
	static final int NAME2_COLUMN = 12;
	static final int EXT1_COLUMN = 13;
	static final int EXT2_COLUMN = 14;
	static final int DESC1_COLUMN = 15;
	static final int DESC2_COLUMN = 16;
	static final int ORIGIN_DS1_COLUMN = 17;
	static final int ORIGIN_DS2_COLUMN = 18;
	static final int ORIGIN_NM1_COLUMN = 19;
	static final int ORIGIN_NM2_COLUMN = 20;
	static final int TXT1_COLUMN = 21;
	static final int TXT2_COLUMN = 22;
	static final int NOTES1_COLUMN = 23;
	static final int NOTES2_COLUMN = 24;
	static final int ERROR1_COLUMN = 25;
	static final int LEVEL1_COLUMN = 26;
	static final int LEVEL2_COLUMN = 27;
	static final int LEVEL3_COLUMN = 28;
	static final int LEVEL4_COLUMN = 29;
	static final int LEVEL5_COLUMN = 30;
		
	public ObjectReport(CommandLine cmd) {
		if (cmd.hasOption("rpt")) {
			this.output_path = cmd.getOptionValue("rpt") + cmd.getOptionValue("pfm") + "_TestResults_Objects_"
					+ CURR_DATE_STR + ".xls";
		} else {
			this.output_path = "./" + cmd.getOptionValue("pfm") + "_TestResults_Objects_" + CURR_DATE_STR + ".xls";
		}
	}

	public ObjectReport(Properties prop) {
		this.output_path = prop.getProperty(OBJECT_OUTPUT_PATH) + "TestResults_Object_" + CURR_DATE_STR + ".xls";
	}

	protected void logAddSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		ObjectTuple objectTuple2 = (ObjectTuple) detail.getTuple2();
		logText(sheet, rownum, 2, "NULL/" + objectTuple2.getId());
	}

	protected void logDelSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		ObjectTuple objectTuple1 = (ObjectTuple) detail.getTuple1();
		logText(sheet, rownum, 2, objectTuple1.getId() + "/NULL");
	}

	protected void logModSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		ObjectTuple objectTuple1 = (ObjectTuple) detail.getTuple1();
		ObjectTuple objectTuple2 = (ObjectTuple) detail.getTuple2();
		logText(sheet, rownum, 2, objectTuple1.getId() + "/" + objectTuple2.getId());
	}	

	public void logTupleCompare(HSSFSheet sheet, int rownum, Tuple tuple1, Tuple tuple2) {		
		if (tuple1 != null) {
			ObjectTuple objectTuple1 = (ObjectTuple)tuple1;
			logText(sheet, rownum, INDEX1_COLUMN, new Integer(objectTuple1.getIndex()).toString());
			logText(sheet, rownum, UNIQUE_NAME1_COLUMN, objectTuple1.getUniqueName());
			logText(sheet, rownum, ID1_COLUMN, objectTuple1.getId());
			logText(sheet, rownum, PARENT_ID1_COLUMN, objectTuple1.getParent_id());
			logText(sheet, rownum, TYPE_ID1_COLUMN, objectTuple1.getType_id());
			logText(sheet, rownum, NAME1_COLUMN, objectTuple1.getName());
			logText(sheet, rownum, EXT1_COLUMN, objectTuple1.getExt());
			logText(sheet, rownum, DESC1_COLUMN, objectTuple1.getDesc());
			logText(sheet, rownum, ORIGIN_DS1_COLUMN, objectTuple1.getOrigin_ds());
			logText(sheet, rownum, ORIGIN_NM1_COLUMN, objectTuple1.getOrigin_nm());
			logText(sheet, rownum, TXT1_COLUMN, objectTuple1.getTxt());
			logText(sheet, rownum, NOTES1_COLUMN, objectTuple1.getNotes());
			logText(sheet, rownum, ERROR1_COLUMN, objectTuple1.getError());
			logText(sheet, rownum, LEVEL1_COLUMN, extractTestCaseName(objectTuple1.getUniqueName(),0));
			logText(sheet, rownum, LEVEL2_COLUMN, extractTestCaseName(objectTuple1.getUniqueName(),1));
			logText(sheet, rownum, LEVEL3_COLUMN, extractTestCaseName(objectTuple1.getUniqueName(),2));
			logText(sheet, rownum, LEVEL4_COLUMN, extractTestCaseName(objectTuple1.getUniqueName(),3));
			logText(sheet, rownum, LEVEL5_COLUMN, extractTestCaseName(objectTuple1.getUniqueName(),4));
		}
		if (tuple2 != null) {
			ObjectTuple objectTuple2 = (ObjectTuple)tuple2;
			logText(sheet, rownum, INDEX2_COLUMN, new Integer(objectTuple2.getIndex()).toString());
			logText(sheet, rownum, UNIQUE_NAME2_COLUMN, objectTuple2.getUniqueName());
			logText(sheet, rownum, ID2_COLUMN, objectTuple2.getId());
			logText(sheet, rownum, PARENT_ID2_COLUMN, objectTuple2.getParent_id());
			logText(sheet, rownum, TYPE_ID2_COLUMN, objectTuple2.getType_id());
			logText(sheet, rownum, NAME2_COLUMN, objectTuple2.getName());
			logText(sheet, rownum, EXT2_COLUMN, objectTuple2.getExt());
			logText(sheet, rownum, DESC2_COLUMN, objectTuple2.getDesc());
			logText(sheet, rownum, ORIGIN_DS2_COLUMN, objectTuple2.getOrigin_ds());
			logText(sheet, rownum, ORIGIN_NM2_COLUMN, objectTuple2.getOrigin_nm());
			logText(sheet, rownum, TXT2_COLUMN, objectTuple2.getTxt());
			logText(sheet, rownum, NOTES2_COLUMN, objectTuple2.getNotes());
			logText(sheet, rownum, LEVEL1_COLUMN, extractTestCaseName(objectTuple2.getUniqueName(),0));
			logText(sheet, rownum, LEVEL2_COLUMN, extractTestCaseName(objectTuple2.getUniqueName(),1));
			logText(sheet, rownum, LEVEL3_COLUMN, extractTestCaseName(objectTuple2.getUniqueName(),2));
			logText(sheet, rownum, LEVEL4_COLUMN, extractTestCaseName(objectTuple2.getUniqueName(),3));
			logText(sheet, rownum, LEVEL5_COLUMN, extractTestCaseName(objectTuple2.getUniqueName(),4));
		}
	}
	
	protected String[] getSummaryHeading() {
		return new String[] { "UNIQUE NAME", "COMMENTS", "GOLD vs NEW ID" };
	}	

	protected String[] getResultHeading() {
		return new String[] { "OBSERVATION", "LINENUM1", "LINENUM2", "UNIQUE_NAME1", "UNIQUE_NAME2", "ID1", "ID2",
				"PARENT_ID1", "PARENT_ID2", "TYPE_ID1", "TYPE_ID2", "NAME1", "NAME2", "EXT1", "EXT2", "DESC1", "DESC2",
				"ORIGIN_DS1", "ORIGIN_DS2", "ORIGIN_NM1", "ORIGIN_NM2", "TXT1", "TXT2", "NOTES1", "NOTES2",
				"ERROR DETAILS", "LEVEL 1", "LEVEL 2", "LEVEL 3", "LEVEL 4", "LEVEL 5",};
	}
	
	private String extractTestCaseName(String uniqueName, int occurenceCount){
		int occurances = uniqueName.split("#").length;
		if(occurenceCount<occurances) {
			return uniqueName.split("#")[occurenceCount];
		}
		return "";
	}
}
