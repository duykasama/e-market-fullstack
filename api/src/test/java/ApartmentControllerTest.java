import com.example.emarket.controllers.ApartmentController;
import com.example.emarket.models.entities.Apartment;
import com.example.emarket.responses.ResponseObject;
import com.example.emarket.services.ApartmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ApartmentControllerTest {

    private ApartmentController apartmentController;

    @Mock
    private ApartmentService apartmentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        apartmentController = new ApartmentController(apartmentService);
    }

    @Test
    public void testGetAllApartments() {
        List<Apartment> mockApartments = Arrays.asList(
                new Apartment("29","Address 1", "1000", (short) 2),
                new Apartment("21","Address 2", "1200", (short) 3)
        );

        when(apartmentService.getAllApartments()).thenReturn(mockApartments);

        ResponseEntity<ResponseObject> response = apartmentController.getAllApartments();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Get apartments successfully", response.getBody().getMessage());
        assertEquals(mockApartments, (List<Apartment>) response.getBody().getData());

        verify(apartmentService, times(1)).getAllApartments();
    }

    @Test
    public void testGetApartmentsByPage() {
        int pageSize = 10;
        int offset = 1;

        List<Apartment> mockApartments = Arrays.asList(
                new Apartment("25","Address 1", "1000", (short) 2),
                new Apartment("26","Address 2", "1200", (short) 3)
        );

        Page<Apartment> page = new PageImpl<>(mockApartments);

        when(apartmentService.getApartmentsByPage(pageSize, offset)).thenReturn(page);

        ResponseEntity<ResponseObject> response = apartmentController.getApartmentsByPage(pageSize, offset);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(mockApartments, page.getContent());

        verify(apartmentService, times(1)).getApartmentsByPage(pageSize, offset);
    }

    @Test
    public void testGetApartmentsByPageWithInvalidParameters() {
        int pageSize = 0;
        int offset = 0;

        ResponseEntity<ResponseObject> response = apartmentController.getApartmentsByPage(pageSize, offset);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Page size and offset must be greater than 0", response.getBody().getMessage());

        verify(apartmentService, never()).getApartmentsByPage(pageSize, offset);
    }

    @Test
    public void testAddApartment() {
        String address = "Address 1";
        String rentalPrice = "1000";
        Integer numberOfRooms = 2;

        ResponseEntity<ResponseObject> response = apartmentController.addApartment(address, rentalPrice, numberOfRooms);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("New apartment created successfully", response.getBody().getMessage());

        verify(apartmentService, times(1)).addApartment(any(Apartment.class));
    }
}
