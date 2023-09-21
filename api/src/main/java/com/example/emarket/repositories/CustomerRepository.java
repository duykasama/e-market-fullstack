package com.example.emarket.repositories;

import com.example.emarket.models.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    List<Customer> findByFirstNameContainingOrLastNameContaining(String firstName, String lastName);
}
