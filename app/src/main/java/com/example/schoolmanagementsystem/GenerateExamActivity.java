package com.example.schoolmanagementsystem;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateExamActivity extends AppCompatActivity {

    private EditText editTextNumberOfQuestions;
    private Button buttonGenerateExam;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_generator);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();

        editTextNumberOfQuestions = findViewById(R.id.edit_text_number_of_questions);
        buttonGenerateExam = findViewById(R.id.button_generate_exam);

        buttonGenerateExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateAndUploadExamDocx();
            }
        });
    }

    private void generateAndUploadExamDocx() {
        String numberOfQuestionsText = editTextNumberOfQuestions.getText().toString();
        if (numberOfQuestionsText.isEmpty()) {
            Log.e("GenerateExamActivity", "Number of questions is empty");
            return;
        }

        int numberOfQuestions = Integer.parseInt(numberOfQuestionsText);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Log.e("GenerateExamActivity", "User not authenticated");
            return;
        }

        String userId = currentUser.getUid();
        db.collection("User").document(userId).collection("questions")
                .limit(numberOfQuestions)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        XWPFDocument document = new XWPFDocument();
                        FileOutputStream fos = null;

                        try {
                            File file = File.createTempFile("exam", ".docx", getCacheDir());
                            fos = new FileOutputStream(file);

                            // Add the number of questions at the beginning
                            XWPFParagraph para = document.createParagraph();
                            XWPFRun run = para.createRun();
                            run.setText("Number of questions: " + queryDocumentSnapshots.size());
                            run.setBold(true);
                            run.addBreak();

                            List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot doc : documents) {
                                String question = doc.getString("question");
                                String type = doc.getString("type");

                                XWPFParagraph questionPara = document.createParagraph();
                                XWPFRun questionRun = questionPara.createRun();
                                questionRun.setText("Question: " + question);

                                if ("MCQ".equals(type)) {
                                    questionRun.addBreak();
                                    questionRun.setText("Option A: " + doc.getString("optionA"));
                                    questionRun.addBreak();
                                    questionRun.setText("Option B: " + doc.getString("optionB"));
                                    questionRun.addBreak();
                                    questionRun.setText("Option C: " + doc.getString("optionC"));
                                    questionRun.addBreak();
                                    questionRun.setText("Option D: " + doc.getString("optionD"));
                                }

                                questionRun.addBreak();
                                questionRun.addBreak(); // Add some space between questions
                            }

                            document.write(fos);
                            fos.close();

                            // Upload the DOCX file to Firebase Cloud Storage
                            uploadDocxToStorage(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (fos != null) fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("GenerateExamActivity", "Error fetching questions from Firestore: " + e.getMessage());
                    }
                });
    }

    private void uploadDocxToStorage(File docxFile) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            StorageReference docxRef = storage.getReference().child("docs").child("exam_" + userId + ".docx");

            UploadTask uploadTask = docxRef.putFile(Uri.fromFile(docxFile));
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    docxRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            saveDownloadUrlToFirestore(downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("GenerateExamActivity", "Error uploading DOCX: " + e.getMessage());
                }
            });
        }
    }

    private void saveDownloadUrlToFirestore(String downloadUrl) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            Map<String, Object> data = new HashMap<>();
            data.put("examUrl", downloadUrl);

            DocumentReference userRef = db.collection("User").document(userId);
            userRef.update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(GenerateExamActivity.this, "Exam DOCX uploaded successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("GenerateExamActivity", "Error updating Firestore: " + e.getMessage());
                }
            });
        } else {
            Log.e("GenerateExamActivity", "Current user is null");
        }
    }
}
