package com.example.quizapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private int currentQuestionIndex = 0;
    private int score = 0;
    private QuizQuestion[] quizQuestions; // You'll need to define your quiz question data structure.

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_question);
// Initialize your quiz questions array here.


        // Initialize your quiz questions array here.
        QuizQuestion question1 = new QuizQuestion(
                "What does XML stand for in Android development?",
                new String[]{"eXtensible Markup Language", "Android eXtensible Markup Language", "eXplicit Markup Language", "Extended Markup Language"},
                0
        );

        QuizQuestion question2 = new QuizQuestion(
                "Which component is the building block of Android user interfaces?",
                new String[]{"Activity", "Service", "Broadcast Receiver", "Content Provider"},
                0
        );

        QuizQuestion question3 = new QuizQuestion(
                "Which method is called when an Android activity is first created?",
                new String[]{"onCreate()", "onStart()", "onResume()", "onInitialize()"},
                0
        );

        QuizQuestion question4 = new QuizQuestion(
                "What is an Android Intent used for?",
                new String[]{"To start a new activity or service", "To define user interface layouts", "To store data in a SQLite database", "To handle user gestures"},
                0
        );

        QuizQuestion question5 = new QuizQuestion(
                "Which file defines the UI components and layout for an Android activity?",
                new String[]{"AndroidManifest.xml", "strings.xml", "activity_main.xml", "layout.xml"},
                2
        );

        QuizQuestion question6 = new QuizQuestion(
                "What is the purpose of the AndroidManifest.xml file?",
                new String[]{"It contains the app's source code", "It defines the app's user interface", "It lists the app's permissions and components", "It stores user data"},
                2
        );

        QuizQuestion question7 = new QuizQuestion(
                "Which programming language is primarily used for Android app development?",
                new String[]{"Java", "Python", "C++", "JavaScript"},
                0
        );

        QuizQuestion question8 = new QuizQuestion(
                "What is the main function of a Content Provider in Android?",
                new String[]{"To provide content to other apps", "To display advertisements", "To manage app permissions", "To handle user authentication"},
                0
        );

        QuizQuestion question9 = new QuizQuestion(
                "Which Android component is used to perform background tasks without a user interface?",
                new String[]{"Service", "Activity", "Broadcast Receiver", "Content Provider"},
                0
        );

        QuizQuestion question10 = new QuizQuestion(
                "What is the purpose of the Gradle build system in Android development?",
                new String[]{"To compile and build the Android app", "To design user interfaces", "To test the app on physical devices", "To deploy the app to the Google Play Store"},
                0
        );
        quizQuestions = new QuizQuestion[]{
                question1, question2, question3, question4, question5,
                question6, question7, question8, question9, question10
        };
        questionTextView = findViewById(R.id.question_text_view);
        optionsRadioGroup = findViewById(R.id.options_radio_group);
        nextButton = findViewById(R.id.next_button);

        showQuestion(currentQuestionIndex);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if an option is selected
                int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
                if (selectedOptionId == -1) {
                    Toast.makeText(MainActivity.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedOption = findViewById(selectedOptionId);
                int selectedOptionIndex = optionsRadioGroup.indexOfChild(selectedOption);

                if (selectedOptionIndex == quizQuestions[currentQuestionIndex].getCorrectOption()) {
                    score++;
                }

                currentQuestionIndex++;
                if (currentQuestionIndex < quizQuestions.length) {
                    showQuestion(currentQuestionIndex);
                } else {
                    showResults();
                }
            }
        });
    }

    private void showQuestion(int questionIndex) {
        QuizQuestion question = quizQuestions[questionIndex];
        questionTextView.setText(question.getQuestion());

        optionsRadioGroup.removeAllViews();
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            optionsRadioGroup.addView(radioButton);
        }

        optionsRadioGroup.clearCheck();
    }

    private void showResults() {
        setContentView(R.layout.quiz_results); // Display the results screen layout.
        TextView resultsTextView = findViewById(R.id.results_text_view);
        TextView scoreTextView = findViewById(R.id.score_text_view);

        resultsTextView.setText("Quiz Completed!");
        scoreTextView.setText("Your Score: " + score + "/" + quizQuestions.length);
    }
}
