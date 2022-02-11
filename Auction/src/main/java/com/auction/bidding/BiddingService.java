package com.auction.bidding;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auction.user.User;
import com.auction.util.LoggedInUser;

@Service
public class BiddingService implements IBiddingService {
	
	@Autowired
	private IBiddingDao biddingDao;
	
	@Override
	public BiddingVO bidding(BiddingVO biddingVO) {
		User user = LoggedInUser.getLoggedInUserDetails().getUser();
		Bidding bidding = new Bidding();
		bidding.setId(biddingVO.getId());
		bidding.setBiddingAt(LocalDateTime.now());
		bidding.setBiddingAmount(biddingVO.getBiddingAmount());
		bidding.setBidder(user);
		bidding = this.biddingDao.save(bidding);
		biddingVO.setBidder(user.userToUserVO());
		biddingVO.setId(bidding.getId());
		biddingVO.setBiddingAt(bidding.getBiddingAt());
		return biddingVO;
	}

}
