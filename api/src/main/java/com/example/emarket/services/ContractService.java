package com.example.emarket.services;

import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ContractRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.DateFormatter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final CustomerService customerService;
    private final ApartmentService apartmentService;

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public void addNewContract(Contract contract) {
        contractRepository.save(contract);
    }

    public Page<Contract> getContractsByPage(Integer pageSize, Integer offset) {
        return contractRepository.findAll(PageRequest.of(offset-1, pageSize));
    }

    public final String saveByFile (MultipartFile file){
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        try (InputStream is = file.getInputStream();
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4) {
//                    String id = data[0].trim();
                    String customer_id = data[0].trim();
                    String apartment_id = data[1].trim();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate startDate = LocalDate.parse(data[2].trim(), dateFormatter);
                    LocalDate endDate = LocalDate.parse(data[3].trim(), dateFormatter);
                    Customer customer = customerService.getCustomerById(customer_id);
                    Apartment apartment = apartmentService.getApartmentById(apartment_id);

                    Contract contract = Contract.builder()
//                            .id(id)
                            .customer(customer)
                            .apartment(apartment)
                            .startDate(startDate)
                            .endDate(endDate)
                            .build();

                    contractRepository.save(contract);
                }
            }
            return "Data uploaded successfully.";
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
