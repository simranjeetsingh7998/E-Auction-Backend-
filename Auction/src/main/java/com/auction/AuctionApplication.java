package com.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.auction.bidder.BidderType;
import com.auction.bidder.IBidderTypeDao;
import com.auction.bidder.category.BidderCategory;
import com.auction.bidder.category.IBidderCategoryDao;
import com.auction.mail.IMailSender;
import com.auction.mail.MailSender;
import com.auction.user.IRoleDao;
import com.auction.user.Role;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;

@SpringBootApplication
public class AuctionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IBidderTypeDao bidderTypeDao;
	
	@Autowired
	private IBidderCategoryDao bidderCategoryDao;
	
	
	@Bean
	public IMailSender mailSender() {
		return new MailSender();
	}

	@Bean
	public OpenAPI ecommerceBackend() {
		return new OpenAPI()
				.info(new Info().title("Auction API").description("Auction api expose by Swagger").version("v0.0.1")
						.license(new License().name("Apache 2.0").url("")))
				.externalDocs(new ExternalDocumentation().description("")
						.url(""))
				.components(new Components().addSecuritySchemes("bearer-key",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
								.in(In.HEADER).name("Authorization")))
				.addSecurityItem(new SecurityRequirement().addList("bearer-key"));
	}

	@Bean
	CommandLineRunner commandLine() {
		return (args) -> {
			if (this.roleDao.count() == 0)
				addRole();
			if(this.bidderTypeDao.count() == 0)
				addBidderType();
			if(this.bidderCategoryDao.count() == 0)
				addBidderCategory();
		};
		
	}

	private void addRole() {
		String roles[] = { "Admin", "User" };
		for (String role : roles) {
			Role roleObj = new Role();
			roleObj.setRole(role);
			roleObj.setEnabled(true);
			this.roleDao.save(roleObj);
		}
	}
	
	private void addBidderType() {
		String bidderTypes[] = { "Indivisiual", "Company" };
		for (String bt : bidderTypes) {
			BidderType bidderType = new BidderType();
			bidderType.setBType(bt);
			bidderType.setActive(true);
			this.bidderTypeDao.save(bidderType);
		}
	}
	
	private void addBidderCategory() {
		  this.bidderTypeDao.findAll().forEach(bidderType->{
			      BidderCategory bidderCategory = new BidderCategory();
			      bidderCategory.setActive(true);
			      bidderCategory.setBCategory("General");
			      bidderCategory.setBidderType(bidderType);
			      this.bidderCategoryDao.save(bidderCategory);
		  });
	}

}
