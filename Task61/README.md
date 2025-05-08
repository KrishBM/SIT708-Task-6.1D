# Personalized Learning Experience App

A mobile application designed to provide personalized learning experiences through adaptive quizzes and assessments. This Android app allows users to select their interests and take quizzes tailored to those topics, with progress tracking and skill achievement features.

## Features

- **User Authentication**: Secure login and registration system
- **Interest-Based Personalization**: Select from various topics of interest
- **Adaptive Quizzes**: Take quizzes generated based on your selected interests
- **Progress Tracking**: Track completed quizzes and skills achieved
- **Immediate Feedback**: Receive instant feedback on quiz answers
- **Skill Achievement System**: Mark skills as achieved when quizzes are completed successfully

## Implementation Details

### Architecture

The app follows a modular architecture with separation of concerns:
- **Model**: Data classes for User, Quiz, Question
- **Activities**: Screen-specific logic and UI management
- **Adapters**: RecyclerView adapters for lists
- **Utils**: Helper classes like SessionManager
- **API**: Integration with LLM services

### Technologies Used

- **Java**: Core programming language
- **Android SDK**: Native Android development
- **Material Design**: Modern UI components and patterns
- **SharedPreferences**: Local storage for user data and preferences
- **RecyclerView**: Display of dynamic content lists
- **Vector Drawables**: Scalable icons and visual elements

### Key Components

1. **Interest Selection System**
   - Material Design Chips for interactive selection
   - User preference tracking

2. **Quiz Generation**
   - Topic-specific quiz content
   - Multiple-choice question formats
   - Adaptive difficulty

3. **Progress Tracking**
   - Visual indicators for completed quizzes
   - Dynamic task count updates
   - Achievement system

4. **User Session Management**
   - Secure login/logout functionality
   - Local data persistence

## Setup and Installation

1. Clone the repository
   ```
   git clone https://github.com/KrishBM/SIT708-Task-6.1D.git
   ```

2. Open the project in Android Studio

3. Build and run on an emulator or physical device
   - Minimum SDK: 24 (Android 7.0)
   - Target SDK: 35

## Future Enhancements

- Integration with online learning resources
- Social features and peer learning
- More advanced adaptive assessment algorithms
- Expanded topic libraries
- Performance analytics and learning insights

## Contributors

- Krish Moradiya (223880281)

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgements

- Material Design Components for Android
- Android Developers Documentation
- SIT708 Mobile Application Development Course at Deakin University 