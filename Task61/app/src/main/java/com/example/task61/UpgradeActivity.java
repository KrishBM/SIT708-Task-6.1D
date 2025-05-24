package com.example.task61;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import com.example.task61.model.User;
import com.example.task61.utils.SessionManager;

import java.util.List;

public class UpgradeActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private SessionManager sessionManager;
    private User currentUser;
    private BillingClient billingClient;
    
    // UI Components
    private ImageView backButton;
    private TextView titleText;
    private TextView currentTierText;
    
    // Subscription Buttons
    private Button starterButton;
    private Button intermediateButton;
    private Button advancedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade);
        
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Debug: Show current user and tier info
        Toast.makeText(this, "Upgrade opened for: " + currentUser.getUsername() + 
                " (Current: " + currentUser.getSubscriptionTier().getDisplayName() + ")", 
                Toast.LENGTH_LONG).show();
        
        initializeViews();
        setupClickListeners();
        setupBillingClient();
        updateUI();
    }
    
    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        currentTierText = findViewById(R.id.current_tier_text);
        
        // Subscription buttons
        starterButton = findViewById(R.id.starter_button);
        intermediateButton = findViewById(R.id.intermediate_button);
        advancedButton = findViewById(R.id.advanced_button);
        
        titleText.setText("Upgrade your experience");
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        starterButton.setOnClickListener(v -> {
            // Starter is free, just update the user
            if (currentUser.getSubscriptionTier() != User.SubscriptionTier.STARTER) {
                updateSubscriptionTier(User.SubscriptionTier.STARTER);
                Toast.makeText(this, "Switched to Starter plan", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "You're already on the Starter plan", Toast.LENGTH_SHORT).show();
            }
        });
        
        intermediateButton.setOnClickListener(v -> {
            if (currentUser.getSubscriptionTier() != User.SubscriptionTier.INTERMEDIATE) {
                initiatePurchase("intermediate_subscription");
            } else {
                Toast.makeText(this, "You're already on the Intermediate plan", Toast.LENGTH_SHORT).show();
            }
        });
        
        advancedButton.setOnClickListener(v -> {
            if (currentUser.getSubscriptionTier() != User.SubscriptionTier.ADVANCED) {
                initiatePurchase("advanced_subscription");
            } else {
                Toast.makeText(this, "You're already on the Advanced plan", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this)
                .setListener(this)
                .enablePendingPurchases()
                .build();
                
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // Billing client is ready
                }
            }
            
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request
            }
        });
    }
    
    private void initiatePurchase(String productId) {
        // Launch PaymentActivity for processing payment
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        
        if (productId.equals("intermediate_subscription")) {
            paymentIntent.putExtra("subscription_tier", "Intermediate");
            paymentIntent.putExtra("subscription_price", "$4.99/month");
            paymentIntent.putExtra("target_tier", User.SubscriptionTier.INTERMEDIATE.name());
        } else if (productId.equals("advanced_subscription")) {
            paymentIntent.putExtra("subscription_tier", "Advanced");
            paymentIntent.putExtra("subscription_price", "$9.99/month");
            paymentIntent.putExtra("target_tier", User.SubscriptionTier.ADVANCED.name());
        }
        
        startActivityForResult(paymentIntent, 1001);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Payment was successful
            boolean paymentSuccess = data.getBooleanExtra("payment_success", false);
            String paymentMethod = data.getStringExtra("payment_method");
            String newTierString = data.getStringExtra("new_tier");
            
            if (paymentSuccess && newTierString != null) {
                // Refresh current user data
                currentUser = sessionManager.getCurrentUser();
                updateUI();
                
                Toast.makeText(this, "Successfully upgraded via " + paymentMethod + "!", 
                        Toast.LENGTH_LONG).show();
            }
        }
    }
    
    private void simulatePurchase(String productId) {
        // Simulate purchase flow for demo
        Toast.makeText(this, "Processing purchase...", Toast.LENGTH_SHORT).show();
        
        new android.os.Handler().postDelayed(() -> {
            User.SubscriptionTier newTier;
            if (productId.equals("intermediate_subscription")) {
                newTier = User.SubscriptionTier.INTERMEDIATE;
            } else {
                newTier = User.SubscriptionTier.ADVANCED;
            }
            
            updateSubscriptionTier(newTier);
            Toast.makeText(this, "Purchase successful! Welcome to " + newTier.getDisplayName() + "!", 
                    Toast.LENGTH_LONG).show();
        }, 2000);
    }
    
    private void updateSubscriptionTier(User.SubscriptionTier newTier) {
        currentUser.setSubscriptionTier(newTier);
        sessionManager.saveUser(currentUser);
        updateUI();
    }
    
    private void updateUI() {
        currentTierText.setText("Current Plan: " + currentUser.getSubscriptionTier().getDisplayName());
        updateButtonStates();
    }
    
    private void updateButtonStates() {
        User.SubscriptionTier currentTier = currentUser.getSubscriptionTier();
        
        // Reset all buttons
        starterButton.setText("Starter - Free");
        intermediateButton.setText("Intermediate - $4.99/month");
        advancedButton.setText("Advanced - $9.99/month");
        
        starterButton.setEnabled(true);
        intermediateButton.setEnabled(true);
        advancedButton.setEnabled(true);
        
        // Update current tier button
        switch (currentTier) {
            case STARTER:
                starterButton.setText("✓ Current Plan - Starter");
                starterButton.setEnabled(false);
                break;
            case INTERMEDIATE:
                intermediateButton.setText("✓ Current Plan - Intermediate");
                intermediateButton.setEnabled(false);
                break;
            case ADVANCED:
                advancedButton.setText("✓ Current Plan - Advanced");
                advancedButton.setEnabled(false);
                break;
        }
    }
    
    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                handlePurchase(purchase);
            }
        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
            Toast.makeText(this, "Purchase cancelled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Purchase failed", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void handlePurchase(Purchase purchase) {
        // Handle successful purchase
        String productId = purchase.getProducts().get(0);
        
        User.SubscriptionTier newTier;
        if (productId.equals("intermediate_subscription")) {
            newTier = User.SubscriptionTier.INTERMEDIATE;
        } else if (productId.equals("advanced_subscription")) {
            newTier = User.SubscriptionTier.ADVANCED;
        } else {
            return;
        }
        
        updateSubscriptionTier(newTier);
        Toast.makeText(this, "Welcome to " + newTier.getDisplayName() + "!", Toast.LENGTH_LONG).show();
    }
    
    @Override
    protected void onDestroy() {
        if (billingClient != null) {
            billingClient.endConnection();
        }
        super.onDestroy();
    }
} 