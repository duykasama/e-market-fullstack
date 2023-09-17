package com.example.emarket.services;

import com.example.emarket.models.entities.Contract;
import com.example.emarket.repositories.ContractRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractService {

    private final ContractRepository contractRepository;
    public ContractService(ContractRepository contractRepository){
        this.contractRepository = contractRepository;
    }

    public List<Contract> getAllContracts() {
        return contractRepository.findAll();
    }

    public void addNewContract(Contract contract) {
        contractRepository.save(contract);
    }

    public Page<Contract> getContractsByPage(Integer pageSize, Integer offset) {
        return contractRepository.findAll(PageRequest.of(offset-1, pageSize));
    }
}
