package com.example.appusers.retrofit;

import com.example.appusers.modelos.usuarios;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface interfaceRetrofit {
    //VERBOS HTTP
    /**
     * POST, Validar que el usuario este registrado en la BD.
     * Es de buenas prácticas utilizar POST pues el usaurio y contraseña viajan en la petición.
     * Recordemos que GET expone los datos y POST los oculta
     */
    /**
     * Función en retrofit para mandar los datos y procesar la respuesta
     * @param usuario proporcionar el usuario
     * @param password proporcionar el password
     * @return devuelve el json proporcionado por la API
     */
    @FormUrlEncoded
    @POST("validar") //recordemos que el enrutamiento dentro la API para validar fue definida con el mismo nombre
    Call<List<usuarios>> validar(@Field("usuario") String usuario,
                                 @Field("password") String password);
    @GET
    Call <List<usuarios>> getusuarios (@Url String url);
}
