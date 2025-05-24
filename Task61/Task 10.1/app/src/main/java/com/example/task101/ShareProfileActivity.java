package com.example.task101;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.task101.model.User;
import com.example.task101.utils.SessionManager;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class ShareProfileActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private User currentUser;
    
    // UI Components
    private ImageView backButton;
    private TextView titleText;
    private CardView profilePreviewCard;
    private TextView previewUsernameText;
    private TextView previewStatsText;
    private TextView previewInterestsText;
    private ImageView qrCodeImageView;
    private Button generateQRButton;
    private Button shareViaAppsButton;
    private Button copyLinkButton;
    private LinearLayout sharingOptionsLayout;
    
    private String shareableProfileUrl;
    private Bitmap qrCodeBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_profile);
        
        sessionManager = new SessionManager(this);
        currentUser = sessionManager.getCurrentUser();
        
        if (currentUser == null) {
            Toast.makeText(this, "Please log in again", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        initializeViews();
        setupClickListeners();
        setupProfilePreview();
        generateShareableUrl();
    }
    
    private void initializeViews() {
        backButton = findViewById(R.id.back_button);
        titleText = findViewById(R.id.title_text);
        profilePreviewCard = findViewById(R.id.profile_preview_card);
        previewUsernameText = findViewById(R.id.preview_username);
        previewStatsText = findViewById(R.id.preview_stats);
        previewInterestsText = findViewById(R.id.preview_interests);
        qrCodeImageView = findViewById(R.id.qr_code_image);
        generateQRButton = findViewById(R.id.generate_qr_button);
        shareViaAppsButton = findViewById(R.id.share_via_apps_button);
        copyLinkButton = findViewById(R.id.copy_link_button);
        sharingOptionsLayout = findViewById(R.id.sharing_options_layout);
        
        titleText.setText("Share Profile");
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(v -> finish());
        
        generateQRButton.setOnClickListener(v -> generateQRCode());
        
        shareViaAppsButton.setOnClickListener(v -> shareViaApps());
        
        copyLinkButton.setOnClickListener(v -> copyLinkToClipboard());
        
        qrCodeImageView.setOnClickListener(v -> {
            if (qrCodeBitmap != null) {
                saveQRCodeToGallery();
            }
        });
    }
    
    private void setupProfilePreview() {
        previewUsernameText.setText(currentUser.getUsername());
        
        // Create stats summary
        String stats = String.format("📊 %d Questions • %.1f%% Accuracy • %s Member",
                currentUser.getTotalQuestions(),
                currentUser.getAccuracyPercentage(),
                currentUser.getSubscriptionTier().getDisplayName());
        previewStatsText.setText(stats);
        
        // Show interests
        if (currentUser.getInterests() != null && !currentUser.getInterests().isEmpty()) {
            StringBuilder interests = new StringBuilder("🎯 Interests: ");
            for (int i = 0; i < Math.min(currentUser.getInterests().size(), 3); i++) {
                if (i > 0) interests.append(", ");
                interests.append(currentUser.getInterests().get(i));
            }
            if (currentUser.getInterests().size() > 3) {
                interests.append(" and ").append(currentUser.getInterests().size() - 3).append(" more");
            }
            previewInterestsText.setText(interests.toString());
        } else {
            previewInterestsText.setText("🎯 Interests: Not specified");
        }
    }
    
    private void generateShareableUrl() {
        // Generate a unique shareable profile ID if not exists
        if (currentUser.getShareableProfileId() == null || currentUser.getShareableProfileId().isEmpty()) {
            String shareableId = UUID.randomUUID().toString().substring(0, 8);
            currentUser.setShareableProfileId(shareableId);
            sessionManager.saveUser(currentUser);
        }
        
        // Create shareable URL (in a real app, this would be your domain)
        shareableProfileUrl = "https://learningapp.com/profile/" + currentUser.getShareableProfileId();
        
        // Show sharing options
        sharingOptionsLayout.setVisibility(View.VISIBLE);
    }
    
    private void generateQRCode() {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            BitMatrix bitMatrix = writer.encode(shareableProfileUrl, BarcodeFormat.QR_CODE, 512, 512);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            
            qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrCodeBitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            
            qrCodeImageView.setImageBitmap(qrCodeBitmap);
            qrCodeImageView.setVisibility(View.VISIBLE);
            generateQRButton.setText("QR Code Generated ✓");
            generateQRButton.setEnabled(false);
            
            Toast.makeText(this, "QR Code generated! Tap to save to gallery", Toast.LENGTH_LONG).show();
            
        } catch (WriterException e) {
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    
    private void shareViaApps() {
        String shareText = String.format(
                "Check out my learning profile! 🎓\n\n" +
                "👤 %s\n" +
                "📊 %d questions completed with %.1f%% accuracy\n" +
                "🏆 %s member\n\n" +
                "View my full profile: %s",
                currentUser.getUsername(),
                currentUser.getTotalQuestions(),
                currentUser.getAccuracyPercentage(),
                currentUser.getSubscriptionTier().getDisplayName(),
                shareableProfileUrl
        );
        
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My Learning Profile");
        
        // If QR code is generated, include it as an image
        if (qrCodeBitmap != null) {
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), qrCodeBitmap, "QR Code", null);
            Uri imageUri = Uri.parse(path);
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        }
        
        startActivity(Intent.createChooser(shareIntent, "Share Profile"));
    }
    
    private void copyLinkToClipboard() {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Profile Link", shareableProfileUrl);
        clipboard.setPrimaryClip(clip);
        
        Toast.makeText(this, "Profile link copied to clipboard!", Toast.LENGTH_SHORT).show();
    }
    
    private void saveQRCodeToGallery() {
        if (qrCodeBitmap == null) return;
        
        try {
            String savedImageURL = MediaStore.Images.Media.insertImage(
                    getContentResolver(),
                    qrCodeBitmap,
                    "Profile QR Code",
                    "QR Code for " + currentUser.getUsername() + "'s profile"
            );
            
            if (savedImageURL != null) {
                Toast.makeText(this, "QR Code saved to gallery!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to save QR Code", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error saving QR Code: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
} 