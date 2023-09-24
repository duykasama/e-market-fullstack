package com.example.emarket.services;

import com.example.emarket.exceptions.BadRequestException;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.repositories.ApartmentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        if (pageSize <= 0 || offset <= 0) {
            throw new BadRequestException("Page size and offset must be greater than 0");
        }
        return apartmentRepository.findAll(PageRequest.of(offset - 1, pageSize));
    }


    public int saveByFile (MultipartFile file){
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        int savedApartmentCount = 0;

        try (
                InputStream is = file.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if(data[0].trim().equalsIgnoreCase("address")){
                    continue;
                }

                if (data.length == 3) {
                    String address = data[0].trim();
                    String rentalPrice = data[1].trim();
                    int numberOfRoom = Integer.parseInt(data[2].trim());

                    Apartment apartment = Apartment.builder()
                            .address(address)
                            .rentalPrice(rentalPrice)
                            .numberOfRooms((short)numberOfRoom)
                            .build();
                    apartmentRepository.save(apartment);
                    savedApartmentCount++;
                }
            }
            return savedApartmentCount;
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }
}
