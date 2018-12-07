package com.example.androidl.prekart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.androidl.prekart.Common.Common;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.example.androidl.prekart.Model.User;

public class SignIn extends AppCompatActivity {

    EditText editPhone,editPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        editPassword = (MaterialEditText)findViewById(R.id.editPassword);
        editPhone = (MaterialEditText)findViewById(R.id.editPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //Init firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please Wait");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                     @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //checkif user exists in database
                        if(dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            // get user information if it exists
                            mDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            Log.v("SignIn.this","CHECK POINT REACHED");

                           user.setPhone(editPhone.getText().toString());//set phone

                            if (user.getPassword().equals(editPassword.getText().toString()))
                            {

                                Toast.makeText(SignIn.this, "Sign in successFull!!", Toast.LENGTH_SHORT).show();
                               Common.currentUser = user;
                                Intent home = new Intent(SignIn.this,Home.class);


                               startActivity(home);
                                finish();
                            }
                            else{
                                Toast.makeText(SignIn.this, "Wrong Password!!", Toast.LENGTH_SHORT).show();
                                }
                        }
                        else
                        { mDialog.dismiss();
                            Toast.makeText(SignIn.this, "No User Record Found!!", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}
