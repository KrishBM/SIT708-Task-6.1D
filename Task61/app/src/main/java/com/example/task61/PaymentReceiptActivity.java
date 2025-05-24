package com.example.task61;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class PaymentReceiptActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User currentUser;
    
    // Intent data
    private String paymentMethod;
    private String subscriptionTier;
    private String subscriptionPrice;
    private User.SubscriptionTier newTier;
    
    // UI Components
    private ImageView successIcon;
    private TextView titleText;
    private TextView receiptNumberText;
    private TextView dateTimeText;
    private TextView paymentMethodText;
    private TextView subscriptionText;
    private TextView amountText;
    private TextView customerText;
    private Button continueButton;
    private Button shareReceiptButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);
        
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        // Get intent data
        paymentMethod = getIntent().getStringExtra("payment_method");
        subscriptionTier = getIntent().getStringExtra("subscription_tier");
        subscriptionPrice = getIntent().getStringExtra("subscription_price");
        String tierString = getIntent().getStringExtra("new_tier");
        
        if (tierString != null) {
            newTier = User.SubscriptionTier.valueOf(tierString);
        }
        
        initializeViews();
        setupClickListeners();
        populateReceipt();
    }
    
    private void initializeViews() {
        successIcon = findViewById(R.id.success_icon);
        titleText = findViewById(R.id.title_text);
        receiptNumberText = findViewById(R.id.receipt_number_text);
        dateTimeText = findViewById(R.id.date_time_text);
        paymentMethodText = findViewById(R.id.payment_method_text);
        subscriptionText = findViewById(R.id.subscription_text);
        amountText = findViewById(R.id.amount_text);
        customerText = findViewById(R.id.customer_text);
        continueButton = findViewById(R.id.continue_button);
        shareReceiptButton = findViewById(R.id.share_receipt_button);
    }
    
    private void setupClickListeners() {
        continueButton.setOnClickListener(v -> {
            // Return to dashboard
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });
        
        shareReceiptButton.setOnClickListener(v -> shareReceipt());
    }
    
    private void populateReceipt() {
        titleText.setText("Payment Successful!");
        
        // Generate receipt number
        String receiptNumber = "R" + System.currentTimeMillis() % 1000000;
        receiptNumberText.setText("Receipt #" + receiptNumber);
        
        // Set current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault());
        dateTimeText.setText(sdf.format(new Date()));
        
        // Payment details
        paymentMethodText.setText(paymentMethod);
        subscriptionText.setText(subscriptionTier + " Plan");
        amountText.setText(subscriptionPrice);
        
        if (currentUser != null) {
            customerText.setText(currentUser.getUsername());
        }
    }
    
    private void shareReceipt() {
        StringBuilder receiptText = new StringBuilder();
        receiptText.append("🎉 Payment Successful!\n\n");
        receiptText.append("Receipt #").append(receiptNumberText.getText().toString().replace("Receipt #", "")).append("\n");
        receiptText.append("Date: ").append(dateTimeText.getText()).append("\n");
        receiptText.append("Customer: ").append(customerText.getText()).append("\n");
        receiptText.append("Subscription: ").append(subscriptionText.getText()).append("\n");
        receiptText.append("Amount: ").append(amountText.getText()).append("\n");
        receiptText.append("Payment Method: ").append(paymentMethodText.getText()).append("\n\n");
        receiptText.append("Thank you for upgrading your learning experience!");
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, receiptText.toString());
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Payment Receipt - " + subscriptionTier + " Plan");
        
        startActivity(Intent.createChooser(shareIntent, "Share Receipt"));
    }
} 