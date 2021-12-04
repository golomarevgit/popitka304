package com.example.kino;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

public class SignUpActivity extends AppCompatActivity {
    Button regbutton, logbutton;
    EditText imya, fam, mail, pass, passtrue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mail = findViewById(R.id.mail);
        imya = findViewById(R.id.imya);
        fam = findViewById(R.id.fam);
        pass = findViewById(R.id.pass);
        passtrue = findViewById(R.id.passtrue);
        regbutton = findViewById(R.id.regbutton);
        logbutton = findViewById(R.id.logbutton);

        regbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mail.getText().toString()) ||
                        TextUtils.isEmpty(pass.getText().toString()) ||
                        TextUtils.isEmpty(fam.getText().toString()) ||
                        TextUtils.isEmpty(imya.getText().toString()) ||
                        TextUtils.isEmpty(passtrue.getText().toString())) {
                    ShowAlertDialogWindow("Есть пустые поля!");
                }
                else if (!pass.getText().toString().equals(passtrue.getText().toString()))
                {
                    ShowAlertDialogWindow("Пароли не совпадают");
                }

                else {
                    registerUser();
                }
            }
        });
        logbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private boolean mailValid(String mail)
    {
        Pattern mailPattern = Pattern.compile("a-z.+@[a-z]+\\.[a-z]");
        Matcher mailMatcher = mailPattern.matcher(mail);
        return mailMatcher.matches();
    }



    public void registerUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(mail.getText().toString());
        registerRequest.setFirstname(imya.getText().toString());
        registerRequest.setLastname(fam.getText().toString());
        registerRequest.setPassword(pass.getText().toString());

        Call<RegisterResponse> registerResponseCall = ApiClient.getRegister().registerUser(registerRequest); //Вызов регистрации
        registerResponseCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, retrofit2.Response<RegisterResponse> response) {
                if (response.isSuccessful()) {
                    String message = "Все ок...";
                    Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    String message = "Что-то пошло не так...";
                    Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            public void onFailure(Call<RegisterResponse> call, Throwable throwable) {
                String message = "Регистрация прошла успешно";
                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignUpActivity.this,SignInActivity.class));
            }
        });
    }


    public void ShowAlertDialogWindow(String s) {
        AlertDialog alertDialog = new AlertDialog.Builder(SignUpActivity.this).setMessage(s).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).create();
        alertDialog.show();
    }
}


