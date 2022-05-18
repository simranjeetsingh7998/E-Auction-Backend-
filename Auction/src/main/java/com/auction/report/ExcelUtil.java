package com.auction.report;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class ExcelUtil {
	
    protected XSSFWorkbook getWorkBook() {
    	return new XSSFWorkbook();
    }
    
    protected XSSFSheet getSheet(XSSFWorkbook xssfWorkbook, String sheetName) {
    	 return xssfWorkbook.createSheet(sheetName);
    }
    
    protected void headline(XSSFWorkbook workbook, XSSFSheet sheet, String organization, int columnLength) {
        Row row = sheet.createRow(0);
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        setBackGroundColor(style);
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        Cell cell = row.createCell(0);
        cell.setCellStyle(style);
        cell.setCellValue(organization);
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,columnLength));
        for(int i = 0; i< columnLength; i++) {
        sheet.setColumnWidth(i, 6000);
        }
    }
    
    private void setBackGroundColor(CellStyle style) {
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
    
    protected void emptyRow(XSSFSheet sheet, int columnLength, int row) {
        sheet.addMergedRegion(new CellRangeAddress(row,row,0,26));
        for(int i = 0; i< columnLength; i++) {
           sheet.setColumnWidth(i, 6000);
        }
    }
    
    protected void header(XSSFWorkbook workbook, XSSFSheet sheet, int rowNumber, List<String> headers) {
        Row row = sheet.createRow(rowNumber);      
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        setBackGroundColor(style);
        style.setBorderBottom(BorderStyle.MEDIUM);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(BorderStyle.MEDIUM);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(BorderStyle.MEDIUM);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(BorderStyle.MEDIUM);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        style.setWrapText(true);
        for(int i = 0; i< headers.size(); i++) {
          createCell(row, i, headers.get(i) , style, sheet);   
        }
}
    
    
    protected void createCell(Row row, int cellIndex, Object cellValue, CellStyle cellStyle, XSSFSheet sheet) {
     //   sheet.autoSizeColumn(cellIndex);
        Cell cell = row.createCell(cellIndex);
        if (cellValue instanceof Double value) {
            cell.setCellValue(value);
            cellStyle.setDataFormat((short)7);
        } else if(cellValue instanceof LocalDateTime dt) {
        	cell.setCellValue(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss").format(dt));
        } 
        else {
            cell.setCellValue((String) cellValue);
        }
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cell.setCellStyle(cellStyle); 	
    }
    
    protected void setColumnsWidth(XSSFSheet sheet) {
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
    }

}
