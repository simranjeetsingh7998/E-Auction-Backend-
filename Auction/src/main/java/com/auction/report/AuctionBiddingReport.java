package com.auction.report;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.Tuple;
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

import com.auction.bidding.IBiddingDao;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.util.LoggedInUser;

@Component
public class AuctionBiddingReport {
	
	private List<String> auctionHeaders = List.of("Id","Auction Name","Base Price","Auction Start Date"+System.lineSeparator()+" and Time"
			,"Auction End Date"+System.lineSeparator()+" and Time", "Increment"+System.lineSeparator()+" Price");
	
	private List<String> biddingHeader = List.of("Bidder Name", "Bid Amount", "Bidding Date and Time", "Rank");
	
	@Autowired
	private IBiddingDao biddingDao;
	
	@Autowired
    private IAuctionPreparationDao auctionPreparationDao;
	
	
    public void export(HttpServletResponse response, Long auctionId) throws IOException {
    	XSSFWorkbook workbook = this.getWorkBook();
    	XSSFSheet sheet = this.getSheet(workbook);
    	this.setColumnsWidth(sheet);
        headline(workbook, sheet);
        emptyRow(sheet);
        int row = 4;
        header(workbook, sheet, row, this.auctionHeaders);
        row = renderAuctionData(workbook, sheet, row+1, auctionId);
        row = row+2;
        header(workbook, sheet, row , this.biddingHeader);
        renderBiddingData(workbook, sheet, row+1, auctionId);
        
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
       // cell.setCellValue("Department of Horticulture, Haryana");
        cell.setCellValue(LoggedInUser.getLoggedInUserDetails().getOrganization().getOrgName());
        sheet.addMergedRegion(new CellRangeAddress(0,1,0,5));
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
    
    private void header(XSSFWorkbook workbook, XSSFSheet sheet, int rowNumber, List<String> headers) {
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
    
    private void createCell(Row row, int cellIndex, Object cellValue, CellStyle cellStyle, XSSFSheet sheet) {
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
    
    private int renderAuctionData(XSSFWorkbook workbook,XSSFSheet sheet, int rowIndex, Long auctionId) {
        List<Tuple> tuples =	this.auctionPreparationDao.findAuctionDetailForReport(auctionId);
        for (Tuple tuple : tuples) {
            Row row = sheet.createRow(rowIndex);      
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(10);
            style.setFont(font);
            createCell(row, 0, String.valueOf(auctionId), style, sheet);
            createCell(row, 1, tuple.get("description").toString(), style, sheet);      
            createCell(row, 2, Double.parseDouble(tuple.get("basePrice").toString()), style, sheet);       
            createCell(row, 3, tuple.get("startDateTime", LocalDateTime.class), style, sheet);    
            createCell(row, 4, tuple.get("endDateTime", LocalDateTime.class), style, sheet);
            createCell(row, 5, Double.parseDouble(tuple.get("incrementValue").toString()) , style, sheet);
            rowIndex++;
		}
        return rowIndex;
    }
    
    private int renderBiddingData(XSSFWorkbook workbook,XSSFSheet sheet, int rowIndex, Long auctionId) {
        List<Tuple> tuples =	this.biddingDao.findBidHistoryByAuctionPreparationForReport(auctionId);
        int temp = rowIndex;
        for (Tuple tuple : tuples) {
            Row row = sheet.createRow(rowIndex);      
            CellStyle style = workbook.createCellStyle();
            XSSFFont font = workbook.createFont();
            font.setFontHeight(10);
            style.setFont(font);
            createCell(row, 0, tuple.get("firstName").toString().concat(" "+tuple.get("lastName")), style, sheet);      
            createCell(row, 1, Double.parseDouble(tuple.get("amount").toString()), style, sheet);       
            createCell(row, 2, tuple.get("bidAt", Timestamp.class).toLocalDateTime(), style, sheet);    
            createCell(row, 3, rowIndex > temp ? "" : "H1", style, sheet);
            rowIndex++;
		}
        return rowIndex;
    }
    
    private void setColumnsWidth(XSSFSheet sheet) {
        sheet.setColumnWidth(0, 5000);
        sheet.setColumnWidth(1, 5000);
        sheet.setColumnWidth(2, 5000);
        sheet.setColumnWidth(3, 5000);
        sheet.setColumnWidth(4, 5000);
        sheet.setColumnWidth(5, 5000);
    }
    
    private XSSFWorkbook getWorkBook() {
    	return new XSSFWorkbook();
    }
    
    private XSSFSheet getSheet(XSSFWorkbook xssfWorkbook) {
    	 return xssfWorkbook.createSheet("Auction Bidding");
    }

    

}
