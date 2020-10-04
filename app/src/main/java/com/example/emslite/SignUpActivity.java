package com.example.emslite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {
    private EditText emailField;
    private EditText passwordField;
    private EditText confirmPasswordField;
    private Button signUpButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailField = findViewById(R.id.signUpEmail);
        passwordField = findViewById(R.id.signUpPassword);
        confirmPasswordField = findViewById(R.id.signUpConfirmPassword);
        signUpButton = findViewById(R.id.signUpButton);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        firebaseAuth = FirebaseAuth.getInstance();

        createUser();
    }

    private void createUser() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailField.getText().toString().trim();
                String password = passwordField.getText().toString().trim();
                String confirmPassword = confirmPasswordField.getText().toString().trim();

                if (email == null || email.equals("")) {
                    Toasty.warning(SignUpActivity.this, "Email is Required.", Toast.LENGTH_SHORT, true).show();
                } else if (password == null || password.equals("")) {
                    Toasty.warning(SignUpActivity.this, "Password is Required.", Toast.LENGTH_SHORT, true).show();
                } else if (password.length() <= 8) {
                    Toasty.warning(SignUpActivity.this, "Password must be more than 8 characters.", Toast.LENGTH_SHORT, true).show();
                } else if (!password.equals(confirmPassword)) {
                    Toasty.error(SignUpActivity.this, "Password is not matching.", Toast.LENGTH_SHORT, true).show();
                } else {
                    signUpButton.setEnabled(false);

                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toasty.success(SignUpActivity.this, "You have signed up successfully!", Toast.LENGTH_SHORT, true).show();

                                Intent intent = new Intent(SignUpActivity.this, WelcomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                signUpButton.setEnabled(true);
                                Toasty.error(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT, true).show();
                            }
                            }
                        }
                    );
                }
            }
        });
    }
}