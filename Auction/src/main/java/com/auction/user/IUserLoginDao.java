package com.auction.user;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserLoginDao extends JpaRepository<UserLogin, Long>{
	
	@Query("from UserLogin where userId=?1")
	Optional<UserLogin> findByUserId(Long userId, PageRequest pageRequest);

}
