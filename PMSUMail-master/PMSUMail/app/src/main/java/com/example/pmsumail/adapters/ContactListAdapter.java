package com.example.pmsumail.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pmsumail.R;
import com.example.pmsumail.model.Contact;

import java.util.List;

public class ContactListAdapter extends ArrayAdapter<Contact> {

    public ContactListAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        Contact contact = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.contact_list_item, viewGroup, false);
        }

        TextView contact_name_view = view.findViewById(R.id.contact_name_view);
//        ImageView image_view = view.findViewById(R.id.image_view);

        contact_name_view.setText(contact.getFirstname());
//        image_view.setImageBitmap(contact.getPhoto().getBitmap());

        return view;
    }
}
