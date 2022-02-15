package com.auction.bidder.enrollment;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.util.FileUpload;
import com.auction.util.LoggedInUser;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional
public class BidderAuctionEnrollmentService implements IBidderAuctionEnrollmentService {
	
	@Autowired
	private IBidderAuctionEnrollmentDao bidderAuctionEnrollmentDao;
	
	@Autowired
	private IAuctionPreparationDao auctionPreparationDao;
	
	@Autowired
	private FileUpload fileUpload;
	
	@Override
	public void save(String bidderAuctionEnrollmentJson, MultipartFile document) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO = mapper.readValue(bidderAuctionEnrollmentJson, BidderAuctionEnrollmentVO.class);
		AuctionPreparation auctionPreparation = this.auctionPreparationDao.findById(bidderAuctionEnrollmentVO.getAuctionPreparation().getId())
		 .orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		if(auctionPreparation.getAuctionStatus().getStatus().equals(AuctionStatus.PUBLISH.getStatus())) {
			 throw new DataMisMatchException("Auction is not published yet");
		}
		LocalDateTime currentDateTime =  LocalDateTime.now();
		if(currentDateTime.isBefore(auctionPreparation.getRegistrationStartDateTime())) {
			throw new DataMisMatchException("Auction registration is not started yet");
		}
		if(currentDateTime.isAfter(auctionPreparation.getRegistrationEndDateTime())) {
			throw new DataMisMatchException("Auction registration is closed");
		}
		BidderAuctionEnrollment bidderAuctionEnrollment = bidderAuctionEnrollmentVO.bidderAuctionEnrollmentVOToBidderAuctionEnrollment();
		bidderAuctionEnrollment.setAuctionPreparation(auctionPreparation);
		bidderAuctionEnrollment.setUser(LoggedInUser.getLoggedInUserDetails().getUser());
		bidderAuctionEnrollment.setEmdAmount(auctionPreparation.getEmdFeeAmount()*bidderAuctionEnrollment.getEmdLimit());
		bidderAuctionEnrollment.setEventProcessingFeeAmount(auctionPreparation.getEventProcessingFeeAmount()*bidderAuctionEnrollment.getEmdLimit());
		for (JointHolderVO jointHolderVO : bidderAuctionEnrollmentVO.getJointHolderVOs()) {
		    bidderAuctionEnrollment.addJointHolder(jointHolderVO.jointHolderVOToJointHolder());	
		}
		StringBuilder directory = this.fileUpload.getDirectory(FileUpload.AUCTIONDIRECTORY, ""+auctionPreparation.getId(), "addpressproof"); 
		StringBuilder fileName = new StringBuilder(UUID.randomUUID().toString());
		String fileOriginalName = document.getOriginalFilename();
		fileName.append(fileOriginalName.substring(fileOriginalName.lastIndexOf(".")));
		this.fileUpload.uploadMultipartDocument(
				directory.toString(),fileName.toString(), document);
		bidderAuctionEnrollment.setAddressProof(directory.append(File.separator).append(fileName.toString()).toString());
	    this.bidderAuctionEnrollmentDao.save(bidderAuctionEnrollment);	
	}
	
	@Override
	public List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionPreparationId);
		return this.bidderAuctionEnrollmentDao.findAllByAuctionPreparation(auctionPreparation)
				.stream().map(BidderAuctionEnrollment::bidderAuctionEnrollmentToBidderAuctionEnrollmentVO).toList();
	}

}
