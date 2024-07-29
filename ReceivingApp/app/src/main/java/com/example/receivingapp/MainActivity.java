package com.example.receivingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMessages;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        textViewMessages = findViewById(R.id.textViewMessages);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("messages");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                StringBuilder messages = new StringBuilder();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String phone = dataSnapshot.child("name").getValue(String.class);
                    String message = dataSnapshot.child("message").getValue(String.class);
                    String timestamp = dataSnapshot.child("timestamp").getValue(String.class);
                    messages.append("From: ").append(phone).append("\n")
                            .append("Message: ").append(message).append("\n")
                            .append("Time: ").append(timestamp).append("\n\n");
                }
                textViewMessages.setText(messages.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Обработка ошибок.
            }
        });

    }
    private String formatTimestamp(String timestamp) {
        long ts = Long.parseLong(timestamp);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        java.util.Date date = new java.util.Date(ts);
        return sdf.format(date);
    }
}