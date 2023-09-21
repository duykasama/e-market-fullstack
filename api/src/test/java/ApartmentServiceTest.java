import com.example.emarket.models.entities.Apartment;
import com.example.emarket.repositories.ApartmentRepository;
import com.example.emarket.services.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApartmentServiceTest {

    @InjectMocks
    private ApartmentService apartmentService;
    @Mock
    private ApartmentRepository apartmentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllApartments() {
        // Arrange
        List<Apartment> apartments = new ArrayList<>();
        apartments.add(new Apartment("1", "123 Main St", "1000", (short) 2));
        when(apartmentRepository.findAll()).thenReturn(apartments);

        // Act
        List<Apartment> result = apartmentService.getAllApartments();

        // Assert
        assertEquals(1, result.size());
        assertEquals("123 Main St", result.get(0).getAddress());
    }

    @Test
    void testAddApartment() {
        // Arrange
        Apartment apartment = new Apartment("1", "123 Main St", "1000", (short) 2);

        // Act
        apartmentService.addApartment(apartment);

        // Assert
        verify(apartmentRepository, times(1)).save(apartment);
    }

    @Test
    void testGetApartmentById() {
        // Arrange
        Apartment apartment = new Apartment("1", "123 Main St", "1000", (short) 2);
        when(apartmentRepository.findById("1")).thenReturn(java.util.Optional.of(apartment));

        // Act
        Apartment result = apartmentService.getApartmentById("1");

        // Assert
        assertNotNull(result);
        assertEquals("123 Main St", result.getAddress());
    }

    @Test
    void testGetApartmentsByPage() {
        // Arrange
        int pageSize = 10;
        int offset = 1;
        PageRequest pageRequest = PageRequest.of(offset - 1, pageSize);
        when(apartmentRepository.findAll(pageRequest)).thenReturn(mock(Page.class));

        // Act
        Page<Apartment> result = apartmentService.getApartmentsByPage(pageSize, offset);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testSaveByFile() throws IOException {
        // Arrange
        MockMultipartFile file = new MockMultipartFile(
                "apartments.csv",
                "apartment-data.csv",
                "text/csv",
                "123 Main St,1000,2\n456 Elm St,800,1".getBytes()
        );

        // Act
        int savedApartmentsCount = apartmentService.saveByFile(file);

        // Assert
        assertNotEquals(0, savedApartmentsCount);
        verify(apartmentRepository, times(2)).save(any(Apartment.class));
    }
}
