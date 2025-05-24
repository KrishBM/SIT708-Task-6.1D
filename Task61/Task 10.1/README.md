# Task 10.1 - Enhanced Personalized Learning Experiences App

## Overview

This is an enhanced version of the Personalized Learning Experiences App that builds upon Task 6.1D. The app now includes three new major features: **Profile Management**, **Quiz History Tracking**, and **Profile Sharing with QR Codes**, along with **In-App Subscription Management**.

## New Features Added in Task 10.1

### 1. Profile Page 📱
- **User Profile Display**: Shows username, email, registration date, and subscription tier
- **Statistics Dashboard**: Visual representation of quiz performance with pie charts
- **Performance Metrics**: Total questions, correct/incorrect answers, and accuracy percentage
- **Subscription Tier Badge**: Visual indicator of current subscription level
- **Quick Actions**: Direct access to history, sharing, and upgrade features

### 2. History Feature 📊
- **Quiz History Tracking**: Complete record of all completed quizzes
- **Detailed Results**: Shows quiz title, category, completion date, score, and time taken
- **Performance Indicators**: Color-coded accuracy indicators for quick assessment
- **Searchable Interface**: Easy-to-navigate list of past quiz attempts
- **Progress Tracking**: Visual timeline of learning progress

### 3. Sharing Feature 🔗
- **QR Code Generation**: Creates unique QR codes for profile sharing
- **Social Media Integration**: Share profile via multiple platforms
- **Link Sharing**: Copy shareable profile links to clipboard
- **Profile Preview**: Shows what others will see when viewing shared profile
- **Privacy Controls**: Manage what information is shared publicly

### 4. Subscription Management ⭐
- **Three Tier System**: Starter (Free), Intermediate ($4.99), Advanced ($9.99)
- **In-App Billing**: Integrated Google Play Billing for seamless purchases
- **Feature Comparison**: Clear breakdown of features for each tier
- **Upgrade Flow**: Smooth transition between subscription levels
- **Current Plan Display**: Always shows active subscription status

## Technical Implementation

### Architecture & Design Patterns

#### Modern Android Development Practices
1. **Material Design 3**: Latest Material Design components and theming
2. **MVVM Architecture**: Separation of concerns with proper data flow
3. **Repository Pattern**: Centralized data management
4. **Dependency Injection**: Clean code structure and testability
5. **Reactive Programming**: Efficient data handling and UI updates

#### Key Technologies Used
- **Android SDK 34**: Latest Android features and APIs
- **Material Design Components**: Modern UI/UX components
- **MPAndroidChart**: Professional chart library for statistics
- **ZXing**: QR code generation and scanning
- **Google Play Billing**: In-app purchase management
- **Gson**: JSON serialization for data persistence
- **Glide**: Efficient image loading and caching

### Enhanced Data Models

#### User Model Enhancements
```java
public class User {
    // Existing fields
    private String id, username, email;
    private List<String> interests, completedQuizIds;
    
    // New enhanced fields
    private Date registrationDate;
    private SubscriptionTier subscriptionTier;
    private int totalQuestions, correctAnswers, incorrectAnswers;
    private String shareableProfileId;
    private boolean isProfilePublic;
    
    // Helper methods for statistics
    public double getAccuracyPercentage();
    public void incrementCorrectAnswers();
    public void incrementIncorrectAnswers();
}
```

#### Quiz History Model
```java
public class QuizHistory {
    private String id, userId, quizId, quizTitle, category;
    private List<QuestionResult> questionResults;
    private int totalQuestions, correctAnswers, incorrectAnswers;
    private double accuracyPercentage;
    private Date completedAt;
    private long timeTaken;
    
    public static class QuestionResult {
        private String questionId, questionText;
        private int selectedAnswerIndex, correctAnswerIndex;
        private boolean isCorrect;
        private String explanation;
    }
}
```

### New Activities & Features

#### ProfileActivity
- **Statistics Visualization**: Interactive pie charts showing performance
- **User Information Display**: Comprehensive profile overview
- **Action Buttons**: Quick access to related features
- **Responsive Design**: Adapts to different screen sizes

#### HistoryActivity
- **RecyclerView Implementation**: Efficient list display
- **Custom Adapter**: Optimized for quiz history items
- **Loading States**: Proper loading and empty state handling
- **Performance Indicators**: Visual feedback for quiz results

#### UpgradeActivity
- **Subscription Tiers**: Three distinct subscription levels
- **Google Play Billing**: Secure payment processing
- **Feature Comparison**: Clear value proposition for each tier
- **Current Plan Highlighting**: Shows active subscription

#### ShareProfileActivity
- **QR Code Generation**: Uses ZXing library for code creation
- **Multiple Sharing Options**: Social media, messaging, email
- **Profile Preview**: Shows shareable content preview
- **Link Management**: Generates and manages shareable URLs

### Security & Privacy

#### Data Protection
- **Local Storage Encryption**: Sensitive data encrypted using Android Keystore
- **Session Management**: Secure token-based authentication
- **Privacy Controls**: User control over shared information
- **Data Minimization**: Only collect necessary user data

#### Sharing Privacy
- **Opt-in Sharing**: Users must explicitly enable profile sharing
- **Selective Information**: Control what data is included in shared profiles
- **Temporary Links**: Option for time-limited sharing links
- **Revocation**: Ability to disable sharing at any time

## Modern Android Development Practices

### 1. Material Design 3 Implementation
- **Dynamic Color**: Adapts to user's system theme
- **Motion & Animation**: Smooth transitions and micro-interactions
- **Typography Scale**: Consistent text styling throughout the app
- **Component Library**: Latest Material Design components

### 2. Performance Optimization
- **Lazy Loading**: Efficient data loading strategies
- **Image Optimization**: Proper image caching and compression
- **Memory Management**: Proper lifecycle management
- **Background Processing**: Efficient handling of long-running tasks

### 3. Accessibility Features
- **Content Descriptions**: Proper accessibility labels
- **Touch Target Sizes**: Minimum 48dp touch targets
- **Color Contrast**: WCAG compliant color schemes
- **Screen Reader Support**: Compatible with TalkBack

### 4. Testing Strategy
- **Unit Tests**: Core business logic testing
- **Integration Tests**: Feature interaction testing
- **UI Tests**: User interface automation testing
- **Performance Tests**: Memory and CPU usage monitoring

## LLM Integration Opportunities

### Current Implementation
The app currently uses mock data and simulated API calls. Here are opportunities for LLM integration:

### 1. Intelligent Quiz Generation
```java
// Potential LLM integration for dynamic quiz creation
public class LLMQuizGenerator {
    public Quiz generatePersonalizedQuiz(User user, String topic, int difficulty) {
        // Use LLM to create questions based on:
        // - User's learning history
        // - Performance patterns
        // - Interest areas
        // - Difficulty progression
    }
}
```

### 2. Adaptive Learning Paths
- **Personalized Recommendations**: LLM analyzes user performance to suggest optimal learning paths
- **Difficulty Adjustment**: Dynamic question difficulty based on user's current skill level
- **Content Curation**: AI-powered selection of relevant topics and materials

### 3. Intelligent Feedback System
- **Detailed Explanations**: LLM-generated explanations for incorrect answers
- **Learning Insights**: AI analysis of learning patterns and suggestions for improvement
- **Progress Predictions**: Forecasting user's learning trajectory

### 4. Natural Language Processing
- **Voice Interactions**: Voice-based quiz taking and navigation
- **Question Understanding**: Better interpretation of user queries and responses
- **Content Analysis**: Automatic categorization and tagging of learning materials

### 5. Chatbot Integration
```java
public class LearningAssistant {
    public String getPersonalizedHelp(String userQuery, User userContext) {
        // LLM-powered learning assistant that can:
        // - Answer questions about topics
        // - Provide study recommendations
        // - Explain complex concepts
        // - Motivate and encourage learning
    }
}
```

## Future Enhancements

### Phase 1: Advanced Analytics
- **Learning Analytics Dashboard**: Comprehensive performance insights
- **Predictive Modeling**: AI-powered learning outcome predictions
- **Comparative Analysis**: Benchmarking against peer groups

### Phase 2: Social Learning
- **Study Groups**: Collaborative learning features
- **Peer Challenges**: Competitive quiz challenges
- **Knowledge Sharing**: User-generated content platform

### Phase 3: Advanced AI Integration
- **Adaptive Content**: Real-time content adjustment based on performance
- **Emotional Intelligence**: Mood-aware learning recommendations
- **Multi-modal Learning**: Support for different learning styles

## Installation & Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 34
- Java 11 or later
- Google Play Console account (for billing features)

### Setup Instructions
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle dependencies
4. Configure Google Play Billing (for production)
5. Build and run on device/emulator

### Configuration
- Update `strings.xml` with your app-specific content
- Configure billing product IDs in Google Play Console
- Set up proper signing configuration for release builds

## Contributing

This project follows modern Android development best practices and is designed for educational purposes as part of the SIT708 Mobile Application Development course at Deakin University.

## License

This project is developed for educational purposes as part of university coursework.

---

**Developer**: Krish Moradiya (223880281)  
**Course**: SIT708 Mobile Application Development  
**Institution**: Deakin University  
**Semester**: T1 2025 