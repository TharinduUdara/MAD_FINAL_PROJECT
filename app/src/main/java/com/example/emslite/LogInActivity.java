package com.example.emslite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import es.dmoral.toasty.Toasty;

public class LogInActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private Button logInButton;

    private FirebaseAuth firebaseAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        email = findViewById(R.id.logInEmail);
        password = findViewById(R.id.logInPassword);
        logInButton = findViewById(R.id.logInButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getApplicationContext().getSharedPreferences("emsLite", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailValue = email.getText().toString().trim();
                String passwordValue = password.getText().toString().trim();

                if (email == null || email.equals("")) {
                    Toasty.warning(LogInActivity.this, "Please enter Email.", Toast.LENGTH_SHORT, true).show();
                } else if (password == null || password.equals("")) {
                    Toasty.warning(LogInActivity.this, "Please enter Password.", Toast.LENGTH_SHORT, true).show();
                } else {
                    logInButton.setEnabled(false);

                    firebaseAuth.signInWithEmailAndPassword(emailValue, passwordValue)
                        .addOnCompleteListener(LogInActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();

                                    Toasty.success(LogInActivity.this, "Logged in successfully.", Toast.LENGTH_SHORT, true).show();
                                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    logInButton.setEnabled(true);
                                    Toasty.error(LogInActivity.this, "Please check email or password.", Toast.LENGTH_SHORT, true).show();
                                }
                            }
                        }
                    );
                }
            }
        });
    }
}