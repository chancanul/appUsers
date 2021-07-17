package com.example.appusers.retrofit;

import android.content.ContentValues;
import android.content.Context;
import android.media.MediaParser;
import android.provider.MediaStore;
import android.view.View;

import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.roles;
import com.example.appusers.modelos.usuarios;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
  static public void saveUser(onResponseUsuarios callBack, View v, ContentValues valores, File imgFile) {

      RequestBody reqBD_id_rol = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("id_rol"));
      RequestBody reqBD_nombre = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("nombre"));
      RequestBody reqBD_apellido_p = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("apellido_p"));
      RequestBody reqBD_apellido_m = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("apellido_m"));
      RequestBody reqBD_usuario = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("usuario"));
      RequestBody reqBD_password = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("password"));
      RequestBody reqBD_imagen = RequestBody.create(MediaType.parse("image/*"), imgFile); // Definir el tipo de archivo debe ser imagen.
      MultipartBody.Part multiParBD_File = MultipartBody.Part.createFormData("imagen", (imgFile.getName()), reqBD_imagen);
      interfaceRetrofit retrofit = config.getInterfaceRtrofit();
      Call <List<usuarios>> call = retrofit.saveUser(config.getBaseurl() + "actUser",reqBD_id_rol, reqBD_nombre, reqBD_apellido_p, reqBD_apellido_m,reqBD_usuario, reqBD_password, multiParBD_File);
      call.enqueue(new Callback<List<usuarios>>() {
          @Override
          public void onResponse(Call<List<usuarios>> call, Response<List<usuarios>> response) {
              callBack.usuarios(response.body());
          }
          @Override
          public void onFailure(Call<List<usuarios>> call, Throwable t) {
              config.showMessageUser(v, "Servidor Inaccesible: " + t.getMessage());
          }
      });
  }
  static public void updateUser(onResponseUsuarios callBack, View v, ContentValues valores, File imgFile) {
      RequestBody reqBD_id_rol = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("id_rol"));
      RequestBody reqBD_nombre = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("nombre"));
      RequestBody reqBD_apellido_p = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("apellido_p"));
      RequestBody reqBD_apellido_m = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("apellido_m"));
      RequestBody reqBD_usuario = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("usuario"));
      RequestBody reqBD_password = RequestBody.create(MediaType.parse("multipart/form-data"), valores.getAsString("password"));
      MultipartBody.Part multParMethod = MultipartBody.Part.createFormData("_method", "PUT");
      RequestBody reqBD_imagen = RequestBody.create(MediaType.parse("image/*"), imgFile); // Definir el tipo de archivo debe ser imagen.
      MultipartBody.Part multiParBD_File = MultipartBody.Part.createFormData("imagen", (imgFile.getName()), reqBD_imagen);
      interfaceRetrofit retrofit = config.getInterfaceRtrofit();
      Call<List<usuarios>> call = retrofit.updateUSer(config.getBaseurl() + "actUser/" + valores.getAsString("id_usuario"),
                                                      reqBD_id_rol,
                                                      reqBD_nombre,
                                                      reqBD_apellido_p,
                                                      reqBD_apellido_m,
                                                      reqBD_usuario,
                                                      reqBD_password,
                                                       multiParBD_File,
                                                       multParMethod);
      call.enqueue(new Callback<List<usuarios>>() {
          @Override
          public void onResponse(Call<List<usuarios>> call, Response<List<usuarios>> response) {
              callBack.usuarios(response.body());
          }
          @Override
          public void onFailure(Call<List<usuarios>> call, Throwable t) {
             config.showMessageUser(v, "No ha sido posible guardar los datos del usuario: " + t.getMessage());
          }
      });
  }
}
