package com.auction.report;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.Tuple;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
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
	
	@Autowired
	private ExcelUtil excelUtil;
	
	private static final int COLUMNLENGTH = 5;
	
	
    public void export(HttpServletResponse response, Long auctionId) throws IOException {
    	XSSFWorkbook workbook = this.excelUtil.getWorkBook();
    	XSSFSheet sheet = this.excelUtil.getSheet(workbook, "Auction "+auctionId+" Bidding");
    	this.excelUtil.setColumnsWidth(sheet);
        this.excelUtil.headline(workbook, sheet, LoggedInUser.getLoggedInUserDetails().getOrganization().getOrgName(), COLUMNLENGTH);
        this.excelUtil.emptyRow(sheet, COLUMNLENGTH, 2);
        int row = 4;
        this.excelUtil.header(workbook, sheet, row, this.auctionHeaders);
        row = renderAuctionData(workbook, sheet, row+1, auctionId);
        row = row+2;
        this.excelUtil.header(workbook, sheet, row , this.biddingHeader);
        renderBiddingDataForAdmin(workbook, sheet, row+1, auctionId);
        
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
         
        outputStream.close();
         
    }
    
    
    public void exportForBidder(HttpServletResponse response, Long auctionId) throws IOException {
    	XSSFWorkbook workbook = this.excelUtil.getWorkBook();
    	XSSFSheet sheet = this.excelUtil.getSheet(workbook, "Auction "+auctionId+" Bidding");
    	this.excelUtil.setColumnsWidth(sheet);
        this.excelUtil.headline(workbook, sheet, this.getOrganizationNameForHeadline(auctionId), COLUMNLENGTH);
        this.excelUtil.emptyRow(sheet, COLUMNLENGTH, 2);
        int row = 4;
        this.excelUtil.header(workbook, sheet, row, this.auctionHeaders);
        row = renderAuctionData(workbook, sheet, row+1, auctionId);
        row = row+2;
        this.excelUtil.header(workbook, sheet, row , this.biddingHeader);
        renderBiddingDataForBidder(workbook, sheet, row+1, auctionId);
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();   
    }
    
    
    private int renderAuctionData(XSSFWorkbook workbook,XSSFSheet sheet, int rowIndex, Long auctionId) {
        List<Tuple> tuples =	this.auctionPreparationDao.findAuctionDetailForReport(auctionId);
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);
        for (Tuple tuple : tuples) {
            Row row = sheet.createRow(rowIndex);      
            this.excelUtil.createCell(row, 0, String.valueOf(auctionId), style, sheet);
            this.excelUtil.createCell(row, 1, tuple.get("description").toString(), style, sheet);      
            this.excelUtil.createCell(row, 2, Double.parseDouble(tuple.get("basePrice").toString()), style, sheet);       
            this.excelUtil.createCell(row, 3, tuple.get("startDateTime", LocalDateTime.class), style, sheet);    
            this.excelUtil.createCell(row, 4, tuple.get("endDateTime", LocalDateTime.class), style, sheet);
            this.excelUtil.createCell(row, 5, Double.parseDouble(tuple.get("incrementValue").toString()) , style, sheet);
            rowIndex++;
		}
        return rowIndex;
    }
    
    private int renderBiddingDataForAdmin(XSSFWorkbook workbook,XSSFSheet sheet, int rowIndex, Long auctionId) {
        List<Tuple> tuples =	this.biddingDao.findBidHistoryByAuctionPreparationForReport(auctionId);
        return this.renderBidding(rowIndex, tuples, sheet, workbook);
    }
    
    private int renderBiddingDataForBidder(XSSFWorkbook workbook,XSSFSheet sheet, int rowIndex, Long auctionId) {
        List<Tuple> tuples =	this.biddingDao.findBidHistoryByAuctionPreparationForBidderReport(auctionId, LoggedInUser.getLoggedInUserDetails().getId());
        return this.renderBidding(rowIndex, tuples, sheet, workbook);
    }
    
    
    private int renderBidding(int rowIndex, List<Tuple> tuples, XSSFSheet sheet, XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(10);
        style.setFont(font);
        for (Tuple tuple : tuples) {
            Row row = sheet.createRow(rowIndex);      
            Object closedAt = tuple.get("closeAt");
            this.excelUtil.createCell(row, 0, tuple.get("firstName").toString().concat(" "+tuple.get("lastName")), style, sheet);      
            this.excelUtil.createCell(row, 1, Double.parseDouble(tuple.get("amount").toString()), style, sheet);       
            this.excelUtil.createCell(row, 2, tuple.get("bidAt", Timestamp.class).toLocalDateTime(), style, sheet);    
            this.excelUtil.createCell(row, 3, (Objects.isNull(closedAt) || closedAt.toString().isBlank()) ? "" : "H1", style, sheet);
            rowIndex++;
		}
       return rowIndex; 
    }

    private String getOrganizationNameForHeadline(Long auctionId) {
    	return this.auctionPreparationDao.findAuctionNameById(auctionId).split("/",1)[0];
    }
}
