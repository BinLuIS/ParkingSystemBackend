package com.binluis.parkingsystem;

import com.binluis.parkingsystem.domain.ParkingBoy;
import com.binluis.parkingsystem.domain.ParkingBoyRepository;
import com.binluis.parkingsystem.domain.ParkingOrder;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import com.binluis.parkingsystem.models.ParkingBoyParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;

import static com.binluis.parkingsystem.WebTestUtil.asJsonString;
import static com.binluis.parkingsystem.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class ParkingBoyResourceTest {
    @Autowired
    ParkingOrderRepository parkingOrderRepository;
    @Autowired
    ParkingBoyRepository parkingBoyRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    MockMvc mvc;



    //When GET /parkingclerks,
    //Return 200 with a parkingBoys list [{"name": "string","email":"string","phoneNumber":"string","status":"string"}]
    @Test
    public void should_get_parking_boys() throws Exception {
        final ParkingBoy boy = parkingBoyRepository.save(new ParkingBoy("boy1","boy1@email","12345678901","accept"));
        parkingBoyRepository.flush();

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkingclerks"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        final ParkingBoyResponse[] parkingBoys = getContentAsObject(result, ParkingBoyResponse[].class);

        assertEquals(1, parkingBoys.length);
        assertEquals(boy.getId(), parkingBoys[0].getId());
    }

    //Given a parking boy {"name": "string","email":"string","phoneNumber":"string","status":"string"},
    //When POST /parkingclerks, Return 201
    @Test
    public void should_create_parking_boys() throws Exception {
        // Given
        final ParkingBoy boy = new ParkingBoy("boy1","boy1@email","12345678901","accept");

        // When
        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingclerks").content(asJsonString(boy)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Then
        assertEquals(201, result.getResponse().getStatus());

        final ParkingBoy createdBoy = parkingBoyRepository.findAll().get(0);
        entityManager.clear();

        assertEquals("boy1", createdBoy.getName());
        assertEquals("boy1@email",createdBoy.getEmail());
        assertEquals("12345678901",createdBoy.getPhoneNumber());
        assertEquals("accept",createdBoy.getStatus());
    }

    //Given parkingBoy id and ParkingBoyParkingOrderAssociationRequest {"parkingOrderId" : Long},
    //When POST /parkingclerks/{id}/parkingorders,
    //Then change parkingOrder status and return 201
    @Test
    public void should_parkingOrder_add_to_parkingBoy() throws Exception {
        ParkingBoy parkingBoy=new ParkingBoy("boy1","boy1@email","12345678901","available");
        ParkingOrder parkingOrder=new ParkingOrder("car1","park","pending");
        parkingBoyRepository.save(parkingBoy);
        parkingBoyRepository.flush();
        parkingOrderRepository.save(parkingOrder);
        parkingOrderRepository.flush();
        ParkingBoyParkingOrderAssociationRequest request=new ParkingBoyParkingOrderAssociationRequest(parkingOrder.getId());

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingclerks/"+parkingBoy.getId()+"/parkingorders").content(asJsonString(request)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        entityManager.clear();
        final ParkingOrder boyAddedParkingOrder = parkingOrderRepository.findAll().get(0);

        assertEquals(201,result.getResponse().getStatus());
        assertEquals(parkingOrder.getId(),parkingOrderRepository.findById(boyAddedParkingOrder.getId()).get().getId());



    }
}
