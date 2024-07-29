package com.example.chatapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextMessage, textViewStatus;
    private Button btnSend;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        editTextName = findViewById(R.id.editTextName);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);
        textViewStatus = findViewById(R.id.textViewStatus);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("messages");

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String name = editTextName.getText().toString().trim();
        String message = editTextMessage.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(message)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
 
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        Log.d("MyApp", "Message saved locally: " + message);

        Map<String, String> messageData = new HashMap<>();
        messageData.put("name", name);
        messageData.put("message", message);
        messageData.put("timestamp", timestamp);

        databaseReference.push().setValue(messageData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("MyApp", "Message sent to Firebase: " + message);
                        Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Failed to send message", Toast.LENGTH_SHORT).show();
                    }
                });
        textViewStatus.setText("Message saved locally: " + message);
    }
}
