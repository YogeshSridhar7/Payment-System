/*
package com.macd.ps.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macd.ps.model.OrderDto;
import com.macd.ps.model.PaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:payment-test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
class PaymentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addPaymentAndGetPaymentUseRealApplicationStack() throws Exception {
        PaymentDto requestPayment = PaymentDto.builder()
                .orderNumber(42)
                .totalAmount(19.99)
                .build();
        OrderDto order = OrderDto.builder().paymentDto(requestPayment).build();

        String response = mockMvc.perform(post("/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.orderNumber").value(42))
                .andExpect(jsonPath("$.totalAmount").value(19.99))
                .andExpect(jsonPath("$.paymentDate").isNotEmpty())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode createdPayment = objectMapper.readTree(response);
        long paymentId = createdPayment.get("id").asLong();

        mockMvc.perform(get("/payment/get/{id}", paymentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paymentId))
                .andExpect(jsonPath("$.orderNumber").value(42))
                .andExpect(jsonPath("$.totalAmount").value(19.99));
    }
}*/
