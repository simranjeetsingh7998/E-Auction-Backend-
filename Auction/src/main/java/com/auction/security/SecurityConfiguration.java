package com.auction.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	private CustomAuthenicationEntryPoint jwtAuthenticationEntryPoint;
	
	@Autowired
	private JWTAuthorizationFilter jwtAuthorizationFilter;

	public SecurityConfiguration(CustomAuthenicationEntryPoint jwtAuthenticationEntryPoint) {
		this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
		// this.jwtRequestFilter = jwtRequestFilter;
	}

	@Autowired
	private UserDetailsService jwtUserDetailsService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().configurationSource(cors()).and().csrf().disable().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and().authorizeRequests().antMatchers("/login","/user/bidder","/user/admin",
						"/user/send/email/otp/**","/user/verify/email/otp",
						"/user/send/mobile/otp/**","/user/verify/mobile/otp",
						"/upload/document/user/**",
						"/bidder/**","/images/**","/ws").permitAll().and()
				.authorizeRequests().anyRequest().authenticated().and()
				.addFilterBefore(this.jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class).sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Bean 
	public CorsConfigurationSource cors() {
		 CorsConfiguration configuration = new CorsConfiguration();
		 configuration.setAllowedHeaders(List.of("*"));
		 configuration.setAllowedMethods(List.of("GET","POST","OPTION","DELETE","PUT","PATCH"));
		 configuration.setAllowedOrigins(List.of("*"));
		 UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		 source.registerCorsConfiguration("/**", configuration);
		 return source;
	}

//	@Bean
//	public JWTAuthorizationFilter perRequestFilter() {
//		return new JWTAuthorizationFilter();
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(jwtUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	private final String[] authWishList = { "/*.html", "/images/**", "/favicon.ico", "/**.png", "/**/*.html",
			"/**/*.jsp", "/**/*.css", "/**/*.js", "/**/*.png", "/**/*.jpg", "/*.jpg", "/*.png", "/**/*.png",
			"/**/*.jpeg", "/**/*.JPG", "/**/*.pdf", "/**/*.mp4", "/**/*.3gp", "/**/*.wmv", "/**/*.flv", "/**/*.avi",
			"/**/*.mov", "/swagger-ui/index.html", "/swagger-resources/**", "/swagger-ui.html", "/v2/api-docs",
			"/v3/api-docs", "/webjars/**", "/v3/api-docs/swagger-config" };

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(authWishList);
	}

}
