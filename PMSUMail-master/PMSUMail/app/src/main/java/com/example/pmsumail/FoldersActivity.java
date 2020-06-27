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

import com.example.pmsumail.adapters.DrawerListAdapter;
import com.example.pmsumail.adapters.FolderListAdapter;
import com.example.pmsumail.model.Folder;
import com.example.pmsumail.model.NavItem;
import com.example.pmsumail.service.AccountService;
import com.example.pmsumail.service.FolderService;
import com.example.pmsumail.service.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoldersActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPane;
    private ListView mDrawerList;
    private AppBarLayout appBarLayout;
    private CharSequence mTitle;
    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();
    private List<Folder> folders = new ArrayList<>();
    private Folder folder = new Folder();
    private SharedPreferences sharedPreferences;
    private FolderService folderService;
    private ListView listView;
    private String userPref;
    private AccountService accountService;


    private FolderListAdapter folderListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folders);

        prepareMenu(mNavItems);

        mTitle = getTitle();
        appBarLayout = findViewById(R.id.appbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);
        listView = findViewById(R.id.folders_list);

        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FoldersActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        mDrawerPane = findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new FoldersActivity.DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.folders_toolbar);
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


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(FoldersActivity.this, CreateFolderActivity.class);
                Toast.makeText(getBaseContext(), "Create folder", Toast.LENGTH_SHORT).show();
                startActivity(in);
            }
        });

        folderService = ServiceUtils.folderService;
        accountService = ServiceUtils.accountService;

        // Pozivanje metode za izlistavanje svih foldera
        Call call = folderService.getFolders();

        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {

                if (response.isSuccessful()) {
                    folders = response.body();
                    listView.setAdapter(new FolderListAdapter(FoldersActivity.this, folders));
                }
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Prelazak na folder act klikom na konkretan folder
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                folder = folders.get(i);
                Intent intent = new Intent(FoldersActivity.this, FolderActivity.class);
                intent.putExtra("folder", folder);
                startActivity(intent);
            }
        });
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
    }


    //Metoda za izlistavanje svih foldera
    public void getFolder() {
        Call<List<Folder>> call = folderService.getFolders();

        call.enqueue(new Callback<List<Folder>>() {
            @Override
            public void onResponse(Call<List<Folder>> call, Response<List<Folder>> response) {
                folders = response.body();
                FolderListAdapter folderListAdapter = new FolderListAdapter(FoldersActivity.this, folders);
                listView.setAdapter(folderListAdapter);
            }

            @Override
            public void onFailure(Call<List<Folder>> call, Throwable t) {

            }
        });
    }

    // Ikonice i naslovi u navigation drawer-u
    private void prepareMenu(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem(getString(R.string.contacts), null, R.drawable.ic_contact));
        mNavItems.add(new NavItem(getString(R.string.emails), null, R.drawable.ic_emails));
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
            Intent contactsIntent = new Intent(this, ContactsActivity.class);
            startActivity(contactsIntent);
        } else if (position == 1) {
            Intent foldersIntent = new Intent(this, EmailsActivity.class);
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

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }
/*
    // Meni na toolbaru
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_folders, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Prelazak na create activity klikom na stavku menija na toolbaru
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_folder:
                Intent in = new Intent(this, CreateFolderActivity.class);
                Toast.makeText(getBaseContext(), "Create folder", Toast.LENGTH_SHORT).show();
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
