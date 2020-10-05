package com.ioan.springdemo.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ioan.springdemo.entity.Customer;
import com.ioan.springdemo.service.CustomerService;

@RestController
@RequestMapping("/api")
public class CustomerRestController {

	// autowire the CustomerService
	@Autowired
	private CustomerService customerService;

	// add mapping for GET /customers
	@GetMapping("/customers")
	public List<Customer> getCustomers() {

		return customerService.getCustomers();
	}

	// add mapping for GET /customers/{customerId}
	@GetMapping("/customers/{customerId}")
	public Customer getCustomer(@PathVariable int customerId) {

		Customer tempCustomer = customerService.getCustomer(customerId);

		if (tempCustomer == null) {
			throw new CustomerNotFoundException("Customer id not found - " + customerId);
		}

		return tempCustomer;
	}

	// add mapping for POST /customers - add new customer
	@PostMapping("/customers")
	public Customer addCustomer(@RequestBody Customer theCustomer) {

		// pass an id in JSON ... set id to 0 to force a save of new item,
		// no update
		theCustomer.setId(0);

		customerService.saveCustomer(theCustomer);

		return theCustomer;
	}

	// add mapping for PUT /customers - update existing customer
	@PutMapping("/customers")
	public Customer updateCustomer(@RequestBody Customer theCustomer) {

		customerService.saveCustomer(theCustomer);

		return theCustomer;
	}

	// add mapping for DELETE /customers/{customerId}
	@DeleteMapping("/customers/{customerId}")
	public String deleteCustomer(@PathVariable int customerId) {

		Customer tempCustomer = customerService.getCustomer(customerId);

		// throw exception if null
		if (tempCustomer == null) {
			throw new CustomerNotFoundException("Customer id not found - " + customerId);
		}

		customerService.deleteCustomer(customerId);

		return "Delete customer id - " + customerId;
	}
}
