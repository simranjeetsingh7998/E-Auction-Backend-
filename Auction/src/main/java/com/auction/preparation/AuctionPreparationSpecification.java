package com.auction.preparation;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.data.jpa.domain.Specification;

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

}
