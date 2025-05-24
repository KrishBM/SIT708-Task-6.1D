package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task61.utils.GooglePayConfig;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentData;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

public class GooglePayTestActivity extends AppCompatActivity {

    private PaymentsClient paymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    
    private TextView statusText;
    private Button checkAvailabilityButton;
    private Button makePaymentButton;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_pay_test);
        
        initializeViews();
        setupGooglePay();
        setupClickListeners();
    }
    
    private void initializeViews() {
        statusText = findViewById(R.id.status_text);
        checkAvailabilityButton = findViewById(R.id.check_availability_button);
        makePaymentButton = findViewById(R.id.make_payment_button);
        
        makePaymentButton.setEnabled(false);
    }
    
    private void setupGooglePay() {
        paymentsClient = GooglePayConfig.createPaymentsClient(this);
        statusText.setText("Google Pay client initialized");
    }
    
    private void setupClickListeners() {
        checkAvailabilityButton.setOnClickListener(v -> checkGooglePayAvailability());
        makePaymentButton.setOnClickListener(v -> requestPayment());
        
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }
    
    private void checkGooglePayAvailability() {
        try {
            IsReadyToPayRequest isReadyToPayRequest = IsReadyToPayRequest.fromJson(
                    GooglePayConfig.getIsReadyToPayRequest().toString()
            );
            
            Task<Boolean> task = paymentsClient.isReadyToPay(isReadyToPayRequest);
            task.addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(Task<Boolean> task) {
                    if (task.isSuccessful()) {
                        boolean isReady = task.getResult();
                        if (isReady) {
                            statusText.setText("✅ Google Pay is available and ready!");
                            makePaymentButton.setEnabled(true);
                            Toast.makeText(GooglePayTestActivity.this, "Google Pay is ready to use", Toast.LENGTH_SHORT).show();
                        } else {
                            statusText.setText("❌ Google Pay is not available on this device");
                            makePaymentButton.setEnabled(false);
                        }
                    } else {
                        statusText.setText("❌ Error checking Google Pay availability: " + task.getException().getMessage());
                        makePaymentButton.setEnabled(false);
                    }
                }
            });
        } catch (JSONException e) {
            statusText.setText("❌ Error creating Google Pay request: " + e.getMessage());
            makePaymentButton.setEnabled(false);
        }
    }
    
    private void requestPayment() {
        try {
            String price = "4.99"; // Test price
            PaymentDataRequest paymentDataRequest = PaymentDataRequest.fromJson(
                    GooglePayConfig.getPaymentDataRequest(price).toString()
            );
            
            if (paymentDataRequest != null) {
                statusText.setText("🔄 Launching Google Pay...");
                
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(paymentDataRequest),
                        this,
                        LOAD_PAYMENT_DATA_REQUEST_CODE
                );
            }
        } catch (JSONException e) {
            statusText.setText("❌ Error creating payment request: " + e.getMessage());
            Toast.makeText(this, "Error creating Google Pay request", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;
                    case RESULT_CANCELED:
                        statusText.setText("⚠️ Payment was cancelled by user");
                        Toast.makeText(this, "Payment cancelled", Toast.LENGTH_SHORT).show();
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status apiException = AutoResolveHelper.getStatusFromIntent(data);
                        handlePaymentError(apiException.getStatusCode());
                        break;
                }
                break;
        }
    }
    
    private void handlePaymentSuccess(PaymentData paymentData) {
        String paymentInfo = paymentData.toJson();
        
        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            String paymentToken = paymentMethodData.getJSONObject("tokenizationData").getString("token");
            
            statusText.setText("✅ Payment successful!\n\nToken received (first 50 chars):\n" + 
                    paymentToken.substring(0, Math.min(50, paymentToken.length())) + "...");
            
            Toast.makeText(this, "Google Pay payment successful!", Toast.LENGTH_LONG).show();
            
        } catch (JSONException e) {
            statusText.setText("✅ Payment successful but error parsing response: " + e.getMessage());
            Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handlePaymentError(int statusCode) {
        statusText.setText("❌ Payment failed with error code: " + statusCode);
        Toast.makeText(this, "Payment failed: " + statusCode, Toast.LENGTH_SHORT).show();
    }
} 