package com.example.appusers.ui.usuario;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.appusers.modelos.usuarios;

public class DetalleUsuarioViewModel extends ViewModel {
    //Variable para traer la selección con los datos del unico usuario seleccionado
    //Como las acciones de traer el seleccionado, guardar y modificar retornan un solo valor con los datos del
    //usuario, entonces solo es requerido un mutable livedata del tipo usuarios.
    private MutableLiveData<String> id_usuario;
    private MutableLiveData<String> nombre;
    private MutableLiveData<String> apellido_p;
    private MutableLiveData<String> apellido_m;
    private MutableLiveData<String> usuario;
    private MutableLiveData<String> password;
    private MutableLiveData<String> imagen;
    private MutableLiveData<usuarios> listUser;

    private DetalleUsuarioViewModel() {
        id_usuario = new MutableLiveData<>();
        nombre = new MutableLiveData<>();
        apellido_p = new MutableLiveData<>();
        apellido_m = new MutableLiveData<>();
        usuario = new MutableLiveData<>();
        password = new MutableLiveData<>();
        imagen = new MutableLiveData<>();
        listUser = new MutableLiveData<>();
    }
    //Variable para traer los datos del usuario al guardar.
    public void setSelected(usuarios usuario) {
        id_usuario.setValue(usuario.getId_usuario());
        nombre.setValue(usuario.getNombre());
        apellido_p.setValue(usuario.getApellido_p());
        apellido_m.setValue(usuario.getApellido_m());
        this.usuario.setValue(usuario.getUsuario());
        password.setValue(usuario.getPassword());
        imagen.setValue(usuario.getImagen());
        this.listUser.setValue(usuario);
    }
    public MutableLiveData<usuarios> getSelected() {
        return listUser;
    }

    public MutableLiveData<String> getId_usuario() {
        return id_usuario;
    }
    public MutableLiveData<String> getNombre() {
        return nombre;
    }
    public MutableLiveData<String> getApellidoP() {
        return apellido_p;
    }
    public MutableLiveData<String> getApellidoM() {
        return apellido_m;
    }
    public MutableLiveData<String> getUsuario() {
        return usuario;
    }
    public MutableLiveData<String> getPassword() {
        return password;
    }
    public MutableLiveData<String> getImagen() {
        return imagen;
    }

}