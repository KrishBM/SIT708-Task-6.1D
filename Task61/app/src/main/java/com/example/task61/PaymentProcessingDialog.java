package com.example.task61;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PaymentProcessingDialog extends Dialog {

    private String paymentMethod;
    private TextView messageText;
    private ProgressBar progressBar;
    private ImageView paymentIcon;

    public PaymentProcessingDialog(Context context, String paymentMethod) {
        super(context);
        this.paymentMethod = paymentMethod;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_payment_processing);
        setCancelable(false);

        messageText = findViewById(R.id.message_text);
        progressBar = findViewById(R.id.progress_bar);
        paymentIcon = findViewById(R.id.payment_icon);

        setupForPaymentMethod();
    }

    private void setupForPaymentMethod() {
        switch (paymentMethod) {
            case "Credit Card":
                messageText.setText("Processing your card payment...\nSecurely connecting to bank...");
                paymentIcon.setImageResource(R.drawable.ic_credit_card);
                break;
            case "PayPal":
                messageText.setText("Connecting to PayPal...\nVerifying your account...");
                paymentIcon.setImageResource(R.drawable.ic_paypal);
                break;
            case "Google Pay":
                messageText.setText("Processing with Google Pay...\nConfirming payment...");
                paymentIcon.setImageResource(R.drawable.ic_google_pay);
                break;
            case "Apple Pay":
                messageText.setText("Processing with Apple Pay...\nConfirming payment...");
                paymentIcon.setImageResource(R.drawable.ic_google_pay);
                break;
            case "Bank Transfer":
                messageText.setText("Initiating bank transfer...\nThis may take a few moments...");
                paymentIcon.setImageResource(R.drawable.ic_bank_transfer);
                break;
            default:
                messageText.setText("Processing payment...");
        }
    }

    public void updateProgress(String message) {
        if (messageText != null) {
            messageText.setText(message);
        }
    }
} 