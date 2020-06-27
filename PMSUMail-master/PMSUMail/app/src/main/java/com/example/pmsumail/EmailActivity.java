package com.example.pmsumail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.model.Account;
import com.example.pmsumail.model.Attachment;
import com.example.pmsumail.model.Message;
import com.example.pmsumail.model.Tag;
import com.example.pmsumail.model.requestbody.MessageCreateRequestBody;
import com.example.pmsumail.service.AccountService;
import com.example.pmsumail.service.MessageService;
import com.example.pmsumail.service.ServiceUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailActivity extends AppCompatActivity {

    Message message = new Message();
    private Account account = new Account();
    private MessageService messageService;
    private AccountService accountService;
    private SharedPreferences sharedPreferences;
    private List<Tag> tags = new ArrayList<>();
    private List<Attachment> attachments = new ArrayList<>();
    String accountPrefe;


    private ImageView btnSend;
    private ImageView btnCancel;

    private Toolbar toolbar;
    private TextView toolbarText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        Toolbar toolbar = findViewById(R.id.mail_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getParcelable("Message");
        }
        messageService = ServiceUtils.messageService;

        accountService = ServiceUtils.accountService;

        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);

        accountPrefe = sharedPreferences.getString(LoginActivity.Username, "");

        TextView from_view = findViewById(R.id.from_view);
        TextView to_view = findViewById(R.id.to_view);
        TextView subject_view = findViewById(R.id.subject_view);
        TextView cc_view = findViewById(R.id.cc_view);
        TextView bc_view = findViewById(R.id.bc_view);
        TextView content_view = findViewById(R.id.content_view);

        from_view.setText("From: " + message.getFrom());
        to_view.setText("To: " + message.getTo());
        subject_view.setText("Subject: " + message.getSubject());
        cc_view.setText("CC: " + message.getCc());
        bc_view.setText("BC: " + message.getBcc());
        content_view.setText("Content: " + message.getContent());
    }

    // Meni na toolbaru
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_email, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Funkionalnost menija gore navedenog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_mail_back:
                Intent i = new Intent(this, EmailsActivity.class);
                startActivity(i);
                return true;
            case R.id.action_replay:
                Toast.makeText(getBaseContext(), "Replay", Toast.LENGTH_SHORT).show();
                replayMessage();
                return true;
            case R.id.action_replayAll:
                replayToAll();
                Toast.makeText(getBaseContext(), "Replay all", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_forward:
                forward();
                Toast.makeText(getBaseContext(), "Forward message", Toast.LENGTH_SHORT).show();

                return true;
            case R.id.action_delete:
                deleteMessage();
                Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, EmailsActivity.class);
                startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }


    public void deleteMessage() {
        Call<Message> call = messageService.deleteMessage(message.getId());

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
               // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void replayMessage() {
        Call<Message> call = messageService.getMessage(message.getId());

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                setContentView(R.layout.activity_replay_email);

                EditText to_view = findViewById(R.id.send_to);
                EditText subject_view = findViewById(R.id.subject);
                EditText cc_view = findViewById(R.id.cc_edit);
                EditText bc_view = findViewById(R.id.bcc);
                EditText content_view = findViewById(R.id.content_edit);

                to_view.setText(message.getFrom());
                subject_view.setText( message.getSubject());
                cc_view.setText( message.getCc());
                bc_view.setText( message.getBcc());
                content_view.setText( message.getContent());


                toolbar = findViewById(R.id.toolbar);
                toolbarText = findViewById(R.id.toolbar_text);


                toolbarText.setText("Replay message");

                btnSend = findViewById(R.id.button_one);
                btnCancel = findViewById(R.id.button_two);

                btnSend.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
                btnCancel.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));

                btnSend.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        MessageCreateRequestBody messageCreateRequestBody = new MessageCreateRequestBody();

                        EditText to_view = findViewById(R.id.send_to);
                        EditText subject_view = findViewById(R.id.subject);
                        EditText cc_view = findViewById(R.id.cc_edit);
                        EditText bc_view = findViewById(R.id.bcc);
                        EditText content_view = findViewById(R.id.content_edit);

                        to_view.setText(message.getFrom());
                        subject_view.setText( message.getSubject());
                        cc_view.setText( message.getCc());
                        bc_view.setText( message.getBcc());
                        content_view.setText( message.getContent());

                        if(bc_view.getText().toString() == null || bc_view.getText().toString().length() == 0  ||  cc_view.getText().toString() == null || cc_view.getText().toString().length() == 0
                                || to_view.getText().toString() == null || to_view.getText().toString().length() == 0) {

                            Toast.makeText(EmailActivity.this, "Fields can not be empty", Toast.LENGTH_LONG).show();
                            return;
                        }//else

                        //if(!isValidEmailId(to_view.getText().toString().trim())){
                          //  Toast.makeText(EmailActivity.this, "'To' must be valid (example@example.example)", Toast.LENGTH_LONG).show();
                           // return;

                        //}
                        else{
                            messageCreateRequestBody.setBcc(bc_view.getText().toString());
                            messageCreateRequestBody.setCc(cc_view.getText().toString());
                            messageCreateRequestBody.setContent(content_view.getText().toString());
                            messageCreateRequestBody.setSubject(subject_view.getText().toString());
                            messageCreateRequestBody.setTo(to_view.getText().toString());

                            messageCreateRequestBody.setFrom(sharedPreferences.getString(LoginActivity.Username, "User"));
                            messageCreateRequestBody.setDateTime(new Date());
                            messageCreateRequestBody.setMessageTag(21.2);
                            messageCreateRequestBody.setMessageRead(false);
                        }


                        Call<Message> call = messageService.createMessage(messageCreateRequestBody);
                        call.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Toast.makeText(EmailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EmailActivity.this, EmailsActivity.class);
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
                        Intent i = new Intent(EmailActivity.this, EmailsActivity.class);
                        startActivity(i);

                    }
                });
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean isValidEmailId(String email){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    public void replayToAll() {
        Call<Message> call = messageService.getMessage(message.getId());

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                setContentView(R.layout.activity_replay_email);

                EditText to_view = findViewById(R.id.send_to);
                EditText subject_view = findViewById(R.id.subject);
                EditText cc_view = findViewById(R.id.cc_edit);
                EditText bc_view = findViewById(R.id.bcc);
                EditText content_view = findViewById(R.id.content_edit);

                to_view.setText( message.getTo());
                subject_view.setText(message.getSubject());
                cc_view.setText(message.getCc());
                bc_view.setText( message.getBcc());
                content_view.setText(message.getContent());

                toolbar = findViewById(R.id.toolbar);
                toolbarText = findViewById(R.id.toolbar_text);

                btnSend = findViewById(R.id.button_one);
                btnCancel = findViewById(R.id.button_two);

                btnSend.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
                btnCancel.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));

                toolbarText.setText("Replay to all");

                btnSend.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        EditText to_view = findViewById(R.id.send_to);
                        EditText subject_view = findViewById(R.id.subject);
                        EditText cc_view = findViewById(R.id.cc_edit);
                        EditText bc_view = findViewById(R.id.bcc);
                        EditText content_view = findViewById(R.id.content_edit);

                        to_view.setText( message.getTo());
                        subject_view.setText(message.getSubject());
                        cc_view.setText(message.getCc());
                        bc_view.setText( message.getBcc());
                        content_view.setText(message.getContent());
                        MessageCreateRequestBody messageCreateRequestBody = new MessageCreateRequestBody();





                        if(bc_view.getText().toString() == null || bc_view.getText().toString().length() == 0  ||  cc_view.getText().toString() == null || cc_view.getText().toString().length() == 0
                                || to_view.getText().toString() == null || to_view.getText().toString().length() == 0) {

                            Toast.makeText(EmailActivity.this, "Fields can not be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //else

                        //if(!isValidEmailId(to_view.getText().toString().trim())){
                          //  Toast.makeText(EmailActivity.this, "'To' must be valid (example@example.example)", Toast.LENGTH_LONG).show();
                           // return;

                        //}
                        else{
                            messageCreateRequestBody.setBcc(bc_view.getText().toString());
                            messageCreateRequestBody.setCc(cc_view.getText().toString());
                            messageCreateRequestBody.setContent(content_view.getText().toString());
                            messageCreateRequestBody.setSubject(subject_view.getText().toString());
                            messageCreateRequestBody.setTo(to_view.getText().toString());

                            messageCreateRequestBody.setFrom(sharedPreferences.getString(LoginActivity.Username, "User"));
                            messageCreateRequestBody.setDateTime(new Date());
                            messageCreateRequestBody.setMessageTag(21.2);
                            messageCreateRequestBody.setMessageRead(false);
                        }


                        Call<Message> call = messageService.createMessage(messageCreateRequestBody);
                        call.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Toast.makeText(EmailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EmailActivity.this, EmailsActivity.class);
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
                        Intent i = new Intent(EmailActivity.this, EmailsActivity.class);
                        startActivity(i);

                    }
                });
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void forward() {
        Call<Message> call = messageService.getMessage(message.getId());

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                setContentView(R.layout.activity_replay_email);

                EditText to_view = findViewById(R.id.send_to);
                EditText subject_view = findViewById(R.id.subject);
                EditText cc_view = findViewById(R.id.cc_edit);
                EditText bc_view = findViewById(R.id.bcc);
                EditText content_view = findViewById(R.id.content_edit);

                to_view.setText("Enter reciever");
                subject_view.setText(message.getSubject());
                cc_view.setText(message.getCc());
                bc_view.setText(message.getBcc());
                content_view.setText( message.getContent());

                toolbar = findViewById(R.id.toolbar);
                toolbarText = findViewById(R.id.toolbar_text);

                btnSend = findViewById(R.id.button_one);
                btnCancel = findViewById(R.id.button_two);

                btnSend.setImageDrawable(getResources().getDrawable(R.drawable.ic_send));
                btnCancel.setImageDrawable(getResources().getDrawable(R.drawable.ic_cancel));


                toolbarText.setText("Forward message");

                btnSend.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        EditText to_view = findViewById(R.id.send_to);
                        EditText subject_view = findViewById(R.id.subject);
                        EditText cc_view = findViewById(R.id.cc_edit);
                        EditText bc_view = findViewById(R.id.bcc);
                        EditText content_view = findViewById(R.id.content_edit);

                        to_view.setText( message.getTo());
                        subject_view.setText(message.getSubject());
                        cc_view.setText(message.getCc());
                        bc_view.setText( message.getBcc());
                        content_view.setText(message.getContent());
                        MessageCreateRequestBody messageCreateRequestBody = new MessageCreateRequestBody();





                        if(bc_view.getText().toString() == null || bc_view.getText().toString().length() == 0  ||  cc_view.getText().toString() == null || cc_view.getText().toString().length() == 0
                                || to_view.getText().toString() == null || to_view.getText().toString().length() == 0) {

                            Toast.makeText(EmailActivity.this, "Fields can not be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //else

                        //if(!isValidEmailId(to_view.getText().toString().trim())){
                          //  Toast.makeText(EmailActivity.this, "'To' must be valid (example@example.example)", Toast.LENGTH_LONG).show();
                           // return;

                       // }
                        else{
                            messageCreateRequestBody.setBcc(bc_view.getText().toString());
                            messageCreateRequestBody.setCc(cc_view.getText().toString());
                            messageCreateRequestBody.setContent(content_view.getText().toString());
                            messageCreateRequestBody.setSubject(subject_view.getText().toString());
                            messageCreateRequestBody.setTo(to_view.getText().toString());

                            messageCreateRequestBody.setFrom(sharedPreferences.getString(LoginActivity.Username, "User"));
                            messageCreateRequestBody.setDateTime(new Date());
                            messageCreateRequestBody.setMessageTag(21.2);
                            messageCreateRequestBody.setMessageRead(false);
                        }


                        Call<Message> call = messageService.createMessage(messageCreateRequestBody);
                        call.enqueue(new Callback<Message>() {
                            @Override
                            public void onResponse(Call<Message> call, Response<Message> response) {
                                Toast.makeText(EmailActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(EmailActivity.this, EmailsActivity.class);
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
                        Intent i = new Intent(EmailActivity.this, EmailsActivity.class);
                        startActivity(i);

                    }
                });
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                // Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
        finish();
        startActivity(getIntent());
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
