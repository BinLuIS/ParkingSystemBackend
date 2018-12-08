package com.binluis.parkingsystem;

import com.binluis.parkingsystem.domain.ParkingLot;
import com.binluis.parkingsystem.domain.ParkingLotRepository;
import com.binluis.parkingsystem.domain.ParkingOrder;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.binluis.parkingsystem.WebTestUtil.getContentAsObject;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class OrderResourceTest {
    @Autowired
    ParkingOrderRepository parkingOrderRepository;

    @Autowired
    MockMvc mvc;

    @Test
    public void should_get_all_orders() throws Exception {
        ParkingOrder parkingOrder = new ParkingOrder("car2","park","accept");
        parkingOrderRepository.save(parkingOrder);
        parkingOrderRepository.flush();

        MvcResult result=this.mvc.perform(get("/orders")).andExpect(status().isOk()).andReturn();
        final ParkingOrder[] parkingOrders = getContentAsObject(result, ParkingOrder[].class);


        assertEquals(1,parkingOrders.length);
        assertEquals("car2",parkingOrders[0].getCarNumber());
        assertEquals("park",parkingOrders[0].getRequestType());
        assertEquals("accept",parkingOrders[0].getStatus());
    }

}
