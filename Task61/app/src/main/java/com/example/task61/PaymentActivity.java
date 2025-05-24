package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.task61.model.User;
import com.example.task61.utils.GooglePayConfig;
import com.example.task61.utils.SessionManager;
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

public class PaymentActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User currentUser;
    
    // Google Pay
    private PaymentsClient paymentsClient;
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    
    // Intent data
    private String subscriptionTier;
    private String subscriptionPrice;
    private User.SubscriptionTier targetTier;
    
    // UI Components
    private ImageView backButton;
    private TextView titleText;
    private TextView subscriptionInfoText;
    private TextView priceText;
    
    // Payment Methods
    private RadioGroup paymentMethodGroup;
    private RadioButton creditCardRadio;
    private RadioButton paypalRadio;
    private RadioButton applePayRadio;
    private RadioButton bankTransferRadio;
    
    // Google Pay Button
    private CardView googlePayButton;
    private TextView googlePayPriceText;
    
    // Payment Forms
    private LinearLayout creditCardForm;
    private LinearLayout paypalForm;
    private LinearLayout bankTransferForm;
    
    // Credit Card Form
    private EditText cardNumberEdit;
    private EditText expiryEdit;
    private EditText cvvEdit;
    private EditText cardHolderEdit;
    
    // PayPal Form
    private EditText paypalEmailEdit;
    private EditText paypalPasswordEdit;
    
    // Bank Transfer Form
    private EditText bankAccountEdit;
    private EditText routingNumberEdit;
    
    // Action Buttons
    private Button processPaymentButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize Google Pay
        paymentsClient = GooglePayConfig.createPaymentsClient(this);
        
        // Get intent data
        subscriptionTier = getIntent().getStringExtra("subscription_tier");
        subscriptionPrice = getIntent().getStringExtra("subscription_price");
        String tierString = getIntent().getStringExtra("target_tier");
        
        if (tierString != null) {
            targetTier = User.SubscriptionTier.valueOf(tierString);
        }
        
        initializeViews();
        setupClickListeners();
        updateUI();
        checkGooglePayAvailability();
    }
    
    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        subscriptionInfoText = findViewById(R.id.subscription_info_text);
        priceText = findViewById(R.id.price_text);
        
        // Payment method radio group
        paymentMethodGroup = findViewById(R.id.payment_method_group);
        creditCardRadio = findViewById(R.id.credit_card_radio);
        paypalRadio = findViewById(R.id.paypal_radio);
        applePayRadio = findViewById(R.id.apple_pay_radio);
        bankTransferRadio = findViewById(R.id.bank_transfer_radio);
        
        // Google Pay button (we'll add this to the layout programmatically)
        googlePayButton = findViewById(R.id.google_pay_button);
        if (googlePayButton != null) {
            googlePayPriceText = googlePayButton.findViewById(R.id.google_pay_price);
        }
        
        // Payment forms
        creditCardForm = findViewById(R.id.credit_card_form);
        paypalForm = findViewById(R.id.paypal_form);
        bankTransferForm = findViewById(R.id.bank_transfer_form);
        
        // Credit card form fields
        cardNumberEdit = findViewById(R.id.card_number_edit);
        expiryEdit = findViewById(R.id.expiry_edit);
        cvvEdit = findViewById(R.id.cvv_edit);
        cardHolderEdit = findViewById(R.id.card_holder_edit);
        
        // PayPal form fields
        paypalEmailEdit = findViewById(R.id.paypal_email_edit);
        paypalPasswordEdit = findViewById(R.id.paypal_password_edit);
        
        // Bank transfer form fields
        bankAccountEdit = findViewById(R.id.bank_account_edit);
        routingNumberEdit = findViewById(R.id.routing_number_edit);
        
        // Action buttons
        processPaymentButton = findViewById(R.id.process_payment_button);
        cancelButton = findViewById(R.id.cancel_button);
        
        titleText.setText("Complete Payment");
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        cancelButton.setOnClickListener(v -> finish());
        
        // Payment method selection
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            hideAllForms();
            
            if (checkedId == R.id.credit_card_radio) {
                creditCardForm.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.paypal_radio) {
                paypalForm.setVisibility(View.VISIBLE);
            } else if (checkedId == R.id.bank_transfer_radio) {
                bankTransferForm.setVisibility(View.VISIBLE);
            }
        });
        
        // Google Pay button click
        if (googlePayButton != null) {
            googlePayButton.setOnClickListener(v -> requestGooglePayPayment());
        }
        
        // Process payment
        processPaymentButton.setOnClickListener(v -> processPayment());
        
        // Select credit card by default
        creditCardRadio.setChecked(true);
        creditCardForm.setVisibility(View.VISIBLE);
    }
    
    private void hideAllForms() {
        creditCardForm.setVisibility(View.GONE);
        paypalForm.setVisibility(View.GONE);
        bankTransferForm.setVisibility(View.GONE);
    }
    
    private void updateUI() {
        subscriptionInfoText.setText("Upgrading to " + subscriptionTier + " Plan");
        priceText.setText(subscriptionPrice);
        
        if (googlePayPriceText != null) {
            googlePayPriceText.setText(subscriptionPrice);
        }
    }
    
    /**
     * Check if Google Pay is available and ready
     */
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
                        if (googlePayButton != null) {
                            googlePayButton.setVisibility(isReady ? View.VISIBLE : View.GONE);
                        }
                        if (isReady) {
                            Toast.makeText(PaymentActivity.this, "Google Pay is ready!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Google Pay is not available
                        if (googlePayButton != null) {
                            googlePayButton.setVisibility(View.GONE);
                        }
                        Toast.makeText(PaymentActivity.this, "Google Pay is not available", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (JSONException e) {
            // Google Pay is not available
            if (googlePayButton != null) {
                googlePayButton.setVisibility(View.GONE);
            }
        }
    }
    
    /**
     * Launch Google Pay payment request
     */
    private void requestGooglePayPayment() {
        try {
            String formattedPrice = GooglePayConfig.formatPrice(subscriptionPrice);
            PaymentDataRequest paymentDataRequest = PaymentDataRequest.fromJson(
                    GooglePayConfig.getPaymentDataRequest(formattedPrice).toString()
            );
            
            if (paymentDataRequest != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(paymentDataRequest),
                        this,
                        LOAD_PAYMENT_DATA_REQUEST_CODE
                );
            }
        } catch (JSONException e) {
            Toast.makeText(this, "Error creating Google Pay request", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handle Google Pay result
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        switch (requestCode) {
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handleGooglePaySuccess(paymentData);
                        break;
                    case RESULT_CANCELED:
                        Toast.makeText(this, "Google Pay payment cancelled", Toast.LENGTH_SHORT).show();
                        break;
                    case AutoResolveHelper.RESULT_ERROR:
                        Status apiException = AutoResolveHelper.getStatusFromIntent(data);
                        handleGooglePayError(apiException.getStatusCode());
                        break;
                }
                break;
        }
    }
    
    /**
     * Handle successful Google Pay payment
     */
    private void handleGooglePaySuccess(PaymentData paymentData) {
        // Extract payment information
        String paymentInfo = paymentData.toJson();
        
        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            String paymentToken = paymentMethodData.getJSONObject("tokenizationData").getString("token");
            
            // In a real app, you would send this token to your server for processing
            // For demo purposes, we'll proceed with the subscription update
            
            Toast.makeText(this, "Google Pay payment successful!", Toast.LENGTH_SHORT).show();
            
            // Update user subscription
            currentUser.setSubscriptionTier(targetTier);
            sessionManager.saveUser(currentUser);
            
            // Show payment receipt
            Intent receiptIntent = new Intent(this, PaymentReceiptActivity.class);
            receiptIntent.putExtra("payment_method", "Google Pay");
            receiptIntent.putExtra("subscription_tier", subscriptionTier);
            receiptIntent.putExtra("subscription_price", subscriptionPrice);
            receiptIntent.putExtra("new_tier", targetTier.name());
            startActivity(receiptIntent);
            
            // Return success result
            Intent resultIntent = new Intent();
            resultIntent.putExtra("payment_success", true);
            resultIntent.putExtra("payment_method", "Google Pay");
            resultIntent.putExtra("new_tier", targetTier.name());
            setResult(RESULT_OK, resultIntent);
            finish();
            
        } catch (JSONException e) {
            Toast.makeText(this, "Error processing Google Pay response", Toast.LENGTH_SHORT).show();
        }
    }
    
    /**
     * Handle Google Pay errors
     */
    private void handleGooglePayError(int statusCode) {
        Toast.makeText(this, "Google Pay error: " + statusCode, Toast.LENGTH_SHORT).show();
    }
    
    private void processPayment() {
        int selectedPaymentMethod = paymentMethodGroup.getCheckedRadioButtonId();
        
        if (selectedPaymentMethod == -1) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            return;
        }
        
        String paymentMethod = getSelectedPaymentMethod(selectedPaymentMethod);
        
        if (!validatePaymentForm(selectedPaymentMethod)) {
            return;
        }
        
        // Show processing dialog
        PaymentProcessingDialog processingDialog = new PaymentProcessingDialog(this, paymentMethod);
        processingDialog.show();
        
        processPaymentButton.setEnabled(false);
        processPaymentButton.setText("Processing...");
        
        // Simulate different payment processing stages
        simulatePaymentStages(paymentMethod, processingDialog);
    }
    
    private void simulatePaymentStages(String paymentMethod, PaymentProcessingDialog dialog) {
        new android.os.Handler().postDelayed(() -> {
            dialog.updateProgress("Verifying payment details...");
        }, 1000);
        
        new android.os.Handler().postDelayed(() -> {
            dialog.updateProgress("Authorizing transaction...");
        }, 2000);
        
        new android.os.Handler().postDelayed(() -> {
            dialog.updateProgress("Finalizing payment...");
        }, 3000);
        
        // Simulate different payment processing times for different methods
        int selectedPaymentMethod = paymentMethodGroup.getCheckedRadioButtonId();
        int processingTime = getProcessingTime(selectedPaymentMethod);
        
        new android.os.Handler().postDelayed(() -> {
            dialog.dismiss();
            simulatePaymentResult(paymentMethod);
        }, processingTime);
    }
    
    private String getSelectedPaymentMethod(int checkedId) {
        if (checkedId == R.id.credit_card_radio) {
            return "Credit Card";
        } else if (checkedId == R.id.paypal_radio) {
            return "PayPal";
        } else if (checkedId == R.id.apple_pay_radio) {
            return "Apple Pay";
        } else if (checkedId == R.id.bank_transfer_radio) {
            return "Bank Transfer";
        }
        return "Unknown";
    }
    
    private boolean validatePaymentForm(int checkedId) {
        if (checkedId == R.id.credit_card_radio) {
            return validateCreditCardForm();
        } else if (checkedId == R.id.paypal_radio) {
            return validatePayPalForm();
        } else if (checkedId == R.id.bank_transfer_radio) {
            return validateBankTransferForm();
        }
        return true;
    }
    
    private boolean validateCreditCardForm() {
        String cardNumber = cardNumberEdit.getText().toString().trim();
        String expiry = expiryEdit.getText().toString().trim();
        String cvv = cvvEdit.getText().toString().trim();
        String cardHolder = cardHolderEdit.getText().toString().trim();
        
        if (cardNumber.isEmpty()) {
            cardNumberEdit.setError("Card number is required");
            cardNumberEdit.requestFocus();
            return false;
        }
        
        if (cardNumber.length() < 16) {
            cardNumberEdit.setError("Enter a valid card number");
            cardNumberEdit.requestFocus();
            return false;
        }
        
        if (expiry.isEmpty()) {
            expiryEdit.setError("Expiry date is required");
            expiryEdit.requestFocus();
            return false;
        }
        
        if (cvv.isEmpty() || cvv.length() < 3) {
            cvvEdit.setError("Enter a valid CVV");
            cvvEdit.requestFocus();
            return false;
        }
        
        if (cardHolder.isEmpty()) {
            cardHolderEdit.setError("Cardholder name is required");
            cardHolderEdit.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean validatePayPalForm() {
        String email = paypalEmailEdit.getText().toString().trim();
        String password = paypalPasswordEdit.getText().toString().trim();
        
        if (email.isEmpty()) {
            paypalEmailEdit.setError("PayPal email is required");
            paypalEmailEdit.requestFocus();
            return false;
        }
        
        if (password.isEmpty()) {
            paypalPasswordEdit.setError("PayPal password is required");
            paypalPasswordEdit.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private boolean validateBankTransferForm() {
        String account = bankAccountEdit.getText().toString().trim();
        String routing = routingNumberEdit.getText().toString().trim();
        
        if (account.isEmpty()) {
            bankAccountEdit.setError("Account number is required");
            bankAccountEdit.requestFocus();
            return false;
        }
        
        if (routing.isEmpty()) {
            routingNumberEdit.setError("Routing number is required");
            routingNumberEdit.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private int getProcessingTime(int checkedId) {
        if (checkedId == R.id.credit_card_radio) {
            return 2000; // 2 seconds
        } else if (checkedId == R.id.paypal_radio) {
            return 3000; // 3 seconds
        } else if (checkedId == R.id.apple_pay_radio) {
            return 1500; // 1.5 seconds
        } else if (checkedId == R.id.bank_transfer_radio) {
            return 4000; // 4 seconds
        }
        return 2000;
    }
    
    private void simulatePaymentResult(String paymentMethod) {
        // Simulate 90% success rate
        boolean paymentSuccess = Math.random() < 0.9;
        
        if (paymentSuccess) {
            // Update user subscription
            currentUser.setSubscriptionTier(targetTier);
            sessionManager.saveUser(currentUser);
            
            // Show payment receipt
            Intent receiptIntent = new Intent(this, PaymentReceiptActivity.class);
            receiptIntent.putExtra("payment_method", paymentMethod);
            receiptIntent.putExtra("subscription_tier", subscriptionTier);
            receiptIntent.putExtra("subscription_price", subscriptionPrice);
            receiptIntent.putExtra("new_tier", targetTier.name());
            startActivity(receiptIntent);
            
            // Return success result
            Intent resultIntent = new Intent();
            resultIntent.putExtra("payment_success", true);
            resultIntent.putExtra("payment_method", paymentMethod);
            resultIntent.putExtra("new_tier", targetTier.name());
            setResult(RESULT_OK, resultIntent);
            finish();
        } else {
            // Payment failed
            processPaymentButton.setEnabled(true);
            processPaymentButton.setText("Process Payment");
            
            Toast.makeText(this, "Payment failed via " + paymentMethod + ". Please try again or use a different payment method.", 
                    Toast.LENGTH_LONG).show();
        }
    }
} 