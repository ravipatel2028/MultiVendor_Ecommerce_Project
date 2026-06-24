package com.ravi.entities;


import com.ravi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDetails {

    private String paymentId;
    private String razorPayPaymentLinkId;
    private String razorPayPaymentLinkReferenceId;
    private String razorPayPaymentLinkStatus;
    private String razorPayPaymentId;
    private PaymentStatus paymentStatus=PaymentStatus.PENDING;
}
