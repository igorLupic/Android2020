package com.example.pmsumail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pmsumail.model.Account;
import com.example.pmsumail.service.AccountService;
import com.example.pmsumail.service.ServiceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btnLogin;
    private SharedPreferences sharedPreferences;
    private AccountService accountService;
    private EditText editUsername;
    private EditText editPassword;
    public static final String Name = "name";
    public static final String MyPres = "MyPre";
    public static final String Username = "usernameKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editUsername = findViewById(R.id.username);
        editPassword = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        accountService = ServiceUtils.accountService;

        sharedPreferences = getSharedPreferences(MyPres, Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editUsername.getText().toString();
                String password = editPassword.getText().toString();

                SharedPreferences.Editor editor = sharedPreferences.edit();
               if(validate(username, password)){
                    editor.putString(Username, username);
                    editor.commit();
                    doLogin(username, password);
                }else{
                   editor.clear().commit();
                   Toast.makeText(getBaseContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public boolean validate(String username, String password){
        if(username == null || username.trim().length() == 0){
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(password == null || password.trim().length() == 0) {
            Toast.makeText(this, "Wrong username or password", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void doLogin(final String username, final String password){
        Call<Account> call = accountService.login(username, password);
        call.enqueue(new Callback<Account>() {

            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                Account account = response.body();
                if(account == null){
                    Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_LONG).show();
                    return;
                }
                if(username.equals(account.getUsername())&& password.equals(account.getPassword() )){

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(Name, account.getUsername());
                    editor.commit();

                    Intent intent = new Intent(LoginActivity.this, EmailsActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(LoginActivity.this, "Wrong username or password", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Greska", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //do nothing
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
