package com.example.apptodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apptodo.MainActivity;
import com.example.apptodo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    protected Button btnSignup;
    protected EditText etEmailSignup;
    protected EditText etPasswordSignup;

    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        etEmailSignup = (EditText) findViewById(R.id.et_signup_email);
        etPasswordSignup = (EditText) findViewById(R.id.et_signup_password);

        mFirebaseAuth = FirebaseAuth.getInstance();

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmailSignup.getText().toString();
                String password = etPasswordSignup.getText().toString();

                email=email.trim();
                password=password.trim();

                if (email.isEmpty() || password.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
                    builder.setMessage("Enter Email or Password to signing up.").setTitle("Reminder").setPositiveButton("OK",null);

                    AlertDialog dialog=builder.create();
                    dialog.show();
                }

                else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Intent intent = new Intent(SignupActivity.this, AddToDoList.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }

                            else {
                                Toast.makeText(SignupActivity.this, "User exist. Try with other email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
