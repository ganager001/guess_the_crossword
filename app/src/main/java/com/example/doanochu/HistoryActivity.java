package com.example.doanochu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableRow.LayoutParams;
import android.util.Log;

public class HistoryActivity extends AppCompatActivity {
    TableLayout tableLayout;
    Button btnBackHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        tableLayout = findViewById(R.id.tableLayout);
        btnBackHistory = findViewById(R.id.btnBackHistory);

        DatabaseHelper dbHelper = new DatabaseHelper(this);

        try {
            SQLiteDatabase database = dbHelper.openDatabase();
            Cursor c = database.query("table_player", null, null, null, null, null, null);

            if (c != null && c.moveToFirst()) {
                do {
                    TableRow row = new TableRow(this);
                    row.setGravity(Gravity.CENTER);
                    LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f);
                    addTextViewToRow(row, c.getString(0), params);
                    addTextViewToRow(row, c.getString(1), params);
                    addTextViewToRow(row, c.getString(2), params);
                    addTextViewToRow(row, c.getString(3), params);

                    tableLayout.addView(row);
                } while (c.moveToNext());
                c.close();
            }
        } catch (Exception e) {
            Log.e("HistoryActivity", "Error retrieving data from database", e);
        } finally {
            dbHelper.closeDatabase();
        }

        btnBackHistory.setOnClickListener(view -> finish());
    }

    private void addTextViewToRow(TableRow row, String text, LayoutParams params) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(8, 8, 8, 8);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(params);
        textView.setBackgroundResource(R.drawable.table_border);
        row.addView(textView);
    }
}
