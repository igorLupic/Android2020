package com.example.pmsumail.service;

import android.app.Service;

import com.example.pmsumail.model.Message;
import com.example.pmsumail.model.requestbody.MessageCreateRequestBody;
import com.example.pmsumail.model.requestbody.MessageReadRequestBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MessageService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(ServiceUtils.MESSAGES)
    Call<List<Message>> getMessages();

    @GET(ServiceUtils.SORTMESSAGESASC)
    Call<List<Message>> sortMessagesAsc();

    @GET(ServiceUtils.SORTMESSAGESDESC)
    Call<List<Message>> sortMessagesDesc();

    @GET(ServiceUtils.MESSAGEID)
    Call<Message> getMessage(@Path("id") int id);

    @DELETE(ServiceUtils.MESSAGEDELETE)
    Call<Message> deleteMessage(@Path("id") int id);

    @POST(ServiceUtils.MESSAGEREAD)
    Call<Message> editMessage(@Body MessageReadRequestBody messageRead);

    @POST(ServiceUtils.MESSAGEADD)
    Call<Message> createMessage(@Body MessageCreateRequestBody messageCreateRequestBody);

}
