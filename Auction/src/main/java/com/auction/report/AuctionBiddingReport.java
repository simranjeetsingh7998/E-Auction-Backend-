package com.auction.report;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auction.bidding.IBiddingService;

@Component
public class AuctionBiddingReport {
	
	@Autowired
	private IBiddingService biddingService;
	
    public void export(HttpServletResponse response) throws IOException {
    	XSSFWorkbook workbook = this.getWorkBook();
    	XSSFSheet sheet = this.getSheet(workbook);
        headline(workbook, sheet);
        emptyRow(sheet);
        header(workbook, sheet);
        renderData(workbook, sheet);
        
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
    
    private void headline(XSSFWorkbook workbook, XSSFSheet sheet) {
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
        cell.setCellValue("Department of Horticulture, Haryana");
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,4));
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
    }
    
    private void setBackGroundColor(CellStyle style) {
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    }
    
    private void emptyRow(XSSFSheet sheet) {
        sheet.addMergedRegion(new CellRangeAddress(2,2,0,26));
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
    }
    
    private void header(XSSFWorkbook workbook, XSSFSheet sheet) {
    	        Row row = sheet.createRow(4);      
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
    	        createCell(row, 0, "Auction Name", style, sheet);      
    	        createCell(row, 1, "Base Price", style, sheet);       
    	        createCell(row, 2, "Auction Start Date"+System.lineSeparator()+" and Time", style, sheet);    
    	        createCell(row, 3, "Auction End Date"+System.lineSeparator()+" and Time", style, sheet);
    	        createCell(row, 4, "Increment"+System.lineSeparator()+" Price", style, sheet);
    }
    
    private void createCell(Row row, int cellIndex, Object cellValue, CellStyle cellStyle, XSSFSheet sheet) {
    	
     //   sheet.autoSizeColumn(cellIndex);
        Cell cell = row.createCell(cellIndex);
        if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
            cellStyle.setDataFormat((short)7);
        } else if (cellValue instanceof Boolean) {
            cell.setCellValue((Boolean) cellValue);
        }else {
            cell.setCellValue((String) cellValue);
        }
        cell.setCellStyle(cellStyle); 	
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
    }
    
    private void renderData(XSSFWorkbook workbook,XSSFSheet sheet) {
        Row row = sheet.createRow(5);      
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(10);
        style.setFont(font);
        createCell(row, 0, "Government Garden and Nursing, Barwala, Hisar", style, sheet);      
        createCell(row, 1, 750000, style, sheet);       
        createCell(row, 2, "06-05-2022  11:00:00", style, sheet);    
        createCell(row, 3, "06-05-2022  11:00:00", style, sheet);
        createCell(row, 4, 2000, style, sheet);
    }
    
    private XSSFWorkbook getWorkBook() {
    	return new XSSFWorkbook();
    }
    
    private XSSFSheet getSheet(XSSFWorkbook xssfWorkbook) {
    	 return xssfWorkbook.createSheet("Auction Bidding");
    }

    

}
