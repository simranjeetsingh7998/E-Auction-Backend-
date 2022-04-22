package com.auction.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserDao extends JpaRepository<User, Long> {

	@Query("from User u join fetch u.role join fetch u.organization where u.email = ?1")
	Optional<User> findByEmail(String email);
	
	@Query("from User u join fetch u.addresses join fetch u.bidderCategory join fetch u.bidderCategory.bidderType join fetch u.role where u.id =?1 ")
	Optional<User> findFullUserDetailById(Long id);

	User findUserByEmail(String userEmail);
	
	boolean existsByEmail(String email);
	
	boolean existsByMobileNumber(String mobileNumber);
	
	boolean existsByAadharNumber(String aadharNumber);
	
	boolean existsByPanCardNumber(String panCardNumber);

}
