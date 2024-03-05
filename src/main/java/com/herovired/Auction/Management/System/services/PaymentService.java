package com.herovired.Auction.Management.System.services;

import com.herovired.Auction.Management.System.models.TransactionDetails;


import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private static final String KEY = "rzp_test_4WKqiNTP0DCoOR";
    private static final String KEY_SECRET = "R4DoJ2oBpEhrHkfBCAfAPfwQ";
    private static final String CURRENCY = "INR";


    public TransactionDetails createTransaction(Long amount) throws RazorpayException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("amount", (amount*100));
        jsonObject.put("currency", CURRENCY);

        RazorpayClient razorpayClient = new RazorpayClient(KEY, KEY_SECRET);
        Order order = razorpayClient.orders.create(jsonObject);
        return prepareTransactionDetails(order);
    }

    private TransactionDetails prepareTransactionDetails(Order order) {
        String orderId = order.get("id");
        String currency = order.get("currency");
        Integer amount = (int) order.get("amount");
        return TransactionDetails.builder()
                .orderId(orderId)
                .currency(currency)
                .amount(amount/100)
                .key(KEY)
                .build();
    }
}
