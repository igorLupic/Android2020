package com.example.pmsumail.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pmsumail.R;
import com.example.pmsumail.model.Folder;

import java.util.List;

public class FolderListAdapter extends ArrayAdapter<Folder> {

    private Folder folder;

    public FolderListAdapter(Context context, List<Folder> folders) {
        super(context, 0, folders);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        folder = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.folder_list_item, viewGroup, false);
        }

        TextView folder_name_view = view.findViewById(R.id.folder_name_view);
        TextView number_of_messages = view.findViewById(R.id.number_of_messages);

        folder_name_view.setText(folder.getName());
        number_of_messages.setText("3");


        return view;
    }

}
