package com.example.smarthome;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity {
    private FirebaseAuth fAuth;
    EditText mail, password;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sessionManager=new SessionManager(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        mail = findViewById(R.id.mail);
        password = findViewById(R.id.passwordLogin);
        TextView singUP = (TextView) findViewById(R.id.signUpText);
        Button login = (Button) findViewById(R.id.buttonLogin);
        TextView forgot = (TextView) findViewById(R.id.forgotpassword);
        ProgressBar bar = (ProgressBar) findViewById(R.id.progress);
        fAuth = FirebaseAuth.getInstance();
        singUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String m_email = mail.getText().toString().trim();
                String m_password = password.getText().toString().trim();
                if (TextUtils.isEmpty(m_email)) {
                    mail.setError("البريد الالكتروني مطلوب.");
                    return;
                }
                if (TextUtils.isEmpty(m_password)) {
                    password.setError("كلمة المرور مطلوبة.");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Password Must be >= 6 Characters");
                    return;
                }

              /*  if (networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected()) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setMessage("Do You want to close application??");
                    boolean wifi = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                    builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //  Action for 'NO' Button
                                    dialog.cancel();
                                }
                            });
                    //Creating dialog box
                    AlertDialog alert = builder.create();
                    //Setting the title manually
                    alert.setTitle("No Internet!!");
                    alert.show();
                }*/

                fAuth.signInWithEmailAndPassword(m_email, m_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            bar.setVisibility(View.VISIBLE);
                            String ID= fAuth.getCurrentUser().getUid().toString();
                            sessionManager.createSession(ID,m_email);
                            Toast.makeText(login.this, "تم تسجيل الدخول بنجاح", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(login.this, Principal.class);
                            startActivity(new Intent(getApplicationContext(), Principal.class));
                        } else {
                            Toast.makeText(login.this, "خطأ" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("إعادة تعيين كلمة المرور؟");
                passwordResetDialog.setMessage("أدخل بريدك الإلكتروني لتلقي رابط إعادة التعيين.");
                passwordResetDialog.setView(resetMail);

                passwordResetDialog.setPositiveButton("نعم", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String mail = resetMail.getText().toString();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(login.this, "إعادة تعيين الارتباط المرسل إلى بريدك الإلكتروني.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(login.this, "خطأ ! لم يتم إرسال رابط إعادة التعيين" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });

                passwordResetDialog.setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
    }
}