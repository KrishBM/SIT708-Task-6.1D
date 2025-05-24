package com.example.task61.utils;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;

import android.content.Context;

public class GooglePayConfig {
    
    // Environment configuration - Use TEST for development, PRODUCTION for live app
    private static final int PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;
    
    // Gateway configuration - Replace with your actual payment processor details
    private static final String GATEWAY_TOKENIZATION_NAME = "example";
    private static final String GATEWAY_MERCHANT_ID = "exampleGatewayMerchantId";
    
    // Merchant info - Replace with your actual merchant details
    private static final String MERCHANT_ID = "01234567890123456789";
    private static final String MERCHANT_NAME = "Learning App Store";
    
    // Country code for transactions
    private static final String COUNTRY_CODE = "US";
    private static final String CURRENCY_CODE = "USD";
    
    /**
     * Gateway tokenization configuration
     */
    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {{
                put("gateway", GATEWAY_TOKENIZATION_NAME);
                put("gatewayMerchantId", GATEWAY_MERCHANT_ID);
            }});
        }};
    }
    
    /**
     * Card payment method configuration
     */
    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");
        
        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", new JSONArray()
                .put("PAN_ONLY")
                .put("CRYPTOGRAM_3DS"));
        parameters.put("allowedCardNetworks", new JSONArray()
                .put("AMEX")
                .put("DISCOVER")
                .put("JCB")
                .put("MASTERCARD")
                .put("VISA"));
        
        cardPaymentMethod.put("parameters", parameters);
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());
        
        return cardPaymentMethod;
    }
    
    /**
     * Information about the merchant requesting payment information
     */
    private static JSONObject getMerchantInfo() throws JSONException {
        return new JSONObject() {{
            put("merchantName", MERCHANT_NAME);
            put("merchantId", MERCHANT_ID);
        }};
    }
    
    /**
     * An object describing accepted forms of payment by your app
     */
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject() {{
            put("apiVersion", 2);
            put("apiVersionMinor", 0);
            put("allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));
        }};
    }
    
    /**
     * Create a Google Pay payment request
     */
    public static JSONObject getPaymentDataRequest(String price) throws JSONException {
        final JSONObject paymentDataRequest = getBaseRequest();
        paymentDataRequest.put("allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));
        paymentDataRequest.put("transactionInfo", getTransactionInfo(price));
        paymentDataRequest.put("merchantInfo", getMerchantInfo());
        
        return paymentDataRequest;
    }
    
    /**
     * Information about the current transaction
     */
    private static JSONObject getTransactionInfo(String price) throws JSONException {
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode", COUNTRY_CODE);
        transactionInfo.put("currencyCode", CURRENCY_CODE);
        
        return transactionInfo;
    }
    
    /**
     * Create a PaymentsClient instance for interacting with the Google Pay API
     */
    public static PaymentsClient createPaymentsClient(Context context) {
        Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(PAYMENTS_ENVIRONMENT)
                .build();
        
        return Wallet.getPaymentsClient(context, walletOptions);
    }
    
    /**
     * Check if Google Pay is ready to pay
     */
    public static JSONObject getIsReadyToPayRequest() throws JSONException {
        return getBaseRequest();
    }
    
    /**
     * Convert price from string to proper format for Google Pay
     */
    public static String formatPrice(String priceString) {
        // Remove currency symbols and convert to decimal
        String numericPrice = priceString.replaceAll("[^\\d.]", "");
        try {
            BigDecimal price = new BigDecimal(numericPrice);
            return price.setScale(2, RoundingMode.HALF_UP).toString();
        } catch (NumberFormatException e) {
            return "0.00";
        }
    }
} 