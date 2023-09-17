package com.example.emarket.services;

import com.example.emarket.models.entities.Apartment;
import com.example.emarket.repositories.ApartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApartmentService {

    private final ApartmentRepository apartmentRepository;
    public ApartmentService(ApartmentRepository apartmentRepository) {
        this.apartmentRepository = apartmentRepository;
    }

    public List<Apartment> getAllApartments() {
        return apartmentRepository.findAll();
    }

    public void addApartment(Apartment apartment) {
        apartmentRepository.save(apartment);
    }

    public Apartment getApartmentById(String apartmentId) {
        return apartmentRepository.findById(apartmentId).orElse(null);
    }

    public Page<Apartment> getApartmentsByPage(Integer pageSize, Integer offset) {
        return apartmentRepository.findAll(PageRequest.of(offset-1, pageSize));
    }
}
