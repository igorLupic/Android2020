package com.example.pmsumail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.adapters.EmailListAdapter;
import com.example.pmsumail.adapters.MessagesListAdapter;
import com.example.pmsumail.model.Folder;
import com.example.pmsumail.model.Message;
import com.example.pmsumail.service.FolderService;
import com.example.pmsumail.service.MessageService;
import com.example.pmsumail.service.ServiceUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FolderActivity extends AppCompatActivity {

    private FolderService folderService;
    private MessageService messageService;

    private TextView folderName;
    private List<Message> messages = new ArrayList<>();
    private ListView listView;

    private Folder folder;

    private ArrayList<Folder> folders = new ArrayList<Folder>();


    private MessagesListAdapter messagesListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        Toolbar toolbar = findViewById(R.id.folder_toolbar);
        setSupportActionBar(toolbar);

        listView = findViewById(R.id.emails_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            folder = extras.getParcelable("folder");
        }
        folderService = ServiceUtils.folderService;
        initView();

    }

    private void initView() {

        if (folder.getName().equalsIgnoreCase("inbox")) {
            Intent i = new Intent(this, EmailsActivity.class);
            startActivity(i);
        }else if (folder.getName().equalsIgnoreCase("drafts")){
            Toast.makeText(getBaseContext(), "Drafts folder", Toast.LENGTH_SHORT).show();

        }else{
              folderName = findViewById(R.id.folder_name);
              folderName.setText(folder.getName());

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_folder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Intent in = new Intent(this, FoldersActivity.class);
                startActivity(in);
                return true;
            case R.id.edit_folder:
                Toast.makeText(getBaseContext(), "Edit folder", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.delete_folder:
                deleteFolder();
              // Toast.makeText(FolderActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, FoldersActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    // Metoda za brisanje foldera
    public void deleteFolder() {
        // Ako je naziv foldera inbox ili drafts, brisanje je zabranjeno
        if (folder.getName().equalsIgnoreCase("inbox") ||
                folder.getName().equalsIgnoreCase("drafts")) {
            Toast.makeText(this, "You cant delete " + folder.getName() + " folder", Toast.LENGTH_SHORT).show();
            return;
        }
        Call<Folder> call = folderService.deleteFolder(folder.getId());

        call.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                Toast.makeText(FolderActivity.this, "Folder deleted", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
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
