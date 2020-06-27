package com.example.pmsumail.service;

import com.example.pmsumail.model.Folder;
import com.example.pmsumail.model.requestbody.FolderRequestBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FolderService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET(ServiceUtils.FOLDERS)
    Call<List<Folder>> getFolders();

    @GET(ServiceUtils.FOLDERID)
    Call<Folder> getFolder(@Path("id") int id);

    @DELETE(ServiceUtils.FOLDERDELETE)
    Call<Folder> deleteFolder(@Path("id") int id);

    @POST(ServiceUtils.FOLDERADD)
    Call<Folder> createFolder(@Body FolderRequestBody folderRequestBody);

}
