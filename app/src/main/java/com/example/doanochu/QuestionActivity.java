package com.example.doanochu;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    String playerName;
    TextView hintTextView;
    TextView questionTextView;
    EditText answerEditText;
    Button submitButton;

    List<String> questionList = new ArrayList<>();  // Danh sách các câu hỏi
    int currentQuestionIndex = 0;  // Biến đếm câu hỏi hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Nhận tên người chơi từ Intent
        playerName = getIntent().getStringExtra("player_name");

        // Khởi tạo TextView cho gợi ý và câu hỏi
        hintTextView = findViewById(R.id.hintTextView);
        questionTextView = findViewById(R.id.questionTextView);
        answerEditText = findViewById(R.id.answerEditText);  // Ô nhập câu trả lời
        submitButton = findViewById(R.id.submitButton);  // Nút xác nhận

        // Khởi tạo DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        Cursor c = null;
        SQLiteDatabase database = null;
        try {
            // Mở cơ sở dữ liệu
            database = dbHelper.openDatabase();

            // Truy vấn bảng table_question
            Cursor cursor = database.query("table_question", null, null, null, null, null, null);

            // Kiểm tra nếu có dữ liệu trả về
            if (cursor != null && cursor.moveToFirst()) {
                // Lấy chỉ số của cột 'question'
                int questionIndex = cursor.getColumnIndex("question");

                // Kiểm tra nếu cột tồn tại
                if (questionIndex != -1) {
                    // Lưu tất cả câu hỏi vào danh sách
                    do {
                        String question = cursor.getString(questionIndex);
                        questionList.add(question);  // Thêm câu hỏi vào danh sách
                    } while (cursor.moveToNext());

                    // Hiển thị câu hỏi đầu tiên
                    displayCurrentQuestion();
                }
            }

        } catch (Exception e) {
            Log.e("QuestionActivity", "Error retrieving data from database", e);
        } finally {
            if (c != null) {
                c.close();
            }
            if (database != null) {
                dbHelper.closeDatabase();
            }
        }

        // Xử lý sự kiện khi nhấn nút submit
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });
    }

    // Hàm hiển thị câu hỏi hiện tại
    private void displayCurrentQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            // Hiển thị câu hỏi hiện tại
            questionTextView.setText(questionList.get(currentQuestionIndex));
        } else {
            // Nếu đã hết câu hỏi
            questionTextView.setText("Chúc mừng! Bạn đã hoàn thành tất cả các câu hỏi.");
        }
    }


    private void checkAnswer() {
        String userAnswer = answerEditText.getText().toString().trim();
        String currentQuestion = questionList.get(currentQuestionIndex);

        // Khởi tạo DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase database = null;
        Cursor cursor = null;
        String correctAnswer = null;

        try {
            // Mở cơ sở dữ liệu
            database = dbHelper.openDatabase();

            cursor = database.query("table_question", new String[]{"answer"}, "question = ?", new String[]{currentQuestion}, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                correctAnswer = cursor.getString(cursor.getColumnIndex("answer"));
            }
        } catch (Exception e) {
            Log.e("QuestionActivity", "Error retrieving answer from database", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (database != null) {
                dbHelper.closeDatabase();
            }
        }

        if (correctAnswer != null && userAnswer.equalsIgnoreCase(correctAnswer)) {
            Toast.makeText(QuestionActivity.this, "Đúng rồi!", Toast.LENGTH_SHORT).show();
            currentQuestionIndex++;
            displayCurrentQuestion();
        } else {
            Toast.makeText(QuestionActivity.this, "Sai rồi! Thử lại.", Toast.LENGTH_SHORT).show();
        }
        answerEditText.setText("");
    }
}
