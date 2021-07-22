package com.example.appusers.retrofit;


import android.view.View;

import com.example.appusers.modelos.usuarios;

import java.util.List;

public interface onResponseUsuarios {
    void usuarios(List<usuarios> list, String code);
}
