package com.example.project1.controllers;

import com.example.project1.DTOs.PayRequestDTO;
import com.example.project1.DTOs.PayResponseDTO;
import com.example.project1.service.RazorPayService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import org.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/razorpay")
public class PaymentController {

    private final RazorPayService razorPayService;

    public PaymentController(RazorPayService razorPayService) {
        this.razorPayService = razorPayService;
    }

    @PostMapping("/checkout")
    ResponseEntity<PayResponseDTO> createPaymentIntent(@Valid @RequestBody PayRequestDTO payRequestDTO) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(razorPayService.createPaymentIntent(payRequestDTO));
    }

    @PostMapping("webhook")
    ResponseEntity<Void> paymentAftermath(@RequestHeader("X-RazorPay-Signature") String signature, @RequestBody String rawJSONPayLoad) throws RazorpayException, JSONException {
        razorPayService.webHookSigVerification(signature, rawJSONPayLoad);

        razorPayService.paymentAfterMath(signature, rawJSONPayLoad);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
 }
