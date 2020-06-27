package com.example.pmsumail;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.pmsumail.adapters.EmailListAdapter;
import com.example.pmsumail.model.Account;
import com.example.pmsumail.model.Message;
import com.example.pmsumail.model.NavItem;
import com.example.pmsumail.model.requestbody.MessageReadRequestBody;
import com.example.pmsumail.service.AccountService;
import com.example.pmsumail.service.MessageService;
import com.example.pmsumail.service.ServiceUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmailsActivity extends AppCompatActivity {



    private DrawerLayout mDrawerLayout;
    private RelativeLayout mDrawerPane;
    private ListView mDrawerList;
    private AppBarLayout appBarLayout;
    private CharSequence mTitle;
    private List<Message> messages = new ArrayList<>();
    private List<Account> accounts = new ArrayList<>();
    private ListView listView;
    private SharedPreferences sharedPreferences;
    private String userPref;
    private Message message = new Message();
    private boolean sortMessages;
    private MessageService messageService;
    private AccountService accountService;

    private ArrayList<NavItem> mNavItems = new ArrayList<NavItem>();

    private EmailListAdapter emailListAdapter;

    private final String CHANNEL_ID = "my_channel";
    private final int NOTIFICATION_ID = 001;
    private int counter = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emails);

        prepareMenu(mNavItems);

        mTitle = getTitle();
        appBarLayout = findViewById(R.id.appbar);

        mDrawerLayout = findViewById(R.id.drawerLayout);
        mDrawerList = findViewById(R.id.navList);
        listView = findViewById(R.id.emails_list);

        //Prelazak na ProfileActivity na klik na "view profile"
        TextView textView = findViewById(R.id.textView);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EmailsActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        mDrawerPane = findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, mNavItems);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerList.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.emails_toolbar);
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

        // Floating action button koji prelazi na create email activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EmailsActivity.this, CreateEmailActivity.class);
                startActivity(i);
                Toast.makeText(getBaseContext(), "Create email", Toast.LENGTH_SHORT).show();

            }
        });

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();


        //Dodato zbog servisa
        TextView userText = findViewById(R.id.userName);
        sharedPreferences = getSharedPreferences(LoginActivity.MyPres, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(LoginActivity.Username)) {
            userText.setText(sharedPreferences.getString(LoginActivity.Name, ""));
        }
        userPref = sharedPreferences.getString(LoginActivity.Username, "");


        messageService = ServiceUtils.messageService;
        accountService = ServiceUtils.accountService;


        // Pozivanje metode koja izlistava sve poruke
        Call call = messageService.getMessages();

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                List<Message> messages1 = response.body();

                if (messages1 == null) {
                    Toast.makeText(EmailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                } else {

                    messages = messages1;

                    final NotificationCompat.Builder builder = new NotificationCompat.Builder(EmailsActivity.this, CHANNEL_ID);
                    for (Message m1 : messages) {
                        if (!m1.isMessageRead()) {
                            counter++;
                            if (counter <= 1) {
                                Intent intent = new Intent(EmailsActivity.this, EmailActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putParcelable("messages", m1);
                                intent.removeExtra("messages");
                                intent.putExtras(bundle);

                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                final PendingIntent intentPending = PendingIntent.getActivity(EmailsActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                builder.setSmallIcon(R.drawable.notification);
                                builder.setContentTitle(m1.getFrom() + " " + counter);


                                builder.setContentText(m1.getContent());
                                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                builder.setContentIntent(intentPending);
                                builder.setAutoCancel(true);
                                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(EmailsActivity.this);
                                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                            } else {

                                Intent intent = new Intent(EmailsActivity.this, EmailsActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                final PendingIntent intentPending = PendingIntent.getActivity(EmailsActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                builder.setSmallIcon(R.drawable.notification);
                                builder.setContentTitle(m1.getFrom() + " " + counter);

                                builder.setContentText(m1.getContent());
                                builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                                builder.setContentIntent(intentPending);
                                builder.setAutoCancel(true);
                                builder.build().flags |= Notification.FLAG_AUTO_CANCEL;

                                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(EmailsActivity.this);
                                notificationManagerCompat.notify(NOTIFICATION_ID, builder.build());
                            }
                        }
                    }
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    CharSequence name = "Message notification";
                    String description = "Include all message notifications";

                    int importance = NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);

                    notificationChannel.setDescription(description);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(notificationChannel);
                }


                if (response.isSuccessful()) {
                    messages = response.body();
                    listView.setAdapter(new EmailListAdapter(EmailsActivity.this, messages));
                }
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("igor", "onFailure: " + t.getMessage());
            }
        });

        Call callAccounts = accountService.getAccounts();

        callAccounts.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> callAcc, Response<List<Account>> responseAcc) {
                if (responseAcc.isSuccessful()) {
                    accounts = responseAcc.body();
                }
            }

            @Override
            public void onFailure(Call callAcc, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                message = messages.get(i);

                if (!message.isMessageRead()) {
                    MessageService service = ServiceUtils.messageService;


                    Call<Message> call = service.editMessage(new MessageReadRequestBody(message.getId()));

                    call.enqueue(new Callback<Message>() {
                        @Override
                        public void onResponse(Call<Message> call, Response<Message> response) {
                            startMessage();

                        }

                        @Override
                        public void onFailure(Call<Message> call, Throwable t) {
                        }
                    });
                } else {
                    startMessage();

                }

            }


        });


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(EmailsActivity.this);

        consultPreferences();

    }



    private void startMessage() {
        Intent intent = new Intent(EmailsActivity.this, EmailActivity.class);
        intent.putExtra("Message", message);
        startActivity(intent);

        messageService = ServiceUtils.messageService;
        Call<Message> call = messageService.getMessage(message.getId());

        call.enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {

                if (response.isSuccessful()) {
                    message = response.body();
                    Intent intent = new Intent(EmailsActivity.this, EmailActivity.class);
                    intent.putExtra("Message", new Gson().toJson(message));

                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void consultPreferences() {
        sortMessages = sharedPreferences.getBoolean(getString(R.string.pref_sort_messages_by_date_key_list_asc), false);
        if (sortMessages) {
            sortDateAsc();
        } else {
            sortDateDesc();
        }

    }

    private void sortDateAsc() {
        Call<List<Message>> callMessage = messageService.sortMessagesAsc();

        callMessage.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages = response.body();

                EmailListAdapter adapter = new EmailListAdapter(EmailsActivity.this, messages);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

    private void sortDateDesc() {
        Call<List<Message>> callMessage = messageService.sortMessagesDesc();

        callMessage.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages = response.body();

                EmailListAdapter adapter = new EmailListAdapter(EmailsActivity.this, messages);
                listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }

    public List<Message> sortedListOfMessages(List<Message> messages, boolean isSortAscending) {
        List<Message> sortedListOfMessagess = messages;
        if (isSortAscending) {
            Collections.sort(messages, new Comparator<Message>() {
                public int compare(Message o1, Message o2) {
                    return o2.getDateTime().compareTo(o1.getDateTime());
                }
            });
        } else {
            Collections.sort(messages, new Comparator<Message>() {
                public int compare(Message o1, Message o2) {
                    return o1.getDateTime().compareTo(o2.getDateTime());
                }
            });
        }


        return sortedListOfMessagess;
    }

    // Metoda koja izlistava sve poruke
    public void getMessage() {
        Call<List<Message>> call = messageService.getMessages();

        call.enqueue(new Callback<List<Message>>() {
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messages = response.body();
                EmailListAdapter emailListAdapter = new EmailListAdapter(EmailsActivity.this, messages);
                listView.setAdapter(emailListAdapter);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }


    //listener koji prihvata informaciju koja pozicija u draweru je kliknuta
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    //ikonice i tekst za navigation drawer
    private void prepareMenu(ArrayList<NavItem> mNavItems) {
        mNavItems.add(new NavItem(getString(R.string.contacts), null, R.drawable.ic_contact));
        mNavItems.add(new NavItem(getString(R.string.folders), null, R.drawable.ic_folders));
        mNavItems.add(new NavItem("Logout", null, R.drawable.ic_back));
        mNavItems.add(new NavItem(getString(R.string.settings), null, R.drawable.ic_settings));
    }

    //prelazak na aktivnosti iz navigation drawera
    private void selectItemFromDrawer(int position) {
        if (position == 0) {
            Intent contactsIntent = new Intent(this, ContactsActivity.class);
            startActivity(contactsIntent);

        } else if (position == 1) {
            Intent foldersIntent = new Intent(this, FoldersActivity.class);
            startActivity(foldersIntent);
        }else if (position == 2) {
            Intent ite = new Intent(this, LoginActivity.class);
            sharedPreferences.edit().clear().commit();
            startActivity(ite);
        }else if (position == 3) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(mNavItems.get(position).getmTitle());
        mDrawerLayout.closeDrawer(mDrawerPane);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_detail, menu);
        return super.onCreateOptionsMenu(menu);
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