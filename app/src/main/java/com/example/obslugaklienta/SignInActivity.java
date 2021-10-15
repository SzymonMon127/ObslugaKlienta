package com.example.obslugaklienta;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;




import com.google.firebase.auth.FirebaseAuth;



public class SignInActivity extends AppCompatActivity implements View.OnClickListener {




    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);




        firebaseAuth = FirebaseAuth.getInstance();




        mUserNameEditText = findViewById(R.id.sing_in_username_et);
        mPasswordEditText = findViewById(R.id.sing_in_password_et);
        ImageButton mLoginButton = findViewById(R.id.log_in_button);




        mLoginButton.setOnClickListener(this);

    }



    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.log_in_button) {
            tryToSignIn();
        }

    }

    private void tryToSignIn() {

        String UserName = mUserNameEditText.getText().toString();
        String Password = mPasswordEditText.getText().toString();

        boolean hasErrors = false;

        if(TextUtils.isEmpty(UserName))
        {
            hasErrors = true;
            mUserNameEditText.setError("Email nie może być pusty!");
        }
        if(TextUtils.isEmpty(Password))
        {
            hasErrors=true;
            mPasswordEditText.setError("Hasło nie może być puste!");
        }

        if(!hasErrors)
        {
            singIn(UserName, Password);
        }

    }

    private void singIn(String userName, String password) {
        firebaseAuth.signInWithEmailAndPassword(userName, password).addOnCompleteListener(task -> {
            if(task.isSuccessful())
            {
                GoToMain();
            }
            else
            {
                Toast.makeText(SignInActivity.this, "Nieudana próba logowania.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void GoToMain()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }





}
