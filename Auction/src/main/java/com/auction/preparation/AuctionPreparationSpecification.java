package com.auction.preparation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.SetJoin;

import org.springframework.data.jpa.domain.Specification;

import com.auction.bidder.enrollment.BidderAuctionEnrollment;
import com.auction.util.LoggedInUser;

public class AuctionPreparationSpecification {
	
	private AuctionPreparationSpecification() {}
	
	 public static Specification<AuctionPreparation> registrationStartDateTimeGreaterThanEqualTo(LocalDateTime registrationStartDateTime) {
		    return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("registrationStartDateTime"), registrationStartDateTime);
	  }
	 
	 public static Specification<AuctionPreparation> registrationEndDateTimeLessThanEqualTo(LocalDateTime registrationEndDateTime) {
		    return (root, query, builder) ->  builder.lessThanOrEqualTo(root.get("registrationEndDateTime"), registrationEndDateTime);
	  }
	 
	 public static Specification<AuctionPreparation> descriptionLike(String description) {
		    return (root, query, builder) ->  builder.like(root.get("description"), description);
	  }
	 
	 public static Specification<AuctionPreparation> search(AuctionPreparationSearchParam auctionPreparationSearchParam){
		   Specification<AuctionPreparation> specification = null;
		      LocalDateTime regisrationStartDateTime = auctionPreparationSearchParam.getRegistrationStartDateTime();
		      LocalDateTime registrationEndDateTime = auctionPreparationSearchParam.getRegistrationEndDateTime();
		      String description = auctionPreparationSearchParam.getDescription();
		      if(!Objects.isNull(regisrationStartDateTime))
		    	    specification = registrationStartDateTimeGreaterThanEqualTo(regisrationStartDateTime);
		      if(!Objects.isNull(registrationEndDateTime)) {
		    	   if(!Objects.isNull(specification))
		    	           specification.and(registrationEndDateTimeLessThanEqualTo(registrationEndDateTime));
		    	   else
		    		   specification = registrationEndDateTimeLessThanEqualTo(registrationEndDateTime);
		      }
		      if(!Objects.isNull(description)) {
		    	  if(!Objects.isNull(specification))
		    	      specification.and(descriptionLike(description));
		    	  else
		    		  specification = descriptionLike(description);
		      }
		   return specification;
	 }
	 
	 
	 public static Specification<AuctionPreparation> fullDetailsById(Long id) {
		 return (root,query,builder) -> {
			 root.fetch("auctionType",JoinType.LEFT);
			 root.fetch("auctionMethod",JoinType.LEFT);
			 root.fetch("propertyType",JoinType.LEFT)
			 .fetch("auctionPreparations", JoinType.LEFT);
			 root.fetch("auctionProcess",JoinType.LEFT);
			 root.fetch("bidSubmissionPlacement",JoinType.LEFT);
			 root.fetch("eventProcessingFeeMode",JoinType.LEFT);
			 root.fetch("emdFeePaymentMode",JoinType.LEFT);
			 root.fetch("emdAppliedFor",JoinType.LEFT);
			 root.fetch("auctionItemTemplate",JoinType.LEFT);
			 root.fetch("auctionItems",JoinType.LEFT);
			return builder.equal(root.get("id"), id);
		 };
	 }
	 
	 
	 public static Specification<AuctionPreparation> findAuctionWithAuctionItemsAndOrganizationItemById(Long id) {
		 return (root,query,builder) -> {
			 root.fetch("auctionItems",JoinType.LEFT).fetch("organizationItem", JoinType.LEFT);
			return builder.equal(root.get("id"), id);
		 };
	 }
	 
	 public static Specification<AuctionPreparation> currentUserAuctions(){
		 LocalDateTime currentDateTime = LocalDateTime.now();
		  return (root, query, builder) -> {
			  SetJoin<AuctionPreparation, BidderAuctionEnrollment> listJoin =  root.joinSet("bidderAuctionEnrollments",JoinType.LEFT);
			  return builder.and(builder.equal(listJoin.get("user"), LoggedInUser.getLoggedInUserDetails().getUser())
					  ,builder.equal(root.get("auctionStatus"), AuctionStatus.SCHEDULED),
					  builder.lessThanOrEqualTo(root.get("auctionStartDateTime"), currentDateTime)
					//  ,builder.greaterThanOrEqualTo(root.get("auctionFinishTime"), currentDateTime)
					  );
		  };
	 }

	 public static Specification<AuctionPreparation> currentUserAuctionsByAuctionIds(List<Long> auctionIds){
		LocalDateTime currentDateTime = LocalDateTime.now();
		 return (root, query, builder) -> {
			 SetJoin<AuctionPreparation, BidderAuctionEnrollment> listJoin =  root.joinSet("bidderAuctionEnrollments",JoinType.LEFT);
			 In<Object> inClause =	builder.in(root.get("id"));
			 return builder.and(builder.equal(listJoin.get("user"), LoggedInUser.getLoggedInUserDetails().getUser())
					 ,builder.equal(root.get("auctionStatus"), AuctionStatus.SCHEDULED),
					 inClause.value(auctionIds),
					 builder.lessThanOrEqualTo(root.get("auctionStartDateTime"), currentDateTime)
					 //,builder.greaterThanOrEqualTo(root.get("auctionFinishTime"), currentDateTime)
					 );
		 };
	}

}
