package com.binluis.parkingsystem;

import com.binluis.parkingsystem.domain.ParkingLot;
import com.binluis.parkingsystem.domain.ParkingLotRepository;
import com.binluis.parkingsystem.domain.ParkingOrder;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import com.binluis.parkingsystem.models.ParkingLotParkingOrderAssociationRequest;
import com.binluis.parkingsystem.models.ParkingLotResponse;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ParkingLotResourceTest {
    @Autowired
    ParkingLotRepository parkingLotRepository;
    @Autowired
    EntityManager entityManager;
    @Autowired
    MockMvc mvc;
    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    //When GET /parkingLots,
    //Return 200 with parking lots list [{"id": Long, "name":"string", "capacity": integer}]
    @Test
    public void should_get_parking_lots() throws Exception {
        final ParkingLot lot = parkingLotRepository.save(new ParkingLot("lot",10));
        parkingLotRepository.flush();

        final MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get("/parkinglots"))
                .andReturn();

        assertEquals(200, result.getResponse().getStatus());

        final ParkingLotResponse[] parkingLots = getContentAsObject(result, ParkingLotResponse[].class);

        assertEquals(1, parkingLots.length);
        assertEquals("lot", parkingLots[0].getName());
        assertEquals(10,parkingLots[0].getCapacity());
    }

    @Test
    public void should_associate_parking_lot_with_parking_order() throws Exception{
        //Given
        final ParkingLot parkingLot = new ParkingLot("Lot1", 10);
        final ParkingOrder parkingOrder = new ParkingOrder("ABC1", "Parking", "Pending");
        parkingLotRepository.save(parkingLot);
        parkingLotRepository.flush();
        parkingOrderRepository.save(parkingOrder);
        parkingOrderRepository.flush();

        ParkingLotParkingOrderAssociationRequest request = ParkingLotParkingOrderAssociationRequest.create(parkingOrder.getId());

        //When
        mvc.perform(post("/parkinglots/"+ parkingLot.getId()+"/orders")
                .content(asJsonString(request)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        //Then

    }

}
