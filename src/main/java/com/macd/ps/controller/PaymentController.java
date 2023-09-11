package com.macd.ps.controller;

import com.macd.ps.model.PaymentDto;
import com.macd.ps.service.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping(value = "/add")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto addPayment(@RequestBody PaymentDto payment, final HttpServletResponse response){
        return paymentService.createPayment(payment);
    }

    @GetMapping(value = "/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentDto getPaymentById(@PathVariable Long id,final HttpServletResponse response) {
        return paymentService.getPaymentById(id);
    }
}
