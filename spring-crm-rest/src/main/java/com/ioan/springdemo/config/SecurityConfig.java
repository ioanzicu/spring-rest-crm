package com.ioan.springdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	@Bean
	public UserDetailsService userDetailsService() {

		User.UserBuilder users = User.withDefaultPasswordEncoder();
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
		manager.createUser(users.username("john").password("test123").roles("EMPLOYEE").build());
		manager.createUser(users.username("mary").password("test123").roles("MANAGER").build());
		manager.createUser(users.username("susan").password("test123").roles("ADMIN").build());
		return manager;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		// secures all REST endpoints under "/api/customers"

		// EMPLOYEE role can perform following
		// 1. Get a list of all customers. GET /api/customers
		// 2. Get a single customer. GET /api/customers/{customerId}

		// MANAGER role can perform following
		// 1. Add a new customer. POST /api/customers
		// 2. Update an existing customer. PUT /api/customers

		// ADMIN role can perform following
		// 1. Delete a customer. DELETE /api/customers/{customerId}
		http.authorizeRequests().antMatchers(HttpMethod.GET, "/api/customers").hasRole("EMPLOYEE")
				.antMatchers(HttpMethod.GET, "/api/customers/**").hasRole("EMPLOYEE")
				.antMatchers(HttpMethod.POST, "/api/customers").hasAnyRole("MANAGER", "ADMIN")
				.antMatchers(HttpMethod.POST, "/api/customers/**").hasAnyRole("MANAGER", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/api/customers").hasAnyRole("MANAGER", "ADMIN")
				.antMatchers(HttpMethod.PUT, "/api/customers/**").hasAnyRole("MANAGER", "ADMIN")
				.antMatchers(HttpMethod.DELETE, "/api/customers/**").hasRole("ADMIN").antMatchers("/api/customers/**")
				.authenticated().and().httpBasic().and()
				// CSRF generally does not apply for REST APIs. CSRF protection is a request
				// that could be processed by a browser by normal users.
				// If you are only creating a REST service that is used by non-browser clients,
				// you will likely want to disable CSRF protection.
				.csrf().disable()
				// For our application, we would like avoid the use of cookies for sesson
				// tracking. This should force the REST client
				// to enter user name and password for each request.
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

	}

}
