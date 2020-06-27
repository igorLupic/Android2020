package com.example.pmsumail.service;

import com.example.pmsumail.model.Account;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AccountService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })

    @GET(ServiceUtils.LOGIN)
    Call<Account> login(@Path("username") String username, @Path("password") String password);

    @GET(ServiceUtils.USERNAME)
    Call<Account> getByUsername(@Path("username") String username);

    @GET(ServiceUtils.ACCOUNTS)
    Call<List<Account>> getAccounts();
}
