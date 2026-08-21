package com.macd.ps.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.macd.ps.exception.GlobalExceptionHandler;
import com.macd.ps.model.OrderDto;
import com.macd.ps.model.PaymentDto;
import com.macd.ps.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PaymentControllerTest {

    private final PaymentService paymentService = mock(PaymentService.class);
    private final MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new PaymentController(paymentService))
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void addPaymentUnwrapsOrderAndReturnsCreatedPayment() throws Exception {
        PaymentDto requestPayment = PaymentDto.builder().orderNumber(42).totalAmount(new BigDecimal("19.99")).build();
        PaymentDto responsePayment = requestPayment.toBuilder().id(7).build();
        when(paymentService.createPayment(requestPayment)).thenReturn(responsePayment);

        mockMvc.perform(post("/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderDto.builder().paymentDto(requestPayment).build())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responsePayment)));

        verify(paymentService).createPayment(requestPayment);
    }

    @Test
    void getPaymentByIdReturnsPayment() throws Exception {
        PaymentDto payment = PaymentDto.builder().id(7).orderNumber(42).totalAmount(new BigDecimal("19.99")).build();
        when(paymentService.getPaymentById(7L)).thenReturn(payment);

        mockMvc.perform(get("/payment/get/7"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(payment)));

        verify(paymentService).getPaymentById(7L);
    }

    @Test
    void addPaymentWhenServiceThrowsException_returnsControllerAdviceResponse() throws Exception {
        PaymentDto requestPayment = PaymentDto.builder().orderNumber(42).totalAmount(new BigDecimal("19.99")).build();

        when(paymentService.createPayment(requestPayment))
                .thenThrow(new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Payment Service is down. Please try again later."));

        mockMvc.perform(post("/payment/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(OrderDto.builder().paymentDto(requestPayment).build())))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Payment Service is down. Please try again later."));
    }
}