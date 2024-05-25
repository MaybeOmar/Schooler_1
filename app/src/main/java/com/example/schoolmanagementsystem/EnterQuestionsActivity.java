package com.example.schoolmanagementsystem;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EnterQuestionsActivity extends AppCompatActivity {

    private RadioGroup radioGroupType;
    private RadioButton radioButtonMCQ;
    private RadioButton radioButtonEssay;
    private EditText editTextQuestion;
    private EditText editTextOptionA;
    private EditText editTextOptionB;
    private EditText editTextOptionC;
    private EditText editTextOptionD;
    private Button buttonSaveQuestion;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_questions);

        radioGroupType = findViewById(R.id.radio_group_type);
        radioButtonMCQ = findViewById(R.id.radio_button_mcq);
        radioButtonEssay = findViewById(R.id.radio_button_essay);
        editTextQuestion = findViewById(R.id.edit_text_question);
        editTextOptionA = findViewById(R.id.edit_text_option_a);
        editTextOptionB = findViewById(R.id.edit_text_option_b);
        editTextOptionC = findViewById(R.id.edit_text_option_c);
        editTextOptionD = findViewById(R.id.edit_text_option_d);
        buttonSaveQuestion = findViewById(R.id.button_save_question);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        radioGroupType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_mcq) {
                    showMCQFields();
                } else if (checkedId == R.id.radio_button_essay) {
                    showEssayField();
                }
            }
        });

        buttonSaveQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveQuestion();
            }
        });
    }

    private void showMCQFields() {
        editTextQuestion.setVisibility(View.VISIBLE);
        editTextOptionA.setVisibility(View.VISIBLE);
        editTextOptionB.setVisibility(View.VISIBLE);
        editTextOptionC.setVisibility(View.VISIBLE);
        editTextOptionD.setVisibility(View.VISIBLE);
    }

    private void showEssayField() {
        editTextQuestion.setVisibility(View.VISIBLE);
        editTextOptionA.setVisibility(View.GONE);
        editTextOptionB.setVisibility(View.GONE);
        editTextOptionC.setVisibility(View.GONE);
        editTextOptionD.setVisibility(View.GONE);
    }

    private void saveQuestion() {
        // Get the question text from the EditText
        String questionText = editTextQuestion.getText().toString().trim();

        // Get the question type (MCQ or Essay)
        String questionType;
        if (radioButtonMCQ.isChecked()) {
            questionType = "MCQ";
        } else {
            questionType = "Essay";
        }

        // Create a map to store the question details
        Map<String, Object> question = new HashMap<>();
        question.put("question", questionText);
        question.put("type", questionType);

        // If the question is MCQ, add options to the map
        if (questionType.equals("MCQ")) {
            question.put("optionA", editTextOptionA.getText().toString().trim());
            question.put("optionB", editTextOptionB.getText().toString().trim());
            question.put("optionC", editTextOptionC.getText().toString().trim());
            question.put("optionD", editTextOptionD.getText().toString().trim());
        }

        // Get the current user
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // Reference to the 'questions' collection under the current user's document
            DocumentReference questionRef = db.collection("User").document(userId).collection("questions").document();
            // Add the question document to Firestore
            questionRef.set(question)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EnterQuestionsActivity.this, "Question saved successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(EnterQuestionsActivity.this, "Error saving question: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.e("EnterQuestionsActivity", "Error saving question", e);
                        }
                    });
        } else {
            Toast.makeText(EnterQuestionsActivity.this, "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }
}
