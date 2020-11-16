package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	private UserDetailsService UserDetailsService;
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/", "/user/registration", "/tweet/{\\d}", "/css/**").permitAll()
	            ///tweet/{\\d}の{\\d}はアクセスの制限なしのパスを指定するときに使うもの例えば/tweet/1とかとか
				//(復習)antMachersの引数に設定したパスでpermitAllをチェーンで繋げることで誰でもそのパスにアクセスで切るようにした                                                 
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.loginPage("/user/login")
				.defaultSuccessUrl("/", true)
				.usernameParameter("email")
				.permitAll()
				.and()
			.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
				.logoutSuccessUrl("/");
	}
	
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(UserDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}
