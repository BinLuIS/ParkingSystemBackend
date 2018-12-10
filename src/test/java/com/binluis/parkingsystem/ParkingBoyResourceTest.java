package com.binluis.parkingsystem;

import com.binluis.parkingsystem.domain.*;
import com.binluis.parkingsystem.models.ParkingBoyParkingLotAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingBoyResponse;
import com.binluis.parkingsystem.models.ParkingOrderResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;

import static com.binluis.parkingsystem.WebTestUtil.asJsonString;
import static com.binluis.parkingsystem.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


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
    ParkingLotRepository parkingLotRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    MockMvc mvc;



    //When GET /parkingclerks,
    //Return 200 with a parkingBoys list [{"name": "string","email":"string","phoneNumber":"string","status":"string"}]
    @Test
    @WithMockUser
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
    @WithMockUser
    public void should_create_parking_boys() throws Exception {
        // Given
        final ParkingBoy boy = new ParkingBoy("boy1","boy1@email","12345678901","accepted");

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
        assertEquals("accepted",createdBoy.getStatus());
    }

    //Given parkingBoy id and ParkingBoyParkingOrderAssociationRequest {"parkingOrderId" : Long},
    //When POST /parkingclerks/{id}/parkingorders,
    //Then change parkingOrder status and return 201
    @Test
    @WithMockUser
    public void should_parkingOrder_add_to_parkingBoy() throws Exception {
        ParkingBoy parkingBoy=new ParkingBoy("boy1","boy1@email","12345678901","available");
        ParkingOrder parkingOrder=new ParkingOrder("car1","parking","pending parking");
        parkingBoyRepository.save(parkingBoy);
        parkingBoyRepository.flush();
        parkingOrderRepository.save(parkingOrder);
        parkingOrderRepository.flush();
        ParkingBoyParkingOrderAssociationRequest request=new ParkingBoyParkingOrderAssociationRequest(parkingOrder.getId());

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post("/parkingclerks/"+parkingBoy.getId()+"/orders").content(asJsonString(request)).contentType(MediaType.APPLICATION_JSON))
                .andReturn();


        entityManager.clear();
        final ParkingOrder boyAddedParkingOrder = parkingOrderRepository.findAll().get(0);

        assertEquals(201,result.getResponse().getStatus());
        assertEquals(parkingOrder.getId(),parkingOrderRepository.findById(boyAddedParkingOrder.getId()).get().getId());
    }

    @Test
    @WithMockUser
    public void should_get_associate_parking_order_of_parking_boy() throws Exception{
        //Given
        final ParkingBoy parkingBoy=new ParkingBoy("boy1","boy1@email","12345678901","available");
        final ParkingOrder parkingOrder = new ParkingOrder("ABC1", "parking", "pending parking");
        parkingOrder.setParkingBoy(parkingBoy);
        parkingBoyRepository.save(parkingBoy);
        parkingLotRepository.flush();
        parkingOrderRepository.save(parkingOrder);
        parkingOrderRepository.flush();

        //When
        final MvcResult result = mvc.perform(get("/parkingclerks/"+ parkingBoy.getId()+"/orders"))
                .andReturn();

        //Then
        assertEquals(200, result.getResponse().getStatus());

        final ParkingOrderResponse[] parkingOrderResponses= getContentAsObject(result, ParkingOrderResponse[].class);

        assertEquals(1, parkingOrderResponses.length);
        assertEquals("ABC1", parkingOrderResponses[0].getCarNumber());
        assertEquals("parking",parkingOrderResponses[0].getRequestType());
        assertEquals("pending parking",parkingOrderResponses[0].getStatus());
    }
}
