package org.d3if3040.logindanqrcode.Api;

import org.d3if3040.logindanqrcode.Model.UserData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("data.json")
    Call<List<UserData>> getUserData();
}


