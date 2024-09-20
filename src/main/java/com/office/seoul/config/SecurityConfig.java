package com.office.seoul.config;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import com.office.seoul.facility.member.MemberAccessDeniedHandler;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	PasswordEncoder passwordEncoder() {
		log.info("passwordEncoder()");
		
		return new BCryptPasswordEncoder();
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    log.info("securityFilterChain()");
		http
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
        		.requestMatchers(
        				"/css/**",
        				"/img/**",
        				"/js/**",
        				"/",
        				"/member/create_account_form",
        				"/member/create_account_confirm",
        				"/member/member_login_form",
        				"/member/member_login_result",
        				"/member/find_id_form",
        				"/member/find_id_confirm",
        				"/member/find_password_form",
        				"/member/find_password_confirm",
        				"/facility/**").permitAll() // 모든 요청을 허용
                .requestMatchers("/reservation/**").hasAnyRole("USER", "ADMIN", "SUPERADMIN")
                .anyRequest().authenticated()
        )
        .csrf(csrf -> csrf.disable());
	
		
		

	    
	    http
    	.formLogin(login -> login
    			.loginPage("/member/member_login_form")
    			.loginProcessingUrl("/member/member_login_confirm")
				.usernameParameter("a_m_id")
				.passwordParameter("a_m_pw")
				.successHandler((request, response, authentication) -> {
					log.info("[MEMBER LOGIN SUCCESS]");
					
					RequestCache requestCache = new HttpSessionRequestCache();
					SavedRequest savedRequest = requestCache.getRequest(request, response);
					String targetURI = "/";
					
					if (savedRequest != null) {
						targetURI = savedRequest.getRedirectUrl();
						requestCache.removeRequest(request, response);
						
					}
					
					response.sendRedirect(targetURI);
					
				})
				.failureHandler((request, response, exception) -> {
					log.info("[MEMBER LOGIN FAIL]");
					log.info("Exception: {}", exception);
					
					response.sendRedirect("/member/member_login_result");
					
				}));
	    
	    http
	    .logout(logout -> logout
	    		.logoutUrl("/member/member_logout_confirm")
	    		.logoutSuccessHandler((request, response, authentication) -> {
	    			log.info("[MEMBER LOGOUT SUCCESS]");
					
					response.sendRedirect("/");
	    		}));

		http
		.exceptionHandling(exceptionConfig -> exceptionConfig
//				.authenticationEntryPoint(null)
				.accessDeniedHandler(new MemberAccessDeniedHandler()));
		

	    return http.build();
	}

	

	
}
