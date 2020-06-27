package com.example.pmsumail;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.model.Contact;
import com.example.pmsumail.service.ContactService;
import com.example.pmsumail.service.ServiceUtils;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    private ContactService contactService;
    private Contact contact = new Contact();


    private EditText firstname;
    private EditText lastname;
    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);

        Toolbar toolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(toolbar);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            contact = extras.getParcelable("Contact");
        }
        contactService = ServiceUtils.contactService;

        TextView firstname_view = findViewById(R.id.firstname_view);
        TextView lastname_view = findViewById(R.id.lastname_view);
        TextView email_view = findViewById(R.id.email_view);

        firstname_view.setText("First Name: " + contact.getFirstname());
        lastname_view.setText("Last Name: " + contact.getLastname());
        email_view.setText("Email: " + contact.getEmail());
    }

    //meni na toolbaru, odnosno ikonice za prelazak na ostale aktivnosti
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //funkcionalnost opcija iz menija gore navedenog
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent in = new Intent(this, ContactsActivity.class);
                startActivity(in);
                return true;
            case R.id.action_delete:
                deleteContact();
                Toast.makeText(ContactActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ContactsActivity.class);
                startActivity(intent);
            case R.id.action_update:
                updateContact();
                return true;
            case R.id.action_save:

                Toast.makeText(ContactActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                Intent intenta = new Intent(this, ContactsActivity.class);
                startActivity(intenta);


        }
        return super.onOptionsItemSelected(item);
    }

    // Metoda koja brise izabranog kontakta
    public void deleteContact() {
        Call<Contact> call = contactService.deleteContact(contact.getId());

        call.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
                //Toast.makeText(ContactActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
               //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void updateContact() {

        setContentView(R.layout.activity_update_contact);

        Toolbar toolbar = findViewById(R.id.contact_toolbar);
        setSupportActionBar(toolbar);


        firstname = findViewById(R.id.first_name);
        lastname = findViewById(R.id.last_name);
        email = findViewById(R.id.email_edit);



        firstname.setText(contact.getFirstname());
        lastname.setText( contact.getLastname());
        email.setText(contact.getEmail());

        if (email.getText().toString().isEmpty() || firstname.getText().toString().isEmpty() || lastname.toString().isEmpty()) {
            Toast.makeText(this, "All fields must be filled!", Toast.LENGTH_SHORT).show();
            return;
        }else if(!isValidEmailId(email.getText().toString().trim())){
            Toast.makeText(ContactActivity.this, "'To' must be valid (example@example.example)", Toast.LENGTH_LONG).show();
            return;

        }


        Call<Contact> contactCall = contactService.updateContact(contact.getId());
        contactCall.enqueue(new Callback<Contact>() {
            @Override
            public void onResponse(Call<Contact> call, Response<Contact> response) {
            }

            @Override
            public void onFailure(Call<Contact> call, Throwable t) {
                Toast.makeText(ContactActivity.this, "Failed to create contact", Toast.LENGTH_SHORT).show();
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
