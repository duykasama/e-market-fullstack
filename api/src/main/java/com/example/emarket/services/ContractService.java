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

    @Transactional
    public void loadContracts(MultipartFile file) {
        List<String> failureContracts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("contract")) {
                    String[] data = line.split(","); // assuming CSV format
                    if (data.length < 5) { // check for missing data
                        failureContracts.add(file.getOriginalFilename());
                        continue;
                    }

                    String contractId = data[0];
                    Optional<Contract> existingContract = contractRepository.findById(contractId);
                    if (existingContract.isPresent()) { // check for duplicate data
                        continue;
                    }
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    Contract contract = new Contract();
                    // You need to fetch the existing Customer and Apartment entities from the database
                    Customer customer = customerRepository.findById(data[2]).orElse(null);
                    Apartment apartment = apartmentRepository.findById(data[1]).orElse(null);
                    contract.setCustomer(customer);
                    contract.setApartment(apartment);
                    contract.setEndDate(LocalDate.parse(data[3], formatter));
                    contract.setStartDate(LocalDate.parse(data[4], formatter));
                    contractRepository.save(contract);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}