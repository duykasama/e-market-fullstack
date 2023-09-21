package com.example.emarket.services;

import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService {

    private final ContractRepository contractRepository;
    private final CustomerService customerService;
    private final ApartmentService apartmentService;
    private final ApartmentRepository apartmentRepository;
    private final CustomerRepository customerRepository;

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
                    String customer_id = data[0].trim();
                    String apartment_id = data[1].trim();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate startDate = LocalDate.parse(data[2].trim(), dateFormatter);
                    LocalDate endDate = LocalDate.parse(data[3].trim(), dateFormatter);
                    Customer customer = customerService.getCustomerById(customer_id);
                    Apartment apartment = apartmentService.getApartmentById(apartment_id);

                    Contract contract = Contract.builder()
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

    public int loadContracts(MultipartFile file) {
        List<String> failureContracts = new ArrayList<>();
        List<Contract> contractsToSave = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // assuming CSV format
                if (data[2].trim().equalsIgnoreCase("StartDate")) {
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Contract contract = new Contract();
                // You need to fetch the existing Customer and Apartment entities from the database
                Customer customer = customerRepository.findById(data[0]).orElse(null);
                Apartment apartment = apartmentRepository.findById(data[1]).orElse(null);
                contract.setCustomer(customer);
                contract.setApartment(apartment);
                contract.setStartDate(LocalDate.parse(data[2], formatter));
                contract.setEndDate(LocalDate.parse(data[3], formatter));
                contractsToSave.add(contract);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (!contractsToSave.isEmpty()) {
            contractRepository.saveAll(contractsToSave);
            return contractsToSave.size();
        }
        return 0;
    }

    public List<Contract> saveContracts(List<Contract> contracts) {
        if (contracts == null || contracts.isEmpty()) {
            throw new IllegalArgumentException("Contracts list cannot be null or empty");
        }

        List<Contract> validContracts = new ArrayList<>();
        for (Contract contract : contracts) {
            if (isValidContract(contract)) {
                validContracts.add(contract);
            }
        }

        if (!validContracts.isEmpty()) {
            contractRepository.saveAll(validContracts);
        }

        return validContracts;
    }

    private boolean isValidContract(Contract contract) {
        if (contract == null) {
            return false; // Contract cannot be null
        }

        if (contract.getStartDate() == null || contract.getEndDate() == null) {
            return false; // Start and end date are required fields
        }

        if (contract.getApartment() == null || contract.getCustomer() == null) {
            return false; // Apartment and customer must be specified
        }

        return true; // If all validation checks pass
    }
}
