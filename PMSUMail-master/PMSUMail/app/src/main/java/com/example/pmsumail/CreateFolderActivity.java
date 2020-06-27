package com.example.pmsumail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pmsumail.model.Folder;
import com.example.pmsumail.model.requestbody.FolderRequestBody;
import com.example.pmsumail.service.FolderService;
import com.example.pmsumail.service.ServiceUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateFolderActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView toolbarText;
    private ImageView btnSave;
    private ImageView btnCancel;
    private ImageView btnBack;
    private EditText folderNameEditText;
    private FolderService folderService;
    private Button butonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_folder);
        folderService = ServiceUtils.folderService;
        initView();


        Toolbar toolbar = findViewById(R.id.folder_toolbar);
        setSupportActionBar(toolbar);


    }

    private void initView() {

        folderNameEditText = findViewById(R.id.folder_name);

    }


    private void createFolder() {
        String folderName = folderNameEditText.getText().toString();
        if (folderName.isEmpty()) {
            Toast.makeText(CreateFolderActivity.this, "Folder name is empty", Toast.LENGTH_LONG).show();
            return;
        }
        FolderRequestBody folderRequestBody = new FolderRequestBody(folderName, "0", 0);
        Call<Folder> folderCall = folderService.createFolder(folderRequestBody);
        folderCall.enqueue(new Callback<Folder>() {
            @Override
            public void onResponse(Call<Folder> call, Response<Folder> response) {
                Toast.makeText(CreateFolderActivity.this, "Folder is created", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Folder> call, Throwable t) {
                Log.e("CreateFolder", "on create folder failure");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_item_create_folder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_back:
                Toast.makeText(this, "Back", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, FoldersActivity.class);
                startActivity(i);
                return true;
            case R.id.action_save:
                createFolder();
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
