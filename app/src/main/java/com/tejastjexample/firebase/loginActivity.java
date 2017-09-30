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

public class loginActivity extends AppCompatActivity implements View.OnClickListener{
    EditText mailsign;
    EditText passsign;
    Button signinsign;
    TextView textsign;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mailsign = (EditText)findViewById(R.id.mailsign);
        passsign = (EditText)findViewById(R.id.passsign);
        signinsign = (Button)findViewById(R.id.signinsign);
        textsign = (TextView)findViewById(R.id.textsign);
        progressDialog =  new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!= null){
            finish();
            startActivity(new Intent(getApplicationContext() , profileActivity.class));
        }
        signinsign.setOnClickListener(this);
        textsign.setOnClickListener(this);

    }
    public void userlogin(){
        String email = mailsign.getText().toString().trim();
        String pass = passsign.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please Enter Email id",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Password is must",Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Logging In...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(),profileActivity.class));
                        }else{
                            Toast.makeText(getApplicationContext(),"oops! Log In failed...",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
    @Override
    public void onClick(View view) {
        if(view == signinsign){
            userlogin();
        }
        if(view == textsign){
            startActivity(new Intent(this,MainActivity.class));
        }

    }
}
