package com.example.emarket.services;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    private final ApartmentRepository apartmentRepository;
    private final CustomerRepository customerRepository;

    public ContractService(ContractRepository contractRepository, ApartmentRepository apartmentRepository, CustomerRepository customerRepository) throws IOException {
        this.contractRepository = contractRepository;
        this.apartmentRepository = apartmentRepository;
        this.customerRepository = customerRepository;
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public void addNewContract(Contract contract) {
        contractRepository.save(contract);
    }

    public Page<Contract> getContractsByPage(Integer pageSize, Integer offset) {
        return contractRepository.findAll(PageRequest.of(offset - 1, pageSize));
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

    public void loadContracts(MultipartFile file) {
        List<String> failureContracts = new ArrayList<>();
        List<Contract> contractsToSave = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(","); // assuming CSV format
                if (data[0].trim().equalsIgnoreCase("start_date")) {
                    continue;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Contract contract = new Contract();
                // You need to fetch the existing Customer and Apartment entities from the database
                Customer customer = customerRepository.findById(data[3]).orElse(null);
                Apartment apartment = apartmentRepository.findById(data[2]).orElse(null);
                contract.setCustomer(customer);
                contract.setApartment(apartment);
                contract.setEndDate(LocalDate.parse(data[1], formatter));
                contract.setStartDate(LocalDate.parse(data[0], formatter));
                contractsToSave.add(contract);
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        if (!contractsToSave.isEmpty()) {
            contractRepository.saveAll(contractsToSave);
        }

    }
}