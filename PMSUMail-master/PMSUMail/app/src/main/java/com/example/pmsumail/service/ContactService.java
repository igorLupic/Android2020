package com.example.pmsumail.service;

import com.example.pmsumail.model.Contact;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ContactService {


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(ServiceUtils.CONTACTS)
    Call<List<Contact>> getContacts();

    @GET(ServiceUtils.CONTACTID)
    Call<Contact> getContact(@Path("id") int id);

    @DELETE(ServiceUtils.CONTACTDELETE)
    Call<Contact> deleteContact(@Path("id") int id);

    @PUT(ServiceUtils.CONTACTUPDATE)
    Call<Contact> updateContact(@Path("id") int id);

    @POST(ServiceUtils.CONTACTADD)
    Call<Contact> addContact(@Body Contact contact);

}
