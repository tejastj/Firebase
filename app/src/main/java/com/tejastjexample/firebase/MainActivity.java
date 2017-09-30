package com.tejastjexample.firebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText mailreg;
    EditText passreg;
    Button signupreg;
    TextView textreg;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mailreg = (EditText)findViewById(R.id.mailreg);
        passreg = (EditText)findViewById(R.id.passreg);
        signupreg = (Button)findViewById(R.id.signupreg);
        textreg = (TextView)findViewById(R.id.textreg);
        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(getApplicationContext() , profileActivity.class));
        }
        signupreg.setOnClickListener(this);
        textreg.setOnClickListener(this);

    }
    public void registerUser(){
        String email = mailreg.getText().toString().trim();
        String pass = passreg.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this,"Enter Email Id to Register ",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(this,"Enter Password to register", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Toast.makeText(MainActivity.this,"Registered Succesfully",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),profileActivity.class));
                        }else {
                            Toast.makeText(MainActivity.this,"Did not Registered , Please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View view) {
        if(view == signupreg){
            registerUser();
        }
        if(view == textreg){
            Toast.makeText(this,"Login activity will open",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, loginActivity.class));
        }

    }
}
