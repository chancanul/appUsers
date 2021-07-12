package com.example.appusers.retrofit;

import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.roles;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class httpCall {
    static public void getRoles(onResponseRoles callBack) {
      interfaceRetrofit retrofit = config.getRetrofit().create(interfaceRetrofit.class);
        Call <List<roles>> request = retrofit.getRoles("actRoles");
        request.enqueue(new Callback<List<roles>>() {
            @Override
            public void onResponse(Call<List<roles>> call, Response<List<roles>> response) {
                callBack.roles(response.body());
            }
            @Override
            public void onFailure(Call<List<roles>> call, Throwable t) {

            }
        });
  }
}
