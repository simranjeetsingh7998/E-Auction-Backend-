package com.auction.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserVerificationDao extends JpaRepository<UserVerification, Long> {
	
	Optional<UserVerification> findByPhoneEmailAndOtpAndIsVerifiedFalse(String phoneEmail, String otp);
	
	Optional<UserVerification> findByPhoneEmailAndIsVerifiedTrue(String phoneEmail);

	@Query(value="select * from user_verification uv where uv.phone_email =?1",nativeQuery=true)
	UserVerification getOtpByMobileNumber(String mobileNumber);
}
