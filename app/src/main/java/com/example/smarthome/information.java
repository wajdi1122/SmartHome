package com.example.smarthome;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class information extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE = 100;
    private FirebaseAuth fAuth;
    SessionManager sessionManager;
    public Uri imageUri;
    ImageView imageView;
    private TextInputEditText fullname,phone,CIN;
    private FirebaseDatabase DB;
    private Uri mImageUri;
    private DatabaseReference databaseReferenceStorege;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        sessionManager=new SessionManager(this);
        fAuth = FirebaseAuth.getInstance();

        Button next=(Button)findViewById(R.id.button_next);

        fullname=(TextInputEditText)findViewById(R.id.fullname_information);
        phone=(TextInputEditText)findViewById(R.id.Phone_information);
        CIN=(TextInputEditText)findViewById(R.id.carte_CIN);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB = FirebaseDatabase.getInstance();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                String userId = fAuth.getCurrentUser().getUid(); // Use consistent userId
                Users users=new Users(fullname.getText().toString(),CIN.getText().toString(),phone.getText().toString());
                reference.child("users").child(userId).setValue(users);
                startActivity(new Intent(getApplicationContext(), Principal.class));
                finish();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

}
