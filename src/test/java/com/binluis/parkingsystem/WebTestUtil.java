package com.binluis.parkingsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;

public class WebTestUtil {
    public static <T> T toObject(String jsonContent, Class<T> clazz) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonContent, clazz);
    }

    public static <T> T getContentAsObject(MvcResult result, Class<T> clazz) throws Exception {
        return toObject(result.getResponse().getContentAsString(), clazz);
    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
