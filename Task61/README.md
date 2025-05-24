# SIT708 Task 6.1D - AI-Powered Mobile Learning Application

An advanced Android mobile learning application that combines artificial intelligence with modern mobile development practices to deliver personalized educational experiences. The app features AI-generated quizzes, comprehensive user analytics, subscription-based learning tiers, and integrated payment processing.

## 🚀 Features Overview

### 🔐 User Authentication & Management
- **Secure Registration/Login System**: Complete user authentication with session management
- **User Profiles**: Comprehensive profile system with statistics and progress tracking
- **Session Persistence**: Automatic login state management across app sessions

### 💎 Subscription-Based Learning Tiers
- **Starter Tier** (Free): Basic quiz access and limited features
- **Intermediate Tier** ($4.99): Enhanced quiz content and progress analytics
- **Advanced Tier** ($9.99): Premium features with full analytics and unlimited access

### 🤖 AI-Powered Quiz Generation
- **LLM Integration**: Dynamic quiz generation using Large Language Models
- **Category-Based Content**: Quizzes tailored to specific learning topics
- **Adaptive Difficulty**: Content complexity based on user performance
- **Real-time Feedback**: Instant explanations for quiz answers

### 📊 Advanced Analytics & Progress Tracking
- **Performance Statistics**: Detailed tracking of correct/incorrect answers and accuracy rates
- **Quiz History**: Comprehensive history with timestamps, scores, and categories
- **Progress Visualization**: Charts and graphs showing learning progression
- **Achievement System**: Track completed quizzes and skill milestones

### 💳 Multi-Platform Payment Integration
- **Google Pay API**: Native Google Pay integration with secure authentication
- **Multiple Payment Methods**: Credit/debit cards, PayPal, Apple Pay, bank transfers
- **Transaction Management**: Automated receipt generation with unique transaction IDs
- **Subscription Management**: Easy upgrade/downgrade between subscription tiers

### 📱 Social & Sharing Features
- **Profile Sharing**: QR code generation for sharing learning profiles
- **Social Integration**: Share achievements and progress on social media
- **Receipt Sharing**: Share payment receipts and subscription confirmations

### 🎨 Modern UI/UX Design
- **Material Design 3**: Latest Material Design components and guidelines
- **Responsive Layouts**: Optimized for various screen sizes and orientations
- **Dark/Light Theme**: Adaptive color schemes for user preference
- **Accessibility**: WCAG-compliant design with proper contrast and text sizing

## 🏗️ Technical Architecture

### Development Stack
- **Language**: Java (Android Native)
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 35 (Android 15)
- **Architecture**: Modular MVC pattern with clear separation of concerns

### Key Dependencies
```gradle
// Core Android Components
implementation 'androidx.appcompat:appcompat:1.6.1'
implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
implementation 'androidx.cardview:cardview:1.0.0'

// Material Design
implementation 'com.google.android.material:material:1.9.0'

// Payment Processing
implementation 'com.google.android.gms:play-services-wallet:19.2.1'
implementation 'com.google.android.gms:play-services-base:18.2.0'

// Data Processing
implementation 'com.google.code.gson:gson:2.10.1'

// Networking
implementation 'com.android.volley:volley:1.2.1'
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'

// Image Processing
implementation 'com.github.bumptech.glide:glide:4.15.1'

// QR Code Generation
implementation 'com.journeyapps:zxing-android-embedded:4.3.0'

// Charts and Analytics
implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'

// Database
implementation 'androidx.room:room-runtime:2.5.0'
```

### Project Structure
```
app/src/main/java/com/example/task61/
├── adapter/              # RecyclerView adapters
│   ├── QuizHistoryAdapter.java
│   └── TaskAdapter.java
├── api/                  # API service classes
│   └── LLMApiService.java
├── model/                # Data models
│   ├── User.java
│   ├── Quiz.java
│   ├── Question.java
│   └── QuizHistory.java
├── utils/                # Utility classes
│   ├── SessionManager.java
│   ├── QuizHistoryManager.java
│   └── GooglePayConfig.java
├── activities/           # Activity classes
│   ├── MainActivity.java
│   ├── LoginActivity.java
│   ├── RegisterActivity.java
│   ├── DashboardActivity.java
│   ├── ProfileActivity.java
│   ├── QuizActivity.java
│   ├── ResultsActivity.java
│   ├── HistoryActivity.java
│   ├── UpgradeActivity.java
│   ├── PaymentActivity.java
│   └── ShareProfileActivity.java
└── res/                  # Resources (layouts, drawables, values)
```

## 🛠️ Setup and Installation

### Prerequisites
- Android Studio Arctic Fox or later
- Java Development Kit (JDK) 8 or later
- Android SDK with minimum API level 24

### Installation Steps

1. **Clone the Repository**
   ```bash
   git clone https://github.com/KrishBM/SIT708-Task-6.1D.git
   cd SIT708-Task-6.1D/Task61
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing Android Studio project"
   - Navigate to the cloned repository and select the Task61 folder

3. **Configure Dependencies**
   - Sync the project to download all required dependencies
   - Ensure Google Play Services is available on your testing device

4. **Run the Application**
   - Connect an Android device or start an emulator
   - Click "Run" or use `Ctrl+R` (Windows/Linux) or `Cmd+R` (Mac)

### Google Pay Configuration
To test Google Pay functionality:
1. Ensure the testing device has Google Play Services installed
2. Set up a test Google account in the Play Console
3. Configure test payment methods in Google Pay settings

## 🎯 Usage Guide

### Getting Started
1. **Registration**: Create a new account with email and password
2. **Interest Selection**: Choose learning categories during onboarding
3. **Dashboard Navigation**: Access quizzes, profile, history, and upgrade options
4. **Take Quizzes**: Select AI-generated quizzes based on your interests
5. **Track Progress**: Monitor performance through analytics and history

### Subscription Management
1. **View Tiers**: Compare Starter, Intermediate, and Advanced plans
2. **Payment Processing**: Choose from multiple payment methods
3. **Receipt Generation**: Automatic receipt creation and sharing
4. **Upgrade/Downgrade**: Flexible subscription tier management

### Analytics and History
1. **Performance Metrics**: View accuracy rates and completion statistics
2. **Quiz History**: Browse detailed history of completed quizzes
3. **Progress Charts**: Visual representation of learning progress
4. **Profile Sharing**: Generate QR codes to share achievements

## 🧠 AI Integration and LLM Usage

### Current Implementation
- **Quiz Generation**: AI creates dynamic, category-specific quiz content
- **Adaptive Content**: Questions adjust based on user performance
- **Intelligent Feedback**: AI-generated explanations for quiz answers

### Future AI Enhancements
- **Personalized Learning Paths**: AI-driven curriculum recommendations
- **Natural Language Interaction**: Conversational learning assistance
- **Advanced Analytics**: ML-powered insights and predictions
- **Content Personalization**: Learning materials adapted to individual styles

## 🔧 Key Features Implementation

### User Statistics Tracking
```java
// User performance metrics
private int correctAnswers;
private int incorrectAnswers;
private int totalQuestions;
private double accuracyPercentage;
private List<String> completedQuizIds;
```

### Quiz History Management
```java
// Persistent storage using SharedPreferences and Gson
public class QuizHistoryManager {
    public void saveQuizHistory(QuizHistory quizHistory);
    public List<QuizHistory> getQuizHistoryForUser(String userId);
    public int getQuizCount(String userId);
}
```

### Google Pay Integration
```java
// Google Pay API configuration
public class GooglePayConfig {
    public static PaymentsClient createPaymentsClient(Context context);
    public static JSONObject createPaymentRequest();
    public static boolean isGooglePayAvailable(Context context);
}
```

## 🚀 Future Enhancements

### Short-term Goals
- **Offline Mode**: Download quizzes for offline learning
- **Push Notifications**: Reminders and achievement notifications
- **Multi-language Support**: Localization for global users
- **Advanced Charts**: More detailed analytics visualizations

### Long-term Vision
- **Machine Learning**: Predictive learning path recommendations
- **Social Learning**: Study groups and peer collaboration
- **AR/VR Integration**: Immersive learning experiences
- **Cross-platform Sync**: Web and iOS companion apps

## 🤝 Contributing

### Development Guidelines
1. Follow Material Design principles
2. Maintain modular architecture
3. Write comprehensive unit tests
4. Document all public methods
5. Use meaningful commit messages

### Code Standards
- Java 8+ features where applicable
- Consistent naming conventions
- Proper error handling
- Performance optimization
- Security best practices

## 📝 License

This project is developed as part of the SIT708 Mobile Application Development course at Deakin University. All rights reserved.

## 👥 Development Team

- **Developer**: Krish Moradiya
- **Student ID**: 223880281
- **Course**: SIT708 Mobile Application Development
- **Institution**: Deakin University
- **Trimester**: T1 2025

## 📚 Documentation and Resources

### Technical References
- [Android Developer Documentation](https://developer.android.com/)
- [Material Design Guidelines](https://material.io/design)
- [Google Pay API Documentation](https://developers.google.com/pay/api)
- [Gson Library Documentation](https://github.com/google/gson)

### Course Materials
- SIT708 Mobile Application Development
- Deakin University Learning Management System
- Android Development Best Practices

## 🆘 Support and Issues

For technical support or bug reports:
1. Check existing documentation
2. Review common issues in course materials
3. Contact course instructors for academic guidance
4. Submit detailed bug reports with logs and screenshots

---

**Last Updated**: January 2025  
**Version**: 1.0.0  
**Build Status**: ✅ Stable 