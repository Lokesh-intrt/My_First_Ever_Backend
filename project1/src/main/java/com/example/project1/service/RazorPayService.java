package com.example.project1.service;

import com.example.project1.DTOs.PayRequestDTO;
import com.example.project1.DTOs.PayResponseDTO;
import com.razorpay.Order;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayClient;
import org.json.JSONObject;

@Service
@Slf4j
public class RazorPayService {

    @Value("${RAZORPAY_KEY}")
    private String keyId;

    @Value("${RAZORPAY_SECRET}")
    private String secret;

    private final PaymentUpdateService paymentUpdateService;

    public RazorPayService(PaymentUpdateService paymentUpdateService) {
        this.paymentUpdateService = paymentUpdateService;
    }

    public PayResponseDTO createPaymentIntent(PayRequestDTO payRequestDTO) throws RazorpayException, JSONException {
        RazorpayClient razorpayClient = new RazorpayClient(keyId, secret);

        long amountInPaise = Math.round(payRequestDTO.getAmount() * 100);

        JSONObject request = new JSONObject();
        request.put("amount",amountInPaise);
        request.put("currency",payRequestDTO.getCurrencyType());
        request.put("receipt","txn_order_"+payRequestDTO.getOrderId());

        Order razorPayOrderResponse = razorpayClient.orders.create(request);

        return new PayResponseDTO(razorPayOrderResponse.get("amount"),
                razorPayOrderResponse.get("id"),
                razorPayOrderResponse.get("currency"),
                keyId);
    }

    public void webHookSigVerification(String signature, String payLoad) throws RazorpayException {
        if(!Utils.verifyWebhookSignature(payLoad,signature,secret))
        {
            log.warn("unknown entity is breaching security by mimicking razorpay!");
            throw new SecurityException("Unknown Entity is sending this webhook. UNAUTHORIZED!");
        }
    }

    public void paymentAfterMath(String signature, String rawJSONPayLoad) throws RazorpayException, JSONException {

        JSONObject payLoad = new JSONObject(rawJSONPayLoad);
        //event = payment success or failure
        String event = payLoad.getString("event");

        if(event.equalsIgnoreCase("order.paid"))
        {
            JSONObject order = payLoad.getJSONObject("payload")
                    .getJSONObject("order")
                    .getJSONObject("entity");

            String receipt = order.getString("receipt");
            String razorpayId = order.getString("id");
            Long orderId = Long.parseLong(receipt.replace("txn_order_",""));

            paymentUpdateService.updateOrderStatus(orderId);
        }

    }
}
