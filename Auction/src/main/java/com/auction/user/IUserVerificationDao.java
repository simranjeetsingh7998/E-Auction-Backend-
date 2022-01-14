package com.auction.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserVerificationDao extends JpaRepository<UserVerification, Long> {
	
	Optional<UserVerification> findByPhoneEmailAndOtpAndIsVerifiedFalse(String phoneEmail, String otp);
	
	Optional<UserVerification> findByPhoneEmailAndIsVerifiedTrue(String phoneEmail);
}
