package com.auction;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.auction.bid.submission.placement.BidSubmissionPlacement;
import com.auction.bid.submission.placement.IBidSubmissionPlacementDao;
import com.auction.bidder.BidderType;
import com.auction.bidder.IBidderTypeDao;
import com.auction.bidder.category.BidderCategory;
import com.auction.bidder.category.IBidderCategoryDao;
import com.auction.category.AuctionCategory;
import com.auction.category.IAuctionCategoryDao;
import com.auction.emd.applied.EMDAppliedFor;
import com.auction.emd.applied.IEMDAppliedForDao;
import com.auction.emd.fee.payment.mode.EMDFeePaymentMode;
import com.auction.emd.fee.payment.mode.IEMDFeePaymentModeDao;
import com.auction.event.processing.fee.mode.EventProcessingFeeMode;
import com.auction.event.processing.fee.mode.IEventProcessingFeeModeDao;
import com.auction.mail.IMailSender;
import com.auction.mail.MailSender;
import com.auction.method.AuctionMethod;
import com.auction.method.IAuctionMethodDao;
import com.auction.process.AuctionProcess;
import com.auction.process.IAuctionProcessDao;
import com.auction.type.AuctionType;
import com.auction.type.IAuctionTypeDao;
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
@EnableScheduling
public class AuctionApplication {
	
	@Autowired
	private IRoleDao roleDao;
	
	@Autowired
	private IBidderTypeDao bidderTypeDao;
	
	@Autowired
	private IBidderCategoryDao bidderCategoryDao;
	
	@Autowired
	private IAuctionTypeDao auctionTypeDao;
	
	@Autowired
	private IAuctionCategoryDao auctionCategoryDao;
	
	@Autowired
	private IAuctionMethodDao auctionMethodDao;
	
	@Autowired
	private IAuctionProcessDao auctionProcessDao;
	
	@Autowired
	private IBidSubmissionPlacementDao bidSubmissionPlacementDao;
	
	@Autowired
	private IEventProcessingFeeModeDao eventProcessingFeeModeDao;
	
	@Autowired
	private IEMDAppliedForDao appliedForDao;
	
	@Autowired
	private IEMDFeePaymentModeDao feePaymentModeDao;
	
	private static final  String URL = "/v3/api-docs";
	
	@Value(value = "${files.resources}")
	private String filePath;

	public static void main(String[] args) {
		SpringApplication.run(AuctionApplication.class, args);
	}
	
	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
			  //  registry.addResourceHandler("/images/**").addResourceLocations("/images/");
			    exposeDirectory("images", registry);
			}
		};
	}
	
    private void exposeDirectory(String dirName, ResourceHandlerRegistry registry) {
        Path uploadDir = Paths.get(dirName);
        String uploadPath = filePath;
        System.out.println(uploadPath); 
        if (dirName.startsWith("../")) dirName = dirName.replace("../", "");
         
        registry.addResourceHandler("/" + dirName + "/**").addResourceLocations("file:/"+ uploadPath + "/");
    }
	
	
	  @Bean public IMailSender mailSender() { return new MailSender(); }
	  
	    @Bean
	    public JavaMailSender javaMailSender() {
		    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		    mailSender.setHost("email-smtp.us-east-1.amazonaws.com");
		    mailSender.setUsername("AKIA6E2TNHXHG4ZGHYFS");
		    mailSender.setPassword("BDJ1Lm297o2LOSz48n+bTIZKCQhf69aihuGWc+69FI0u");
		    mailSender.setPort(465);
		    mailSender.setProtocol("smtps");

		    Properties properties = new Properties();
		    properties.setProperty("mail.smtps.auth", "true");
		    properties.setProperty("mail.smtp.ssl.enable", "true");
		    properties.setProperty("mail.debug", "true");
		    mailSender.setJavaMailProperties(properties);
		    return mailSender;
	    }
	 

	@Bean
	public OpenAPI ecommerceBackend() {
		return new OpenAPI()
				.info(new Info().title("Auction API").description("Auction api expose by Swagger").version("v0.0.1")
						.license(new License().name("Apache 2.0").url(URL)))
				.externalDocs(new ExternalDocumentation().description("")
						.url(URL))
				.components(new Components().addSecuritySchemes("bearer-key",
						new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
								.in(In.HEADER).name("Authorization")))
				.addSecurityItem(new SecurityRequirement().addList("bearer-key"));
	}

	@Bean
	CommandLineRunner commandLine() {
		return args -> {
			if (this.roleDao.count() == 0)
				addRole();
			if(this.bidderTypeDao.count() == 0)
				addBidderType();
			if(this.bidderCategoryDao.count() == 0)
				addBidderCategory();
			if(this.auctionTypeDao.count() == 0)
				 addAuctionType();
			if(this.auctionCategoryDao.count() == 0)
				 addAuctionCategory();
			if(this.auctionMethodDao.count() == 0)
				 addAuctionMethod();
			if(this.auctionProcessDao.count() == 0)
				 addAuctionProcess();
			if(this.bidSubmissionPlacementDao.count() == 0)
				 addBidSubmissionPlacement();
			if(this.eventProcessingFeeModeDao.count() == 0)
				 this.addEventProcessingFeeMode();
			if(this.feePaymentModeDao.count() == 0)
				 this.addEmdFeePaymentMode();
			if(this.appliedForDao.count() == 0)
				this.addEmdAppliedFor();
		};	
	}

	private void addRole() {
		String roles [] = { "Admin", "Bidder" };
		for (String role : roles) {
			Role roleObj = new Role();
			roleObj.setRole(role);
			roleObj.setEnabled(true);
			this.roleDao.save(roleObj);
		}
	}
	
	private void addBidderType() {
		String bidderTypes [] = { "Indivisiual", "Company" };
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
			      bidderType.getBidderCategories().add(bidderCategory);
			      this.bidderTypeDao.save(bidderType);
		  });
	}
	
	private void addAuctionType() {
		String auctionTypes [] = { "Open", "Limited" };
		for (String at : auctionTypes) {
			AuctionType auctionType = new AuctionType();
			auctionType.setId(null);
			auctionType.setAType(at);
			auctionType.setActive(true);
		    this.auctionTypeDao.save(auctionType);	
		}
	}
	
	private void addAuctionCategory() {
		String auctionCategories [] = { "Residental", "Commercial", "Industrail" };
		for (String ac : auctionCategories) {
			AuctionCategory auctionCategory = new AuctionCategory();
			auctionCategory.setActive(true);
			auctionCategory.setCategory(ac);
			this.auctionCategoryDao.save(auctionCategory);
		}
	}
	
	private void addAuctionMethod() {
		String auctionMethods [] = { "Normal", "Roundwise","H1 Bidding Rule" };
		for (String am: auctionMethods) {
			AuctionMethod auctionMethod = new AuctionMethod();
			auctionMethod.setActive(true);
			auctionMethod.setMethod(am);
			this.auctionMethodDao.save(auctionMethod);
		}
	}
	
	private void addAuctionProcess() {
		String auctionProcesses [] = { "Forward Auction", "Backward Auction"};
		for (String ap : auctionProcesses) {
			AuctionProcess auctionProcess = new AuctionProcess();
			auctionProcess.setActive(true);
			auctionProcess.setProcess(ap);
			this.auctionProcessDao.save(auctionProcess);
		}
	}
	
	private void addBidSubmissionPlacement() {
		String bidSubmissionPlacements [] = { "Bid factore", "Bid Price" };
		for (String bsp : bidSubmissionPlacements) {
			BidSubmissionPlacement bidSubmissionPlacement = new BidSubmissionPlacement();
			bidSubmissionPlacement.setActive(true);
			bidSubmissionPlacement.setBidSubmissionPlacementType(bsp);
			this.bidSubmissionPlacementDao.save(bidSubmissionPlacement);
		}
	}
	
	private void addEventProcessingFeeMode() {
		String eventProcessingFeeModes [] = { "online", "offline", "Not Required"};
		for (String epfm : eventProcessingFeeModes) {
			EventProcessingFeeMode eventProcessingFeeMode = new EventProcessingFeeMode();
			eventProcessingFeeMode.setActive(true);
			eventProcessingFeeMode.setEpfMode(epfm);
		  	this.eventProcessingFeeModeDao.save(eventProcessingFeeMode);
		}
		
	}
	
	private void addEmdFeePaymentMode() {
		String emdFeePaymentModes [] = {"online", "offline", "Not Required"};
		for (String efpm : emdFeePaymentModes) {
			EMDFeePaymentMode emdFeePaymentMode = new EMDFeePaymentMode();
			emdFeePaymentMode.setActive(true);
			emdFeePaymentMode.setEmdfpMode(efpm);
			this.feePaymentModeDao.save(emdFeePaymentMode);
		}
	}
	
	
	private void addEmdAppliedFor() {
		String emdAppliedFor [] = {"Auction and item wise", "Item wise"};
		for (String eaf : emdAppliedFor) {
			EMDAppliedFor appliedFor = new EMDAppliedFor();
			appliedFor.setActive(true);
			appliedFor.setEmdFor(eaf);
			this.appliedForDao.save(appliedFor);
		}
	}

}
