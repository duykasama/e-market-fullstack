import com.example.emarket.models.entities.Apartment;
import com.example.emarket.models.entities.Contract;
import com.example.emarket.models.entities.Customer;
import com.example.emarket.repositories.ContractRepository;
import com.example.emarket.services.ApartmentService;
import com.example.emarket.services.ContractService;
import com.example.emarket.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ContractServiceTest {

    @InjectMocks
    private ContractService contractService;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private CustomerService customerService;

    @Mock
    private ApartmentService apartmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllContracts() {
        // Arrange
        List<Contract> contracts = new ArrayList<>();
        contracts.add(new Contract());
        when(contractRepository.findAll()).thenReturn(contracts);

        // Act
        List<Contract> result = contractService.getAllContracts();

        // Assert
        assertEquals(1, result.size());
        assertNotNull(result.get(0));
    }

    @Test
    void testAddNewContract() {
        // Arrange
        Contract contract = new Contract();

        // Act
        contractService.addNewContract(contract);

        // Assert
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void testGetContractsByPage() {
        // Arrange
        int pageSize = 10;
        int offset = 1;
        PageRequest pageRequest = PageRequest.of(offset - 1, pageSize);
        when(contractRepository.findAll(pageRequest)).thenReturn(mock(Page.class));

        // Act
        Page<Contract> result = contractService.getContractsByPage(pageSize, offset);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveByFile() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "contracts.csv",
                "contract-data.csv",
                "text/csv",
                "1,2,01/01/2023,01/02/2023\n3,4,01/03/2023,01/04/2023".getBytes()
        );
        Customer customer1 = new Customer();
        Apartment apartment1 = new Apartment();
        Customer customer3 = new Customer();
        Apartment apartment4 = new Apartment();
        when(customerService.getCustomerById("1")).thenReturn(customer1);
        when(apartmentService.getApartmentById("2")).thenReturn(apartment1);
        when(customerService.getCustomerById("3")).thenReturn(customer3);
        when(apartmentService.getApartmentById("4")).thenReturn(apartment4);

        // Act
        String message = contractService.saveByFile(file);

        // Assert
        assertEquals("Data uploaded successfully.", message);
        verify(contractRepository, times(2)).save(any(Contract.class));
    }
}
