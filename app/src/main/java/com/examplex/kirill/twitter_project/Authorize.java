package com.examplex.kirill.twitter_project;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.examplex.kirill.twitter_project.models.Messages;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import io.realm.Realm;

public class Authorize extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mListner;
    List<String> tasks;

    EditText email, password;
    Button signUp, signIn;
    Realm realm;

    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        initData();

    }

    private void initData() {
        email = (EditText) findViewById(R.id.user_email);
        password = (EditText) findViewById(R.id.user_password);

        signIn = (Button) findViewById(R.id.btn_sign_in);
        signUp = (Button) findViewById(R.id.btn_sign_up);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

        initAuth();
    }

    private void initAuth() {
        mAuth = FirebaseAuth.getInstance();
        mListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {


                    Intent i = new Intent(Authorize.this, MainActivity.class);
                    startActivity(i);
                }
                else
                {

                }
            }
        };

        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null)
        {
            Intent i = new Intent(Authorize.this, MainActivity.class);
            startActivity(i);
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_sign_in:{
                
                singin(email.getText().toString(), password.getText().toString());
                break;
            }
            case R.id.btn_sign_up:{
                signup(email.getText().toString(), password.getText().toString());
                break;
            }
        }
    }

    private void singin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Authorize.this, "Authorize complited"+"", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Authorize.this, MainActivity.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(Authorize.this, "Authorize Error"+"", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                    Toast.makeText(Authorize.this, "Sign Up Successful"+"", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Authorize.this, "Sign Up Unuccessful"+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public long idNextVal (Realm realm)

    {
        Number maxId;
        maxId = (Number) (realm.where(Messages.class).max(Messages.MSG_ID));
        long nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
        return  nextId;

    }
}
