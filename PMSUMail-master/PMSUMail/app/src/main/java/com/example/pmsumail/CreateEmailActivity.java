package com.example.pmsumail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.model.Account;
import com.example.pmsumail.model.Contact;
import com.example.pmsumail.model.Message;
import com.example.pmsumail.model.Tag;
import com.example.pmsumail.model.requestbody.MessageCreateRequestBody;
import com.example.pmsumail.service.MessageService;
import com.example.pmsumail.service.ServiceUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateEmailActivity extends AppCompatActivity {



    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageView btnSend;
    private ImageView btnCancel;
    private EditText tagText;
    private static Tag tag = new Tag();
    private MessageService messageService;
    private static Tag tagBody = new Tag();
    private static Message messageBody;

    public static final String[] CONTACTS = new String[]{"igor@gmail.com", "admin@gmail.com"};
    public static final String[] CCC = new String[]{"cc", "cc1"};
    public static final String[] BCC= new String[]{"bcc", "bcc1"};

    private AutoCompleteTextView to;
    private EditText subject;
    private AutoCompleteTextView  cc;
    private AutoCompleteTextView  bcc;
    private EditText content;
    private SharedPreferences sharedPreferences;

    public CreateEmailActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email);

        Toolbar toolbar = findViewById(R.id.toolbar);

        messageService = ServiceUtils.messageService;
        initView();


        setSupportActionBar(toolbar);
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }


    private void initView() {
        to = (AutoCompleteTextView)findViewById(R.id.send_to);
        subject = findViewById(R.id.subject);
        cc = (AutoCompleteTextView)findViewById(R.id.cc_edit);
        bcc = (AutoCompleteTextView)findViewById(R.id.bcc);
        content = findViewById(R.id.content_edit);

        toolbar = findViewById(R.id.toolbar);
        btnSend = findViewById(R.id.button_one);
        btnCancel = findViewById(R.id.button_two);
        toolbarText = findViewById(R.id.toolbar_text);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);

        btnSend.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
        btnCancel.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));



        ArrayAdapter<String> adapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1,CONTACTS) ;

        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.send_to);
        actv.setThreshold(1);
        actv.setAdapter(adapter);
        actv.setTextColor(Color.BLACK);


        ArrayAdapter<String> adapter1 = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1,CCC) ;

        AutoCompleteTextView accc = (AutoCompleteTextView) findViewById(R.id.cc_edit);
        accc.setThreshold(1);
        accc.setAdapter(adapter1);
        accc.setTextColor(Color.BLACK);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1,BCC) ;

        AutoCompleteTextView abcc = (AutoCompleteTextView) findViewById(R.id.bcc);
        abcc.setThreshold(1);
        abcc.setAdapter(adapter2);
        abcc.setTextColor(Color.BLACK);



        toolbarText.setText("Create message");


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MessageCreateRequestBody messageCreateRequestBody = new MessageCreateRequestBody();





                if(bcc.getText().toString() == null || bcc.getText().toString().length() == 0  ||  cc.getText().toString() == null || cc.getText().toString().length() == 0
                || to.getText().toString() == null || to.getText().toString().length() == 0) {

                    Toast.makeText(CreateEmailActivity.this, "Fields can not be empty", Toast.LENGTH_LONG).show();
                    return;
                }else

                    if(!isValidEmailId(to.getText().toString().trim())){
                    Toast.makeText(CreateEmailActivity.this, "'To' must be valid (example@example.example)", Toast.LENGTH_LONG).show();
                    return;

                }else{
                    messageCreateRequestBody.setBcc(bcc.getText().toString());
                    messageCreateRequestBody.setCc(cc.getText().toString());
                    messageCreateRequestBody.setContent(content.getText().toString());
                    messageCreateRequestBody.setSubject(subject.getText().toString());
                    messageCreateRequestBody.setTo(to.getText().toString());

                    messageCreateRequestBody.setFrom(sharedPreferences.getString(LoginActivity.Username, "User"));
                    messageCreateRequestBody.setDateTime(new Date());
                    messageCreateRequestBody.setMessageTag(21.2);
                    messageCreateRequestBody.setMessageRead(false);
                }


                Call<Message> call = messageService.createMessage(messageCreateRequestBody);
                call.enqueue(new Callback<Message>() {
                    @Override
                    public void onResponse(Call<Message> call, Response<Message> response) {
                        Toast.makeText(CreateEmailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(CreateEmailActivity.this, EmailsActivity.class);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Call<Message> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateEmailActivity.this, EmailsActivity.class);
                startActivity(i);

            }
        });

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
