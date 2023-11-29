package com.example.smarthome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUp extends AppCompatActivity {

    SessionManager sessionManager;
    private FirebaseAuth fAuth;

    private EditText email,password;
    Uri imageUri;
    ImageView imageView;
    private FirebaseDatabase DB;
    private Uri mImageUri;
    private DatabaseReference mDatabaseRef;
    String idImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        sessionManager=new SessionManager(this);

        DatabaseReference  reference = FirebaseDatabase.getInstance().getReference("users");
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.passwordLogin);
        Button signup = (Button) findViewById(R.id.buttonSignUp);
        TextView loginText =(TextView)findViewById(R.id.loginText);

        fAuth = FirebaseAuth.getInstance();
        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), Principal.class));
            finish();
        }
        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), login.class));
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_email = email.getText().toString().trim();
                String m_password = password.getText().toString().trim();


                if (TextUtils.isEmpty(m_email)) {
                    email.setError("Email is Required.");
                    return;
                }

                if (TextUtils.isEmpty(m_password)) {
                    password.setError("Password is Required.");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Password Must be >= 6 Characters");
                    return;
                }
                DB= FirebaseDatabase.getInstance();
                DatabaseReference reference= DB.getReference("users");
                fAuth.createUserWithEmailAndPassword(m_email, m_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = fAuth.getCurrentUser();
                            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    sessionManager.createSession(fAuth.getUid(),m_email);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","On failur:Eamil not sent"+e.getMessage());
                                }
                            });
                            Toast.makeText(SignUp.this, "User created", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUp.this, "Error" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(getApplicationContext(), information.class));
                        finish();
                    }

                });
            }

        });

    }
}