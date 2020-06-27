package com.example.pmsumail.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.pmsumail.R;
import com.example.pmsumail.model.Message;

import java.text.SimpleDateFormat;
import java.util.List;

public class EmailListAdapter extends ArrayAdapter<Message> {

    private Message message;


    public EmailListAdapter(Context context, List<Message> messages) {
        super(context, 0, messages);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        message = getItem(position);

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.email_list_item, viewGroup, false);
        }

        TextView email_context_view = view.findViewById(R.id.email_context_view);
        TextView email_from_view = view.findViewById(R.id.email_name_view);
        TextView date_messages = view.findViewById(R.id.date_messages);

        email_context_view.setText(message.getContent());

        email_from_view.setText(message.getFrom());
        if(!message.isMessageRead())view.setBackgroundColor(Color.GRAY);

        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        String date = simpleDate.format(message.getDateTime());
        date_messages.setText(date);




        return view;
    }

}
