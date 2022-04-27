package com.auction.bidder.enrollment;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.auction.emd.fee.payment.mode.EmdFeePaymentModeEnum;
import com.auction.emd.fee.payment.mode.IEMDFeePaymentModeDao;
import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceAlreadyExist;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.method.IAuctionMethodDao;
import com.auction.preparation.AuctionMethodEnum;
import com.auction.preparation.AuctionPreparation;
import com.auction.preparation.AuctionStatus;
import com.auction.preparation.IAuctionPreparationDao;
import com.auction.user.User;
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

	@Autowired
	private IEMDFeePaymentModeDao feePaymentModeDao;

	@Autowired
	private IAuctionMethodDao auctionMethodDao;

	@Override
	public BidderAuctionEnrollmentVO save(String bidderAuctionEnrollmentJson, MultipartFile document)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO = mapper.readValue(bidderAuctionEnrollmentJson,
				BidderAuctionEnrollmentVO.class);
		AuctionPreparation auctionPreparation = this.auctionPreparationDao
				.findById(bidderAuctionEnrollmentVO.getAuctionPreparation().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		User user = LoggedInUser.getLoggedInUserDetails().getUser();
		bidderAuctionEnrollmentValidations(auctionPreparation, user);
		BidderAuctionEnrollment bidderAuctionEnrollment = bidderAuctionEnrollmentVO
				.bidderAuctionEnrollmentVOToBidderAuctionEnrollment();
		bidderAuctionEnrollment.setAuctionPreparation(auctionPreparation);
		bidderAuctionEnrollment.setUser(user);
		bidderAuctionEnrollment
				.setEmdAmount(auctionPreparation.getEmdFeeAmount() * bidderAuctionEnrollment.getEmdLimit());
		bidderAuctionEnrollment.setEventProcessingFeeAmount(
				auctionPreparation.getEventProcessingFeeAmount() * bidderAuctionEnrollment.getEmdLimit());
		for (JointHolderVO jointHolderVO : bidderAuctionEnrollmentVO.getJointHolderVOs()) {
			bidderAuctionEnrollment.addJointHolder(jointHolderVO.jointHolderVOToJointHolder());
		}
		bidderAuctionEnrollment.setAddressProof(uploadAndGetDocumentUrl(auctionPreparation.getId(),
				DocumentTypeEnum.ADDRESSPROOF.getDocumentType(), document));
		boolean isAuctionNormalAndOffline = isBidderAuctionEnrollmentVerified(auctionPreparation);
		bidderAuctionEnrollment.setVerified(!isAuctionNormalAndOffline);
		Long id = this.bidderAuctionEnrollmentDao.save(bidderAuctionEnrollment).getId();
		bidderAuctionEnrollmentVO.setId(id);
		return bidderAuctionEnrollmentVO;
	}

	private void bidderAuctionEnrollmentValidations(AuctionPreparation auctionPreparation, User user) {
		if (this.bidderAuctionEnrollmentDao.existsByAuctionPreparationAndUser(auctionPreparation, user))
			throw new ResourceAlreadyExist("You have already enrolled for this auction");
		if (!auctionPreparation.getAuctionStatus().getStatus().equalsIgnoreCase(AuctionStatus.PUBLISH.getStatus())) {
			throw new DataMisMatchException("Auction is not published yet");
		}
		LocalDateTime currentDateTime = LocalDateTime.now();
		if (currentDateTime.isBefore(auctionPreparation.getRegistrationStartDateTime())) {
			throw new DataMisMatchException("Auction registration is not started yet");
		}
		if (currentDateTime.isAfter(auctionPreparation.getRegistrationEndDateTime())) {
			throw new DataMisMatchException("Auction registration is closed");
		}
	}

	private String uploadAndGetDocumentUrl(Long auctionPreparationId, String documentType, MultipartFile document)
			throws IOException {
		StringBuilder directory = this.fileUpload.getDirectory(FileUpload.AUCTIONDIRECTORY, "" + auctionPreparationId,
				documentType);
		StringBuilder fileName = new StringBuilder(UUID.randomUUID().toString());
		String fileOriginalName = document.getOriginalFilename();
		fileName.append(fileOriginalName.substring(fileOriginalName.lastIndexOf(".")));
		this.fileUpload.uploadMultipartDocument(directory.toString(), fileName.toString(), document);
		return directory.append(File.separator).append(fileName.toString()).toString();
	}

	private boolean isBidderAuctionEnrollmentVerified(AuctionPreparation auctionPreparation) {
		return this.feePaymentModeDao.findAllByAuctionPreparations(auctionPreparation).get(0).getEmdfpMode()
				.equals(EmdFeePaymentModeEnum.OFFLINE.getPaymentMode())
				&& this.auctionMethodDao.findAllByAuctionPreparations(auctionPreparation).get(0).getMethod()
						.equals(AuctionMethodEnum.NORMAL.getMethod());

	}

	@Override
	public BidderAuctionEnrollmentVO updateBidderAuctionEnrollment(Long bidderAuctionEnrollmentId,
			String bidderAuctionEnrollmentJson, MultipartFile document) throws IOException {
		BidderAuctionEnrollment auctionEnrollment = this.bidderAuctionEnrollmentDao.findById(bidderAuctionEnrollmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Bidder Auction Enrollment not found"));
		ObjectMapper mapper = new ObjectMapper();
		BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO = mapper.readValue(bidderAuctionEnrollmentJson,
				BidderAuctionEnrollmentVO.class);
		AuctionPreparation auctionPreparation = this.auctionPreparationDao
				.findById(bidderAuctionEnrollmentVO.getAuctionPreparation().getId())
				.orElseThrow(() -> new ResourceNotFoundException("Auction not found"));
		User user = LoggedInUser.getLoggedInUserDetails().getUser();
		bidderAuctionEnrollmentValidations(auctionPreparation, user);
		BidderAuctionEnrollment bidderAuctionEnrollment = bidderAuctionEnrollmentVO
				.bidderAuctionEnrollmentVOToBidderAuctionEnrollment();
		bidderAuctionEnrollment.setAuctionPreparation(auctionPreparation);
		bidderAuctionEnrollment.setUser(user);
		bidderAuctionEnrollment
				.setEmdAmount(auctionPreparation.getEmdFeeAmount() * bidderAuctionEnrollment.getEmdLimit());
		bidderAuctionEnrollment.setEventProcessingFeeAmount(
				auctionPreparation.getEventProcessingFeeAmount() * bidderAuctionEnrollment.getEmdLimit());
		for (JointHolderVO jointHolderVO : bidderAuctionEnrollmentVO.getJointHolderVOs()) {
			bidderAuctionEnrollment.addJointHolder(jointHolderVO.jointHolderVOToJointHolder());
		}
		if (!Objects.isNull(document))
			bidderAuctionEnrollment.setAddressProof(uploadAndGetDocumentUrl(auctionPreparation.getId(),
					DocumentTypeEnum.ADDRESSPROOF.getDocumentType(), document));
		else {
			bidderAuctionEnrollment.setAddressProof(auctionEnrollment.getAddressProof());
			bidderAuctionEnrollment.setTransactionProof(auctionEnrollment.getTransactionProof());
		}
		boolean isAuctionNormalAndOffline = isBidderAuctionEnrollmentVerified(auctionPreparation);
		bidderAuctionEnrollment.setVerified(!isAuctionNormalAndOffline);
		Long id = this.bidderAuctionEnrollmentDao.save(bidderAuctionEnrollment).getId();
		bidderAuctionEnrollmentVO.setId(id);
		return bidderAuctionEnrollmentVO;
	}

	@Override
	public void uploadBidderAuctionEnrollmentDocument(Long bidderAuctionEnrollmentId, String documentType,
			MultipartFile file) throws IOException {
		BidderAuctionEnrollment bidderAuctionEnrollment = this.bidderAuctionEnrollmentDao
				.findById(bidderAuctionEnrollmentId)
				.orElseThrow(() -> new ResourceNotFoundException("Bidder Auction Enrollment not found"));
		AuctionPreparation auctionPreparation = bidderAuctionEnrollment.getAuctionPreparation();
		if (isBidderAuctionEnrollmentVerified(auctionPreparation)) {
			if (documentType.equalsIgnoreCase(DocumentTypeEnum.ADDRESSPROOF.getDocumentType())) {
				String url = uploadAndGetDocumentUrl(bidderAuctionEnrollmentId,
						DocumentTypeEnum.ADDRESSPROOF.getDocumentType(), file);
				bidderAuctionEnrollment.setAddressProof(url);
			} else if (documentType.equalsIgnoreCase(DocumentTypeEnum.TRANSACTIONPROOF.getDocumentType())) {
				String url = uploadAndGetDocumentUrl(bidderAuctionEnrollmentId,
						DocumentTypeEnum.TRANSACTIONPROOF.getDocumentType(), file);
				bidderAuctionEnrollment.setTransactionProof(url);
			}
			this.bidderAuctionEnrollmentDao.save(bidderAuctionEnrollment);
		}
	}

	@Override
	public List<BidderAuctionEnrollmentVO> findAllByAuctionPreparation(Long auctionPreparationId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionPreparationId);
		return this.bidderAuctionEnrollmentDao.findAllByAuctionPreparation(auctionPreparation).stream()
				.map(BidderAuctionEnrollment::bidderAuctionEnrollmentToBidderAuctionEnrollmentVO).toList();
	}

	@Override
	public BidderAuctionEnrollmentVO findByAuctionIdAndBidder(Long auctionId) {
		AuctionPreparation auctionPreparation = new AuctionPreparation();
		auctionPreparation.setId(auctionId);
		BidderAuctionEnrollment bidderAuctionEnrollment = this.bidderAuctionEnrollmentDao
				.findByAuctionPreparationAndUser(auctionPreparation, LoggedInUser.getLoggedInUserDetails().getUser())
				.orElse(new BidderAuctionEnrollment());
		BidderAuctionEnrollmentVO bidderAuctionEnrollmentVO = bidderAuctionEnrollment
				.bidderAuctionEnrollmentToBidderAuctionEnrollmentVO();
		bidderAuctionEnrollmentVO.getJointHolderVOs().addAll(bidderAuctionEnrollment.getJointHolders().stream()
				.map(JointHolder::jointHolderToJointHolderVO).toList());
		return bidderAuctionEnrollmentVO;
	}

	@Override
	public void bidderAuctionEnrollmentVerification(BidderEnrollmentVerificationVO bidderEnrollmentVerificationVO) {
//		boolean flag = true;
//		List<BidderAuctionEnrollment> auctionEnrollments = this.bidderAuctionEnrollmentDao.findAllByAuctionPreparationId(bidderEnrollmentVerificationVO.getAuctionId());
//		for(BidderAuctionEnrollment bidder : auctionEnrollments)
//		{
//			if(bidder.getId().equals(bidderEnrollmentVerificationVO.getBidderEnrollmentid())) {
//				bidder.setVerified(bidderEnrollmentVerificationVO.getIsVerified());
//				bidderAuctionEnrollmentDao.save(bidder);
//				
//				flag = false;
//				break;
//			}
//		}	
//		if(flag) {
//			 throw new DataMisMatchException("Entity not Found");
//		}
		Optional<BidderAuctionEnrollment> bidderAuctionEnrollment = this.bidderAuctionEnrollmentDao.findByAuctionPreparationIdAndId(bidderEnrollmentVerificationVO.getAuctionId(),bidderEnrollmentVerificationVO.getBidderEnrollmentid());
		if(bidderAuctionEnrollment.isPresent()) {
			BidderAuctionEnrollment bidder=	bidderAuctionEnrollment.get();
			bidder.setVerified(bidderEnrollmentVerificationVO.getIsVerified());
			bidderAuctionEnrollmentDao.save(bidder);
		}
		else {
			throw new DataMisMatchException("Entity not Found");
		}
	}

}
