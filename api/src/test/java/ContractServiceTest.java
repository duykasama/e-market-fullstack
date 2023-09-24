import com.example.emarket.controllers.ContractController;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.repositories.CustomerRepository;
import com.example.emarket.services.ContractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ContractServiceTest {
    @InjectMocks
    private ContractController contractController;

    @InjectMocks
    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ApartmentRepository apartmentRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadNonExistentFiles() throws IOException {
        MultipartFile mockFile = createMockMultipartFile("non_existent_file.csv");

        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        contractService.loadContracts(mockFile);

        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void testLoadExistentFiles() throws IOException {
        MultipartFile mockFile = createMockMultipartFile("students.csv");

        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        contractService.loadContracts(mockFile);

        verify(contractRepository, atLeastOnce()).saveAll(anyIterable());
    }

    @Test
    public void testLoadExistentFilesWithMissedValues() throws IOException {
        MultipartFile mockFile = createMockMultipartFile("students-missing.csv");

        when(apartmentRepository.findById(anyString())).thenReturn(Optional.of(createMockApartment()));
        when(customerRepository.findById(anyString())).thenReturn(Optional.of(createMockCustomer()));
        when(contractRepository.findById(anyString())).thenReturn(Optional.empty());

        contractService.loadContracts(mockFile);

        verify(contractRepository, never()).save(any(Contract.class));
    }

    @Test
    public void testSaveNullModelList() {
        assertThrows(IllegalArgumentException.class, () -> contractService.saveContracts(null));
    }

    @Test
    public void testSaveEmptyModelList() {
        List<Contract> emptyList = new ArrayList<>();

        assertThrows(IllegalArgumentException.class, () -> contractService.saveContracts(emptyList));
    }

    @Test
    public void testSaveNonEmptyModelList() {
        List<Contract> contracts = new ArrayList<>();

        Apartment apartment1 = new Apartment();
        apartment1.setId("apartment1");
        apartment1.setAddress("123 Main St");

        Customer customer1 = new Customer();
        customer1.setId("customer1");
        customer1.setFirstName("John Doe");

        Contract contract1 = new Contract();
        contract1.setId("contract1");
        contract1.setStartDate(LocalDate.of(2023, 9, 20));
        contract1.setEndDate(LocalDate.of(2023, 9, 23));
        contract1.setApartment(apartment1);
        contract1.setCustomer(customer1);

        contracts.add(contract1);

        Apartment apartment2 = new Apartment();
        apartment2.setId("apartment2");
        apartment2.setAddress("456 Elm St");

        Customer customer2 = new Customer();
        customer2.setId("customer2");
        customer2.setFirstName("Jane Smith");

        Contract contract2 = new Contract();
        contract2.setId("contract2");
        contract2.setStartDate(LocalDate.of(2023, 9, 21));
        contract2.setEndDate(LocalDate.of(2023, 9, 24));
        contract2.setApartment(apartment2);
        contract2.setCustomer(customer2);

        contracts.add(contract2);
        when(contractRepository.saveAll(anyIterable())).thenReturn(contracts);

        List<Contract> savedContracts = contractService.saveContracts(contracts);

        assertEquals(2, savedContracts.size());
        assertEquals("contract1", savedContracts.get(0).getId());
        assertEquals("contract2", savedContracts.get(1).getId());
    }

    @Test
    public void testGetAllModelsFromDatabase() {
        List<Contract> contracts = new ArrayList<>();
        contracts.add(createMockContract("contract1"));
        contracts.add(createMockContract("contract2"));

        when(contractRepository.findAll()).thenReturn(contracts);

        List<Contract> result = contractService.getAllContracts();

        assertEquals(2, result.size());
        assertEquals("contract1", result.get(0).getId());
        assertEquals("contract2", result.get(1).getId());
    }

    @Test
    public void testSaveDuplicatedModels() {
        List<Contract> contracts = new ArrayList<>();

        Apartment apartment1 = new Apartment();
        apartment1.setId("apartment1");
        apartment1.setAddress("123 Main St");

        Customer customer1 = new Customer();
        customer1.setId("customer1");
        customer1.setFirstName("John Doe");

        Contract contract1 = new Contract();
        contract1.setId("contract1");
        contract1.setStartDate(LocalDate.of(2023, 9, 20));
        contract1.setEndDate(LocalDate.of(2023, 9, 23));
        contract1.setApartment(apartment1);
        contract1.setCustomer(customer1);

        contracts.add(contract1);

        Apartment apartment2 = new Apartment();
        apartment2.setId("apartment1");
        apartment2.setAddress("123 Main St");

        Customer customer2 = new Customer();
        customer2.setId("customer1");
        customer2.setFirstName("John Doe");

        Contract contract2 = new Contract();
        contract2.setId("contract1");
        contract2.setStartDate(LocalDate.of(2023, 9, 20));
        contract2.setEndDate(LocalDate.of(2023, 9, 23));
        contract2.setApartment(apartment1);
        contract2.setCustomer(customer1);

        contracts.add(contract2);

        when(contractRepository.saveAll(anyIterable())).thenReturn(contracts);

        List<Contract> savedContracts = contractService.saveContracts(contracts);

        assertEquals(2, savedContracts.size());
    }


    @Test
    public void testSaveContractsWithMissingCustomersOrApartments() {
        List<Contract> contracts = new ArrayList<>();
        Contract contract1 = createMockContract("contract1");
        contract1.setCustomer(null);
        contract1.setApartment(null);
        contracts.add(contract1);

        when(contractRepository.saveAll(anyIterable())).thenReturn(contracts);

        List<Contract> savedContracts = contractService.saveContracts(contracts);

    }


    private Contract createMockContract(String id) {
        Contract contract = new Contract();
        contract.setId(id);
        return contract;
    }

    private Apartment createMockApartment() {
        Apartment apartment = new Apartment();
        apartment.setId("apartment1");
        return apartment;
    }

    private Customer createMockCustomer() {
        Customer customer = new Customer();
        customer.setId("customer1");
        return customer;
    }

    private MultipartFile createMockMultipartFile(String fileName) throws IOException {
        return new MultipartFile() {
            @Override
            public String getName() {
                return fileName;
            }

            @Override
            public String getOriginalFilename() {
                return fileName;
            }

            @Override
            public String getContentType() {
                return "text/csv";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                String csvContent = "apartmentId,customerId,StartDate,end_date\n" +
                        "400d7792-d833-49ed-b5bb-ce603cb2b733,dd7da36a-33a7-4315-9fd5-6242bb1eda96,2023-09-20,2023-09-23\n";
                return csvContent.getBytes();
            }

            @Override
            public InputStream getInputStream() throws IOException {
                String csvContent = "apartmentId,customerId,StartDate,end_date\n" +
                        "400d7792-d833-49ed-b5bb-ce603cb2b733,dd7da36a-33a7-4315-9fd5-6242bb1eda96,2023-09-20,2023-09-23\n";
                return new ByteArrayInputStream(csvContent.getBytes());
            }

            @Override
            public void transferTo(java.io.File dest) throws IOException, IllegalStateException {

            }
        };

    }
}