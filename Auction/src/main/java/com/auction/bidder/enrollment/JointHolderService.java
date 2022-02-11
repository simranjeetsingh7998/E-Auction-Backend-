package com.auction.bidder.enrollment;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.global.exception.DataMisMatchException;
import com.auction.global.exception.ResourceNotFoundException;
import com.auction.preparation.AuctionPreparation;

@Service
public class JointHolderService implements IJointHolderService {
	
	@Autowired
	private IJointHolderDao jointHolderDao;
	
	@Autowired
	private IBidderAuctionEnrollmentDao bidderAuctionEnrollmentDao;

	@Override
	public JointHolderVO save(Long bidderAuctionEnrollmentId, JointHolderVO jointHolderVO) {
		BidderAuctionEnrollment bidderAuctionEnrollment = this.bidderAuctionEnrollmentDao.findById(bidderAuctionEnrollmentId)
		.orElseThrow(() -> new ResourceNotFoundException("Please add yourself to auction before adding Joint holder"));
		AuctionPreparation auctionPreparation = bidderAuctionEnrollment.getAuctionPreparation();
		LocalDateTime currentDateTime =  LocalDateTime.now();
		if(auctionPreparation.getRegistrationEndDateTime().isBefore(currentDateTime)) {
			throw new DataMisMatchException("Auction registration is closed");
		}
		JointHolder jointHolder = jointHolderVO.jointHolderVOToJointHolder();
		jointHolder.setBidderAuctionEnrollment(bidderAuctionEnrollment);
		Long jointHolderId = this.jointHolderDao.save(jointHolder).getId();
		jointHolderVO.setId(jointHolderId);
		return jointHolderVO;
	}
	
	@Override
	public List<JointHolderVO> findAllByBidderAuctionEnrollmentId(Long bidderAuctionEnrollmentId) {
		BidderAuctionEnrollment bidderAuctionEnrollment = new BidderAuctionEnrollment();
		bidderAuctionEnrollment.setId(bidderAuctionEnrollmentId);
		return this.jointHolderDao.findAllByBidderAuctionEnrollment(bidderAuctionEnrollment)
				.stream().map(JointHolder::jointHolderToJointHolderVO).toList();
	}
	
	@Override
	public void delete(Long id) {
		JointHolder jointHolder = this.jointHolderDao.findById(id).orElseThrow(()
				-> new ResourceNotFoundException("Joint holder not found"));
		this.jointHolderDao.delete(jointHolder);
	}

}
