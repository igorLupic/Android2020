package com.example.pmsumail;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.adapters.ContactListAdapter;
import com.example.pmsumail.adapters.DrawerListAdapter;
import com.example.pmsumail.model.Contact;
import com.example.pmsumail.model.NavItem;
import com.example.pmsumail.service.AccountService;
import com.example.pmsumail.service.ContactService;
import com.example.pmsumail.service.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactsActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPane;
    private ListView mDrawerList;
    private AppBarLayout appBarLayout;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private List<Contact> contacts = new ArrayList<>();
    private Contact contact = new Contact();
    private SharedPreferences sharedPreferences;
    private ContactService contactService;
    private ListView listView;
    private String userPref;
    private AccountService accountService;

    private ContactListAdapter contactListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        prepareMenu(mNavItems);

        mTitle = getTitle();
        appBarLayout = findViewById(R.id.appbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);
        listView = findViewById(R.id.contacts_list);


        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ContactsActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        mDrawerPane = findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new ContactsActivity.DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);

        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
        }
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mTitle);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        // Ispisivanje ulogovanog korisnika u draweru
        TextView userText = findViewById(R.id.userName);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(LoginActivity.Username)) {
            userText.setText(sharedPreferences.getString(LoginActivity.Name, ""));
        }
        userPref = sharedPreferences.getString(LoginActivity.Username, "");

        contactService = ServiceUtils.contactService;
        accountService = ServiceUtils.accountService;

        // Floating action button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactsActivity.this, CreateContactActivity.class);
                Toast.makeText(getBaseContext(), "Create contact", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        // Pozivanje metode koja izlistava sve kontakte
        contactService = ServiceUtils.contactService;
        Call call = contactService.getContacts();

        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {

                if (response.isSuccessful()) {
                    contacts = response.body();
                    listView.setAdapter(new ContactListAdapter(ContactsActivity.this, contacts));
                }
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Otvaranje selektovanog kontakta
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                contact = contacts.get(i);
                Intent intent = new Intent(ContactsActivity.this, ContactActivity.class);
                intent.putExtra("Contact", contact);
                startActivity(intent);
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    // Metoda koja izlistava sve kontakte
    public void getContact() {
        Call<List<Contact>> call = contactService.getContacts();

        call.enqueue(new Callback<List<Contact>>() {
            @Override
            public void onResponse(Call<List<Contact>> call, Response<List<Contact>> response) {
                contacts = response.body();
                ContactListAdapter contactListAdapter = new ContactListAdapter(ContactsActivity.this, contacts);
                listView.setAdapter(contactListAdapter);
            }

            @Override
            public void onFailure(Call<List<Contact>> call, Throwable t) {

            }
        });
    }

    // Ikonice i naslovi u navigation drawer-u
    private void prepareMenu(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem(getString(R.string.emails), null, R.drawable.ic_emails));
        mNavItems.add(new NavItem(getString(R.string.folders), null, R.drawable.ic_folders));
        mNavItems.add(new NavItem(getString(R.string.settings), null, R.drawable.ic_settings));
        mNavItems.add(new NavItem("Logout", null, R.drawable.ic_icon));
    }

    // Listener za navigation drawer
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    // Prelazak na druge aktivnosti klikom na odredjenu poziciju odnosno stavku u draweru
    private void selectItemFromDrawer(int position) {
        if (position == 0) {
            Intent emailsIntent = new Intent(this, EmailsActivity.class);
            startActivity(emailsIntent);
        } else if (position == 1) {
            Intent foldersIntent = new Intent(this, FoldersActivity.class);
            startActivity(foldersIntent);
        } else if (position == 2) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        } else if (position == 3) {
            Intent ite = new Intent(this, LoginActivity.class);
            sharedPreferences.edit().clear().apply();
            startActivity(ite);
            finish();
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }
/*
    // Meni na toolbaru
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_contacts, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Prelazak na create activity klikom na stavku menija na toolbaru
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_contacts:
                Intent in = new Intent(this, CreateContactActivity.class);
                Toast.makeText(getBaseContext(), "Create contact", Toast.LENGTH_SHORT).show();
                startActivity(in);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/

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
