package com.example.easymechanic.data.model

import com.google.gson.annotations.SerializedName

data class Payment(
    @SerializedName("id") val id: Int,
    @SerializedName("request_id") val requestId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("status") val status: String,
    @SerializedName("payment_method") val paymentMethod: String?,
    @SerializedName("transaction_id") val transactionId: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("razorpay_order_id") val razorpayOrderId: String?,
    @SerializedName("razorpay_payment_id") val razorpayPaymentId: String?
)

data class PaymentsResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val payments: List<Payment>?
)

data class ProcessPaymentRequest(
    @SerializedName("request_id") val requestId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("payment_method") val paymentMethod: String
)

