package com.doctoraak.doctoraakdoctor.model

import com.google.gson.annotations.SerializedName

//payment error
data class PaymentErrorDetails( val detail : String)

//Payment auth
data class PaymentAuthRequest( val api_key : String)

data class PaymentAuthResponse(
    val profile: Profile,
    val token: String
)

data class Profile(
    val acq_partner: Any,
    val active: Boolean,
    val address: Any,
    val allow_terminal_order_id: Boolean,
    val awb_banner: Any,
    val bank_deactivation_reason: Any,
    val bank_merchant_status: Int,
    val bank_related: Any,
    val city: Any,
    val commercial_registration: Any,
    val commercial_registration_area: Any,
    val company_emails: List<String>,
    val company_name: String,
    val country: String,
    val created_at: String,
    val custom_export_columns: List<Any>,
    val deactivated_by_bank: Boolean,
    val delivery_status_callback: String,
    val delivery_update_endpoint: Any,
    val distributor_branch_code: Any,
    val distributor_code: Any,
    val email_banner: Any,
    val email_notification: Boolean,
    val failed_attempts: Int,
    val id: Int,
    val identification_number: Any,
    val logo_url: Any,
    val merchant_external_link: Any,
    val national_id: Any,
    val order_retrieval_endpoint: Any,
    val password: Any,
    val permissions: List<Any>,
    val phones: List<String>,
    val postal_code: String,
    val profile_type: String,
    val server_IP: List<Any>,
    val state: String,
    val street: String,
    val super_agent: Any,
    val user: UserPayment,
    val username: Any,
    val wallet_limit_profile: Any
)

data class UserPayment(
    val date_joined: String,
    val email: String,
    val first_name: String,
    val groups: List<Any>,
    val id: Int,
    val is_active: Boolean,
    val is_staff: Boolean,
    val is_superuser: Boolean,
    val last_login: Any,
    val last_name: String,
    val user_permissions: List<Int>,
    val username: String
)

//order registeration
data class OrderRegisterationRequest(
    @SerializedName("amount_cents")
    var amountCents: String = "",
    @SerializedName("auth_token")
    var authToken: String = "",
    @SerializedName("currency")
    var currency: String = "EGP",
    @SerializedName("delivery_needed")
    var deliveryNeeded: String = "",
    @SerializedName("items")
    var items: List<Any> = listOf(),
    @SerializedName("merchant_id")
    var merchantId: String = ""
)

data class OrderRegisterationResponse(
    @SerializedName("amount_cents")
    var amountCents: Int = 0,
    @SerializedName("api_source")
    var apiSource: String = "",
    @SerializedName("collector")
    var collector: Any = Any(),
    @SerializedName("commission_fees")
    var commissionFees: Int = 0,
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("delivery_fees_cents")
    var deliveryFeesCents: Int = 0,
    @SerializedName("delivery_needed")
    var deliveryNeeded: Boolean = false,
    @SerializedName("delivery_status")
    var deliveryStatus: List<Any> = listOf(),
    @SerializedName("delivery_vat_cents")
    var deliveryVatCents: Int = 0,
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("is_cancel")
    var isCancel: Boolean = false,
    @SerializedName("is_canceled")
    var isCanceled: Boolean = false,
    @SerializedName("is_payment_locked")
    var isPaymentLocked: Boolean = false,
    @SerializedName("is_return")
    var isReturn: Boolean = false,
    @SerializedName("is_returned")
    var isReturned: Boolean = false,
    @SerializedName("items")
    var items: List<Any> = listOf(),
    @SerializedName("merchant")
    var merchant: Merchant = Merchant(),
    @SerializedName("merchant_order_id")
    var merchantOrderId: Any = Any(),
    @SerializedName("merchant_staff_tag")
    var merchantStaffTag: Any = Any(),
    @SerializedName("notify_user_with_email")
    var notifyUserWithEmail: Boolean = false,
    @SerializedName("order_url")
    var orderUrl: String = "",
    @SerializedName("paid_amount_cents")
    var paidAmountCents: Int = 0,
    @SerializedName("payment_method")
    var paymentMethod: String = "",
    @SerializedName("pickup_data")
    var pickupData: Any = Any(),
    @SerializedName("shipping_data")
    var shippingData: Any = Any(),
    @SerializedName("shipping_details")
    var shippingDetails: Any = Any(),
    @SerializedName("token")
    var token: String = "",
    @SerializedName("url")
    var url: String = "",
    @SerializedName("wallet_notification")
    var walletNotification: Any = Any()
)

data class Merchant(
    @SerializedName("city")
    var city: String = "",
    @SerializedName("company_emails")
    var companyEmails: List<String> = listOf(),
    @SerializedName("company_name")
    var companyName: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("created_at")
    var createdAt: String = "",
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("phones")
    var phones: List<String> = listOf(),
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("street")
    var street: String = ""
)
//card payment key
data class CardPaymentKeyRequest(
    @SerializedName("amount_cents")
    var amountCents: String = "",
    @SerializedName("auth_token")
    var authToken: String = "",
    @SerializedName("billing_data")
    var billingData: BillingData = BillingData(),
    @SerializedName("currency")
    var currency: String = "EGP",
    @SerializedName("expiration")
    var expiration: Int = 0,
    @SerializedName("integration_id")
    var integrationId: Int = 0,
    @SerializedName("lock_order_when_paid")
    var lockOrderWhenPaid: String = "",
    @SerializedName("order_id")
    var orderId: Int = 0
)

data class BillingData(
    @SerializedName("apartment")
    var apartment: String = "",
    @SerializedName("building")
    var building: String = "",
    @SerializedName("city")
    var city: String = "",
    @SerializedName("country")
    var country: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("first_name")
    var firstName: String = "",
    @SerializedName("floor")
    var floor: String = "",
    @SerializedName("last_name")
    var lastName: String = "",
    @SerializedName("phone_number")
    var phoneNumber: String = "",
    @SerializedName("postal_code")
    var postalCode: String = "",
    @SerializedName("shipping_method")
    var shippingMethod: String = "",
    @SerializedName("state")
    var state: String = "",
    @SerializedName("street")
    var street: String = ""
)

data class CardPaymentKeyResposne(
    @SerializedName("token")
    var token: String = ""
)