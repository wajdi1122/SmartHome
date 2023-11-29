package com.example.smarthome;

import static android.service.controls.ControlsProviderService.TAG;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Principal extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int PICK_IMAGE = 100;
    private FirebaseAuth fAuth;
    SessionManager sessionManager;
    public Uri imageUri;
    ImageView imageView;
    private EditText fullname,phone,CIN;
    private FirebaseDatabase DB;

    private DatabaseReference databaseReferenceStorege;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        sessionManager=new SessionManager(this);
        fAuth = FirebaseAuth.getInstance();
        Button out =(Button) findViewById (R.id.bntnout);
        Button next=(Button) findViewById (R.id.modifer_save);

        fullname=(EditText)findViewById(R.id.modifier_Fullname);
        phone=(EditText)findViewById(R.id.modifier_Phone);
        CIN=(EditText)findViewById(R.id.modifier_CIN);
        fAuth.getCurrentUser();
        DB= FirebaseDatabase.getInstance();
        DatabaseReference reference= DB.getReference("users");
        ValueEventListener valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and whenever data at this location is updated.

                // Get the data as a YourDataModel object
                Users data = dataSnapshot.getValue(Users.class);

                // Do something with the data
                if (data != null) {
                    // Handle the data
                    fullname.setText(data.getFirstName());
                    CIN.setText(data.getCin());
                    phone.setText(data.getPhone());
                    // ...
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id= sessionManager.sharedPreferences.getString("USERNAME","username");
                reference.child(id).child("ID").child(id);
                reference.child(fAuth.getCurrentUser().getUid()).child("firstName").setValue(fullname.getText().toString());
                reference.child(fAuth.getCurrentUser().getUid()).child("phone").setValue(phone.getText().toString());
                reference.child(fAuth.getCurrentUser().getUid()).child("cin").setValue(CIN.getText().toString());
                startActivity(new Intent(getApplicationContext(), Principal.class));
                finish();
            }
        });
        out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent=new Intent(Principal.this,login.class);
                startActivity(intent);
                finish();            }
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