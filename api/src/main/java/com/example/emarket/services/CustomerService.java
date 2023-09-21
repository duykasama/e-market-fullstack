package com.example.emarket.services;

import com.example.emarket.exceptions.CustomerNotFoundException;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.repositories.UserRepository;

import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void loadCustomer(InputStream inputStream) throws IOException {
        List<Customer> customers = new ArrayList<>();

        try (CSVParser csvParser = new CSVParser(
                new InputStreamReader(inputStream), CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : csvParser) {
                String customerId = record.get("customerid");
                String firstName = record.get("FirstName");
                String lastName = record.get("LastName");
                String address = record.get("Address");
                Integer age = Integer.parseInt(record.get("Age"));
                String status = record.get("Status");

                Customer customer = Customer.builder()
                        .id(customerId)
                        .firstName(firstName)
                        .lastName(lastName)
                        .address(address)
                        .age(age.shortValue())
                        .status(status)
                        .build();

                customers.add(customer);
            }
        }

        for (Customer customer : customers) {
            customer.setId(UUID.randomUUID().toString());
        }

        customerRepository.saveAll(customers);
    }


    public List<Customer> saveCustomers(List<Customer> customers) {
        if (customers == null || customers.isEmpty()) {
            throw new IllegalArgumentException("Customer list cannot be null or empty");
        }

        List<Customer> savedCustomers = new ArrayList<>();

        for (Customer customer : customers) {
            // Check if a customer with the same ID already exists in the database
            boolean isDuplicate = customerRepository.existsById(customer.getId());

            if (!isDuplicate) {
                // If not a duplicate, save the customer to the database
                Customer savedCustomer = customerRepository.save(customer);
                savedCustomers.add(savedCustomer);
            }
        }

        return savedCustomers;
    }

}
