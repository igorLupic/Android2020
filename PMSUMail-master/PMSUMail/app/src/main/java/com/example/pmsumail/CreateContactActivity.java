package com.example.pmsumail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pmsumail.model.Contact;
import com.example.pmsumail.service.ContactService;
import com.example.pmsumail.service.ServiceUtils;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateContactActivity extends AppCompatActivity {

    private EditText firstname;
    private EditText lastname;
    private EditText email;

    private ContactService contactService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        initView();

        Toolbar toolbar = findViewById(R.id.contact_toolbar);
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
        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);
        email = findViewById(R.id.email_edit);

        contactService = ServiceUtils.contactService;

    }


    private void createContact() {
        Contact contact = new Contact();
        contact.setEmail(email.getText().toString());
        contact.setFirstname(firstname.getText().toString());
        contact.setLastname(lastname.getText().toString());
        contact.setDisplay("display1");
        contact.setNote("note1");


       if (email.getText().toString().isEmpty() || firstname.getText().toString().isEmpty() || lastname.toString().isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!isValidEmailId(email.getText().toString().trim())){
           Toast.makeText(CreateContactActivity.this, "'To' must be valid (example@example.example)", Toast.LENGTH_LONG).show();
           return;

       }


        Call<Contact> contactCall = contactService.addContact(contact);
        contactCall.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                Toast.makeText(CreateContactActivity.this, "Contact created", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Toast.makeText(CreateContactActivity.this, "Failed to create contact", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_create_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                onBackPressed();
                return true;
            case R.id.action_save:
                createContact();
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
