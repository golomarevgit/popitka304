package com.example.kino;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    EditText maillog, passlog;
    Button log, reglog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        log = findViewById(R.id.log);
        maillog = findViewById(R.id.maillog);
        passlog=findViewById(R.id.passlog);
        reglog=findViewById(R.id.reglog);

        reglog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
    }

    private boolean emailvalid(String mail)
    {
        Pattern emailPattern = Pattern.compile("a-z.+@[a-z]+\\.[a-z]");
        Matcher emailMatcher = emailPattern.matcher(mail);
        return emailMatcher.matches();
    }
    private void loginUser(){
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(maillog.getText().toString());
        loginRequest.setPassword(passlog.getText().toString());

        Call<LoginResponse> loginResponseCall = ApiClient.getLogin().loginUser(loginRequest);
        loginResponseCall.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(SignInActivity.this, "Вы успешно вошли!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    String message ="Что-то пошло не так...";
                    Toast.makeText(SignInActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(SignInActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void ShowAlertDialogWindow(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(SignInActivity.this).setMessage(s).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();
        alertDialog.show();
    }
}