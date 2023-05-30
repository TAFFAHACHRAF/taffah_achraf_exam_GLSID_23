package com.example.taffah_achraf_GLSID_23;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ajincodew.R;

public class LoginActivity extends AppCompatActivity {
    private Button login;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        email = findViewById(R.id.emailLogin);
        password = findViewById(R.id.passLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredEmail = email.getText().toString();
                String enteredPassword = password.getText().toString();

                saveLoginCredentials(enteredEmail, enteredPassword);

                Toast.makeText(LoginActivity.this, "Login!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, IpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveLoginCredentials(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.apply();
    }
}
