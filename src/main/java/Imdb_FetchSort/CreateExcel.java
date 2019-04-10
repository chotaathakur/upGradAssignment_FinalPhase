package Imdb_FetchSort;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.hssf.usermodel.HSSFCell;

import org.apache.poi.hssf.usermodel.HSSFRow;

public class CreateExcel {

	public static HSSFWorkbook workbook;
	public static HSSFSheet sheet, sheet2,sheet3;
	public static HSSFRow rowhead, rowhead2,rowhead3;
	public static HSSFCell cell, cell2,cell3;
	public static CellStyle cellStyle;
	

	public static void setBorder(HSSFCell cell) {
		cellStyle = workbook.createCellStyle();

		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());

		cellStyle.setBorderTop(BorderStyle.THIN);
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

		cellStyle.setBorderLeft(BorderStyle.THIN);
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());

		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());

		cell.setCellStyle(cellStyle);
	}

	public static void setColor(HSSFCell cell) {
		Font headerFont = workbook.createFont();
		headerFont.setColor(IndexedColors.WHITE.getIndex());
		cellStyle.setFillBackgroundColor((IndexedColors.BLACK.getIndex()));
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		cellStyle.setFont(headerFont);
		cell.setCellStyle(cellStyle);
	}

}
