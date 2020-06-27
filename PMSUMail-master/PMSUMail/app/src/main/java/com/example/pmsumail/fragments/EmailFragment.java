package com.example.pmsumail.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pmsumail.R;
import com.example.pmsumail.model.Message;
import com.example.pmsumail.model.Tag;
import com.example.pmsumail.service.MessageService;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EmailFragment extends Fragment {
    View view;

    private Message message;
    ArrayList<Message> messages = new ArrayList<>();
    private MessageService messageService;
    private List<Tag> tags;

    public EmailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.email_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String json = null;
        Bundle extras = getActivity().getIntent().getExtras();
        if (extras != null){
            json = extras.getString("Message");
        }
        message = new Gson().fromJson(json, Message.class);

        message.getId();

        TextView from_view = view.findViewById(R.id.from_view);
        from_view.setText(message.getFrom());

        TextView to_view = view.findViewById(R.id.to_view);
        to_view.setText(message.getTo());

        TextView subject_view = view.findViewById(R.id.subject_view);
        subject_view.setText(message.getSubject());

        TextView cc_view = view.findViewById(R.id.cc_view);
        cc_view.setText(message.getCc());

        TextView bc_view = view.findViewById(R.id.bc_view);
        bc_view.setText(message.getBcc());

        TextView content_view = view.findViewById(R.id.content_view);
        content_view.setText(message.getContent());

        TextView date_messages = view.findViewById(R.id.date_messages);
        String dateMessage = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(message.getDateTime());
        date_messages.setText(dateMessage);

    }
}
