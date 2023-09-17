package com.example.emarket.services;

import com.example.emarket.exceptions.CustomerNotFoundException;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public void save(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public void deleteCustomerById(String id) throws Exception{
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isEmpty()){
            throw new CustomerNotFoundException();
        }
        customerRepository.delete(customer.get());
    }

    public Page<Customer> getCustomersByPage(Integer pageSize, Integer offset) {
        return customerRepository.findAll(PageRequest.of(offset-1, pageSize));
    }
}
