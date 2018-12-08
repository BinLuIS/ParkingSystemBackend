package com.binluis.parkingsystem;

import com.binluis.parkingsystem.domain.ParkingLot;
import com.binluis.parkingsystem.domain.ParkingLotRepository;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import com.binluis.parkingsystem.models.ParkingLotResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.EntityManager;

import static com.binluis.parkingsystem.WebTestUtil.getContentAsObject;
import static org.junit.Assert.assertEquals;

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

}
