package com.binluis.parkingsystem;

import com.binluis.parkingsystem.domain.ParkingOrder;
import com.binluis.parkingsystem.domain.ParkingOrderRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TestTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ParkingOrderRepository parkingOrderRepository;

    @Test
    public void should_get_message() throws Exception {
        MvcResult result=this.mvc.perform(get("/test")).andExpect(status().isOk()).andReturn();
        String json=result.getResponse().getContentAsString();

        assertEquals("{test:success}",json);

    }

    @Test
    public void should_get_message_for_lot_order_relationship() throws Exception {
        MvcResult result=this.mvc.perform(get("/test/temp")).andExpect(status().isOk()).andReturn();
        String json=result.getResponse().getContentAsString();

        assertEquals("{test:success}",json);
    }

    @Test
    public void should_get_all_orders() throws Exception {
        ParkingOrder parkingOrder = new ParkingOrder("car2","park","accept");
        parkingOrderRepository.save(parkingOrder);
        parkingOrderRepository.flush();
        MvcResult result=this.mvc.perform(get("/orders")).andExpect(status().isOk()).andReturn();
        String json=result.getResponse().getContentAsString();

        assertEquals("{test:success}",json);
    }

}
