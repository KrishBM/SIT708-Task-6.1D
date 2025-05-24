package com.example.task101;

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
import androidx.cardview.widget.CardView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.example.task101.model.User;
import com.example.task101.utils.SessionManager;
import com.google.common.collect.ImmutableList;

import java.util.List;

public class UpgradeActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    private SessionManager sessionManager;
    private User currentUser;
    private BillingClient billingClient;
    
    // UI Components
    private ImageView backButton;
    private TextView titleText;
    private TextView currentTierText;
    
    // Subscription Cards
    private CardView starterCard;
    private CardView intermediateCard;
    private CardView advancedCard;
    
    // Starter Plan
    private TextView starterTitleText;
    private TextView starterPriceText;
    private TextView starterFeaturesText;
    private Button starterButton;
    
    // Intermediate Plan
    private TextView intermediateTitleText;
    private TextView intermediatePriceText;
    private TextView intermediateFeaturesText;
    private Button intermediateButton;
    
    // Advanced Plan
    private TextView advancedTitleText;
    private TextView advancedPriceText;
    private TextView advancedFeaturesText;
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
        
        initializeViews();
        setupClickListeners();
        setupBillingClient();
        updateUI();
    }
    
    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        currentTierText = findViewById(R.id.current_tier_text);
        
        // Subscription cards
        starterCard = findViewById(R.id.starter_card);
        intermediateCard = findViewById(R.id.intermediate_card);
        advancedCard = findViewById(R.id.advanced_card);
        
        // Starter plan
        starterTitleText = findViewById(R.id.starter_title);
        starterPriceText = findViewById(R.id.starter_price);
        starterFeaturesText = findViewById(R.id.starter_features);
        starterButton = findViewById(R.id.starter_button);
        
        // Intermediate plan
        intermediateTitleText = findViewById(R.id.intermediate_title);
        intermediatePriceText = findViewById(R.id.intermediate_price);
        intermediateFeaturesText = findViewById(R.id.intermediate_features);
        intermediateButton = findViewById(R.id.intermediate_button);
        
        // Advanced plan
        advancedTitleText = findViewById(R.id.advanced_title);
        advancedPriceText = findViewById(R.id.advanced_price);
        advancedFeaturesText = findViewById(R.id.advanced_features);
        advancedButton = findViewById(R.id.advanced_button);
        
        titleText.setText("Upgrade your experience");
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        starterButton.setOnClickListener(v -> {
            // Starter is free, just update the user
            if (currentUser.getSubscriptionTier() != User.SubscriptionTier.STARTER) {
                updateSubscriptionTier(User.SubscriptionTier.STARTER);
            }
        });
        
        intermediateButton.setOnClickListener(v -> {
            if (currentUser.getSubscriptionTier() != User.SubscriptionTier.INTERMEDIATE) {
                initiatePurchase("intermediate_subscription");
            }
        });
        
        advancedButton.setOnClickListener(v -> {
            if (currentUser.getSubscriptionTier() != User.SubscriptionTier.ADVANCED) {
                initiatePurchase("advanced_subscription");
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
                    queryProductDetails();
                }
            }
            
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request
            }
        });
    }
    
    private void queryProductDetails() {
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(
                                ImmutableList.of(
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("intermediate_subscription")
                                                .setProductType(BillingClient.ProductType.SUBS)
                                                .build(),
                                        QueryProductDetailsParams.Product.newBuilder()
                                                .setProductId("advanced_subscription")
                                                .setProductType(BillingClient.ProductType.SUBS)
                                                .build()))
                        .build();

        billingClient.queryProductDetailsAsync(
                queryProductDetailsParams,
                (billingResult, productDetailsList) -> {
                    // Process the result
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        // Update UI with actual prices from Google Play
                        for (ProductDetails productDetails : productDetailsList) {
                            updatePriceDisplay(productDetails);
                        }
                    }
                }
        );
    }
    
    private void updatePriceDisplay(ProductDetails productDetails) {
        // Update the UI with actual prices from Google Play Console
        // This is a simplified implementation
        String productId = productDetails.getProductId();
        if (productId.equals("intermediate_subscription")) {
            // Update intermediate price display
        } else if (productId.equals("advanced_subscription")) {
            // Update advanced price display
        }
    }
    
    private void initiatePurchase(String productId) {
        // In a real implementation, you would use the actual ProductDetails
        // For demo purposes, we'll simulate the purchase
        simulatePurchase(productId);
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
        
        // Update subscription plan details
        updatePlanDetails();
        updateButtonStates();
    }
    
    private void updatePlanDetails() {
        // Starter Plan
        starterTitleText.setText("Starter");
        starterPriceText.setText("Free");
        starterFeaturesText.setText("• Basic quizzes\n• Limited topics\n• Basic progress tracking");
        
        // Intermediate Plan
        intermediateTitleText.setText("Intermediate");
        intermediatePriceText.setText("$4.99/month");
        intermediateFeaturesText.setText("• All Starter features\n• Advanced quizzes\n• Detailed analytics\n• Priority support");
        
        // Advanced Plan
        advancedTitleText.setText("Advanced");
        advancedPriceText.setText("$9.99/month");
        advancedFeaturesText.setText("• All Intermediate features\n• Unlimited quizzes\n• Custom quiz creation\n• Advanced AI insights\n• Premium support");
    }
    
    private void updateButtonStates() {
        User.SubscriptionTier currentTier = currentUser.getSubscriptionTier();
        
        // Reset all buttons
        starterButton.setText("Select");
        intermediateButton.setText("Purchase");
        advancedButton.setText("Purchase");
        
        starterButton.setEnabled(true);
        intermediateButton.setEnabled(true);
        advancedButton.setEnabled(true);
        
        // Update current tier button
        switch (currentTier) {
            case STARTER:
                starterButton.setText("Current Plan");
                starterButton.setEnabled(false);
                break;
            case INTERMEDIATE:
                intermediateButton.setText("Current Plan");
                intermediateButton.setEnabled(false);
                break;
            case ADVANCED:
                advancedButton.setText("Current Plan");
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