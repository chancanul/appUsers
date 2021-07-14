package com.example.appusers.retrofit;

import android.content.Context;
import android.view.View;

import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.roles;
import com.example.appusers.modelos.usuarios;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

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

  static public void getUsuarios(onResponseUsuarios callBack, View v) {
        interfaceRetrofit retrofit = config.getRetrofit().create(interfaceRetrofit.class);
        Call <List<usuarios>> call = retrofit.getusuarios("actUser");
        call.enqueue(new Callback<List<usuarios>>() {
            @Override
            public void onResponse(Call<List<usuarios>> call, Response<List<usuarios>> response) {
                callBack.usuarios(response.body());
            }

            @Override
            public void onFailure(Call<List<usuarios>> call, Throwable t) {
                Snackbar.make(v,"Servidor inaccesible",Snackbar.LENGTH_SHORT).show();
            }
        });
  }

  static public void getValidar(onResponseValidar callBack,View v, String user, String password) {
        interfaceRetrofit retrofit = config.getRetrofit().create(interfaceRetrofit.class);
        Call <List<usuarios>> call = retrofit.validar(user, password);
        call.enqueue(new Callback<List<usuarios>>() {
            @Override
            public void onResponse(@NotNull Call<List<usuarios>> call, @NotNull Response<List<usuarios>> response) {
                callBack.usuarios(response.body());
            }

            @Override
            public void onFailure(@NotNull Call<List<usuarios>> call, @NotNull Throwable t) {
                Snackbar.make(v,"Servidor inaccesible",Snackbar.LENGTH_SHORT).show();
            }
        });
  }
}
