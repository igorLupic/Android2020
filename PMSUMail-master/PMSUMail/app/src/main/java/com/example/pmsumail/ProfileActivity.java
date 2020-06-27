package com.example.pmsumail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.model.Account;
import com.example.pmsumail.service.AccountService;
import com.example.pmsumail.service.ServiceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileActivity extends AppCompatActivity {

    private Account account = new Account();
    private NavigationView navigation;
    private TextView textViewUsername;
    private TextView textViewEmail;
    private TextView textViewSmtp;
    private TextView textViewPop3;
    private AccountService accountService;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        accountService = ServiceUtils.accountService;
        initView();


        Toolbar toolbar = findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initView() {
        textViewUsername = findViewById(R.id.text_view_username);
        textViewEmail = findViewById(R.id.email_text_view);
        textViewSmtp = findViewById(R.id.smtp_text_view);
        textViewPop3 = findViewById(R.id.pop3_text_view);
        SharedPreferences preferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);
        String username = preferences.getString(LoginActivity.Username, "DEFAULT");

        Call<Account> call = accountService.getByUsername(username);
        call.enqueue(new Callback<Account>() {
            @Override
            public void onResponse(Call<Account> call, Response<Account> response) {
                if (response.body() == null) return;
                textViewUsername.setText("Username: " + response.body().getUsername());
                textViewEmail.setText("Password: " + response.body().getPassword());
                textViewSmtp.setText("Smtp: " + response.body().getSmtp());
                textViewPop3.setText("Pop3: " + response.body().getPop3());
            }

            @Override
            public void onFailure(Call<Account> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                Intent i = new Intent(this, LoginActivity.class);
                Toast.makeText(getBaseContext(), "Log out" , Toast.LENGTH_SHORT ).show();
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
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
