package com.orion.regression.report;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

import com.orion.regression.bean.Outcome;
import com.orion.regression.bean.OutcomeDetail;
import com.orion.regression.bean.Redundancy;
import com.orion.regression.bean.Tuple;

public abstract class Report {
	private static final Logger log = Logger.getLogger(Report.class.getName());

	String output_path = null;

	static int sheetNumberIndex = 0;
	static final int OBSERVATION_COLUMN = 0;
	static final String CURR_DATE_STR = new SimpleDateFormat("MMddyy").format(new Date());

	static HSSFCellStyle passStyle = null;
	static HSSFCellStyle failStyle = null;
	static HSSFCellStyle warnStyle = null;
	static HSSFCellStyle headerStyle = null;

	static DocumentBuilder builder = null;
	static XPath xPath = XPathFactory.newInstance().newXPath();

	
	protected void logOutcome(HSSFSheet sheet, int rownum, OutcomeDetail detail) {
		Tuple tuple1 = detail.getTuple1();
		Tuple tuple2 = detail.getTuple2();		
		logTupleCompare(sheet,rownum,tuple1,tuple2);		
	}
	
	public abstract void logTupleCompare(HSSFSheet sheet, int rownum, Tuple tuple1, Tuple tuple2);
	
	public void logRedundancies(HSSFSheet sheet, List<Redundancy> redundancies) {
		int rownum = 0;
		for (Redundancy redundancy : redundancies) {
			logText(sheet, ++rownum, OBSERVATION_COLUMN, "REDUNANT");
			// Color coding the observation
			setOutcomeCellStyle(sheet, rownum, OBSERVATION_COLUMN, Style.WARN);
			Tuple tuple1 = redundancy.getDuplicateTuple();
			Tuple tuple2 = redundancy.getOriginalTuple();
			logTupleCompare(sheet,rownum,tuple1,tuple2);
		}
	}
	
	/**
	 * Logs all the result of comparison into an Excel Sheet(on the order of File
	 * Manager entries)
	 * 
	 * @param summary
	 * @param hdbName
	 * @throws Exception
	 */
	public void logResults(Outcome outcome) throws Exception {
		File file = createFile(output_path);
		String absolutePath = file.getAbsolutePath();
		log.log(Level.DEBUG,"Absolute path of XLS report->"+absolutePath);
		int fileCount = 1;

		sheetNumberIndex = 0;
		int rownum = 0;

		HSSFWorkbook workbook = new HSSFWorkbook();
		passStyle = getSuccessStyle(workbook);
		failStyle = getFailureStyle(workbook);
		warnStyle = getWarningStyle(workbook);
		headerStyle = getHeaderStyle(workbook);

		HSSFSheet sheet = createCompareSummarySheet(workbook);
		List<OutcomeDetail> details = outcome.getDetails();
		for (OutcomeDetail detail : details) {
			if ("ADD".equals(detail.getObservation())) {
				logText(sheet, ++rownum, 0, detail.getTuple2().getUniqueName());
				logText(sheet, rownum, 1, "This Node is added");
				logAddSummary(sheet, rownum, detail);
			}
		}
		for (OutcomeDetail detail : details) {
			if ("DEL".equals(detail.getObservation())) {
				logText(sheet, ++rownum, 0, detail.getTuple1().getUniqueName());
				logText(sheet, rownum, 1, "This Node is deleted");
				logDelSummary(sheet, rownum, detail);
			}
		}
		for (OutcomeDetail detail : details) {
			if ("MOD".equals(detail.getObservation())) {
				logText(sheet, ++rownum, 0, detail.getTuple1().getUniqueName());
				logText(sheet, rownum, 1, detail.getTuple1().getError());
				logModSummary(sheet, rownum, detail);
			}
		}
		
		sheet = createStableRedundanciesSheet(workbook);
		logRedundancies(sheet, outcome.getStableRedundancies());
		sheet = createNextRedundanciesSheet(workbook);
		logRedundancies(sheet, outcome.getNextRedundancies());
		
		rownum = 0;
		sheet = createCompareResultSheet(workbook);
		log.log(Level.DEBUG, "Updating Compare Results entries on Sheet #" + (sheetNumberIndex + 1) + "..");
		for (OutcomeDetail detail : details) {
			rownum++;
			if (rownum == 65535) {
				if ((sheetNumberIndex) % 10 == 0) {
					log.log(Level.INFO, "File " + file.getName() + " has more than 10 Sheets..");
					FileOutputStream stream = new FileOutputStream(file);
					log.log(Level.INFO, "Flushing..");
					workbook.write(stream);
					stream.close();
					String regex = (fileCount) + ".xls";
					String replace = (++fileCount) + ".xls";
					Pattern p = Pattern.compile(regex);
					Matcher m = p.matcher(absolutePath);
					absolutePath = m.replaceAll(replace);
					file = new File(absolutePath);
					log.log(Level.INFO, "Created new File " + file.getName() + "..");
					workbook = new HSSFWorkbook();
					passStyle = getSuccessStyle(workbook);
					failStyle = getFailureStyle(workbook);
					warnStyle = getWarningStyle(workbook);
					headerStyle = getHeaderStyle(workbook);
				}
				log.log(Level.INFO, "Sheet #" + (sheetNumberIndex + 1)
						+ " exhausted after writing. Moving onto to sheet #" + (++sheetNumberIndex + 1));
				sheet = createCompareResultSheet(workbook);
				log.log(Level.INFO, "Updating Compare Results entries on Sheet #" + (sheetNumberIndex + 1) + "..");
				rownum = 1;
			}

			logText(sheet, rownum, OBSERVATION_COLUMN, detail.getObservation());
			// Color coding the observation
			setOutcomeCellStyle(sheet, rownum, OBSERVATION_COLUMN, detail.getStyle());
			logOutcome(sheet, rownum, detail);

		}
		FileOutputStream stream = new FileOutputStream(file);
		workbook.write(stream);
		stream.close();
		log.log(Level.INFO, "The Compare Results data has been written !!");
	}

	protected abstract void logAddSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail);

	protected abstract void logDelSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail);

	protected abstract void logModSummary(HSSFSheet sheet, int rownum, OutcomeDetail detail);
	
	/**
	 * Creates a file for a given path
	 * 
	 * @param path
	 * @return
	 */
	private File createFile(final String relPath) {
		Path path = Paths.get(".").resolve(relPath);
		/*
		//using PosixFilePermission to set file permissions 777
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        //add owners permission
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        //add group permissions
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_WRITE);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        //add others permissions
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_WRITE);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        */
        File file = null;
        //Files.setPosixFilePermissions(path, perms);
		file = new File(path.toString());
		//file =  Files.createFile(path).toFile();
		file.setExecutable(true, false);
		file.setReadable(true, false);
		file.setWritable(true, false);
        return file;
	}

	/**
	 * Create a work sheet to store the summary of Compare results
	 * 
	 * @param wb
	 * @return
	 */
	HSSFSheet createCompareSummarySheet(final HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet();
		wb.setSheetName(wb.getSheetIndex(sheet), "Summary");
		setHeading(sheet, getSummaryHeading());
		return sheet;
	}

	/**
	 * Create a work sheet to store the Stable Redundancies
	 * 
	 * @param wb
	 * @return
	 */
	HSSFSheet createStableRedundanciesSheet(final HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet();
		wb.setSheetName(wb.getSheetIndex(sheet), "Stable Redundants");
		setHeading(sheet, getRedundanciesHeading());
		return sheet;
	}

	/**
	 * Create a work sheet to store the Next Redundancies
	 * 
	 * @param wb
	 * @return
	 */
	HSSFSheet createNextRedundanciesSheet(final HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet();
		wb.setSheetName(wb.getSheetIndex(sheet), "Next Redundants");
		setHeading(sheet, getRedundanciesHeading());
		return sheet;
	}

	protected abstract String[] getSummaryHeading();
	
	/**
	 * Create a work sheet to store the detailed Compare results
	 * 
	 * @param wb
	 * @return
	 */
	HSSFSheet createCompareResultSheet(final HSSFWorkbook wb) {
		HSSFSheet sheet = wb.createSheet();
		wb.setSheetName(wb.getSheetIndex(sheet), (sheetNumberIndex) + "-CompareResults");

		setHeading(sheet, getResultHeading());
		return sheet;
	}

	protected String[] getRedundanciesHeading() {
		return getResultHeading();
	}
	
	protected abstract String[] getResultHeading();

	/**
	 * Sets a column header for a given sheet using header names from a String array
	 * 
	 * @param sheet
	 * @param colHeaders
	 */
	void setHeading(final HSSFSheet sheet, final String[] colHeaders) {
		for (int column = 0; column < colHeaders.length; column++) {
			logText(sheet, 0, column, colHeaders[column]);
			setOutcomeCellStyle(sheet, 0, column, Style.HEADER);
		}
	}

	/**
	 * Logs a float number on a given row and column of an excel sheet
	 * 
	 * @param sheet
	 * @param rownum
	 * @param columnnum
	 * @param message
	 * @param formattedStyle
	 */
	static void logFloat(final HSSFSheet sheet, final int rownum, final int columnnum, final String message,
			final CellStyle formattedStyle) {
		HSSFCell cell = getCell(sheet, rownum, columnnum);
		if (cell != null && message != null) {
			if (message.length() < 32767) {
				cell.setCellStyle(formattedStyle);
				cell.setCellValue(Float.parseFloat(message));
			} else {
				log.log(Level.INFO, "ERROR : Message Length Exceeded 32,767 -> " + message.length());
			}
		}

	}

	/**
	 * Returns a cell element for a given sheet, row and column
	 * 
	 * @param sheet
	 * @param rownum
	 * @param columnnum
	 * @return
	 */
	static HSSFCell getCell(final HSSFSheet sheet, final int rownum, final int columnnum) {
		HSSFRow portletRow = sheet.getRow(rownum);
		if (portletRow == null) {
			portletRow = sheet.createRow(rownum);
		}
		HSSFCell cell = portletRow.getCell(columnnum);
		if (cell == null) {
			cell = portletRow.createCell(columnnum);
		}
		return cell;
	}

	/**
	 * Logs a String message on a given row and column of an excel sheet
	 * 
	 * @param sheet
	 * @param rownum
	 * @param columnnum
	 * @param message
	 */
	static void logText(final HSSFSheet sheet, final int rownum, final int columnnum, final String message) {
		HSSFCell cell = getCell(sheet, rownum, columnnum);
		if (message != null) {
			if (message.length() < 32767) {
				cell.setCellValue(new HSSFRichTextString(message));
			} else {
				// log.log(Level.INFO, "ERROR : Message Length Exceeded 32,767 -> "+
				// message.length());
			}
		}

	}

	/**
	 * Sets the cell style properties based on the choice of argument. The various
	 * style elements supported are success, failure, warning and header.
	 * 
	 * @param sheet
	 * @param rownum
	 * @param columnnum
	 * @param style
	 */
	void setOutcomeCellStyle(final HSSFSheet sheet, final int rownum, final int columnnum, final Style style) {
		HSSFRow row = sheet.getRow(rownum);
		HSSFCell cell = row.getCell(columnnum);
		if (style == Style.PASS) {
			cell.setCellStyle(passStyle);
		} else if (style == Style.WARN) {
			cell.setCellStyle(warnStyle);
		} else if (style == Style.FAIL) {
			cell.setCellStyle(failStyle);
		} else if (style == Style.HEADER) {
			cell.setCellStyle(headerStyle);
		}
	}

	/**
	 * Returns instance of CellStyle for Success Criteria
	 * 
	 * @param wb
	 * @return cellStyle
	 */
	static HSSFCellStyle getSuccessStyle(final HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}

	/**
	 * Returns instance of CellStyle for Failure Criteria
	 * 
	 * @param wb
	 * @return cellStyle
	 */
	static HSSFCellStyle getFailureStyle(final HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}

	/**
	 * Returns instance of CellStyle for Warning Criteria
	 * 
	 * @param wb
	 * @return cellStyle
	 */
	static HSSFCellStyle getWarningStyle(final HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		return cellStyle;
	}

	/**
	 * Returns instance of CellStyle for Column Header
	 * 
	 * @param wb
	 * @return cellStyle
	 */
	static HSSFCellStyle getHeaderStyle(final HSSFWorkbook wb) {
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
		cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		HSSFFont font = wb.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);
		return cellStyle;
	}

}
