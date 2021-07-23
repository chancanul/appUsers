package com.example.appusers.ui.usuario;

import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.widget.ImageView;

import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.appusers.configuracion.images;
import com.example.appusers.modelos.roles;
import com.example.appusers.modelos.usuarios;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.onResponseUsuarios;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import kotlin.text.UStringsKt;

public class UsuarioFragmentViewModel extends ViewModel {
    //Capturar todos los valores del modelo usuario
    private MutableLiveData<String> id_usuario;
    private MutableLiveData<String> nombre;
    private MutableLiveData<String> apellido_p;
    private MutableLiveData<String> apellido_m;
    private MutableLiveData<String> usuario;
    private MutableLiveData<String> password;
    private MutableLiveData<String> imagen;
    private MutableLiveData<String> id_rol;
    private MutableLiveData<Bitmap> bitImage;
    private MutableLiveData<Uri> imageUri;
    //Datos del select
    private MutableLiveData<List<usuarios>> listUsers;
    private MutableLiveData<List<roles>> listRoles;
    private MutableLiveData<String> codeRoles;
    private MutableLiveData<String> code;
    private MutableLiveData<usuarios> selected;
    public UsuarioFragmentViewModel() {
        id_usuario = new MutableLiveData<>();
        nombre = new MutableLiveData<>();
        apellido_p = new MutableLiveData<>();
        apellido_m = new MutableLiveData<>();
        usuario = new MutableLiveData<>();
        password = new MutableLiveData<>();
        imagen = new MutableLiveData<>();
        imageUri = new MutableLiveData<>();
        listUsers = new MutableLiveData<>();
        code = new MutableLiveData<>();
        selected = new MutableLiveData<>();
        listRoles = new MutableLiveData<>();
        codeRoles = new MutableLiveData<>();
        id_rol = new MutableLiveData<>();
    }

    //Los metodos para escuchar y establelcer los cambios
    public void setIdUsuario(String id_usuario) {
        this.id_usuario.setValue(id_usuario);
    }
    public MutableLiveData<String> getIdUsuario() {
        return this.id_usuario;
    }
    public void setNombre(String nombre) {
        this.nombre.setValue(nombre);
    }
    public MutableLiveData<String> getNombre() {
        return this.nombre;
    }
    public void setApellidoP(String apellidop) {
       this.apellido_p.setValue(apellidop);
    }
    public MutableLiveData<String> getApellidoP() {
        return apellido_p;
    }
    public void setApellidoM(String apellidom) {
        this.apellido_m.setValue(apellidom);
    }
    public MutableLiveData<String> getApellidoM() {
        return this.apellido_m;
    }
    public void setUsuario(String usuario) {
        this.usuario.setValue(usuario);
    }
    public MutableLiveData<String> getUsuario() {
        return usuario;
    }
    public void setPassword(String password) {
        this.password.setValue(password);
    }
    public MutableLiveData<String> getPassword() {
        return password;
    }
    public MutableLiveData<String> getImage() {
        return this.imagen;
    }
    public void setIdrol(String id_rol) {
        this.id_rol.setValue(id_rol);
    }
    public LiveData<String> getIdRol() {
        return id_rol;
    }
    public LiveData<String> getCodeRoles() {
        return codeRoles;
    }
    public LiveData<List<roles>> getRoles() {
        return listRoles;
    }


    public MutableLiveData<Bitmap> getBitImage() {
        return bitImage;
    }

    public MutableLiveData<Bitmap> setImageBitMap(Uri uri, FragmentActivity fragment) {
        int rotate = 0;
        try {
            ExifInterface exif = new ExifInterface(Objects.requireNonNull(images.getPath(uri, fragment)));
            int orientation  = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
       this.bitImage.setValue(images.getGuardarImagen(uri, rotate, fragment));
        return this.bitImage;
        //Asignar la imagen al ImageView del layout.
        //binding.duCimgVUsuario.setImageBitmap(thumbnail);
        //imgUser.setImageURI(imageUri);
        //this.imageUri.setValue(uri);
    }
    public MutableLiveData<Uri> setImageUri(Uri uri) {
        this.imageUri.setValue(uri);
        return this.imageUri;
    }
    public MutableLiveData<Uri> getImageUri(){
        return this.imageUri;
    }

    public void setSelected(usuarios item) {
       // selected.setValue(item);
        this.id_usuario.setValue(item.getId_usuario());
        this.nombre.setValue(item.getNombre());
        this.apellido_p.setValue(item.getApellido_p());
        this.apellido_m.setValue(item.getApellido_m());
        this.usuario.setValue(item.getUsuario());
        this.password.setValue(item.getPassword());
        this.imagen.setValue(item.getImagen());
        this.id_rol.setValue(item.getRoles().getId_rol());
    }
    public MutableLiveData<List<usuarios>> getUsers() {
       return listUsers;
    }
    public MutableLiveData<usuarios> getSelected() {
      return selected;
    }
    public MutableLiveData<String> requestUsuarios() {
        httpCall.getUsuarios((list, Code) -> {
            switch (Code) {
                case "200":
                    listUsers.setValue(list);
                    code.setValue(Code);
                    break;
                case "null":
                    code.setValue(Code);
                    break;
                case "void":
                    code.setValue(Code);
                    break;
            }
        });
        return code;
    }

    public MutableLiveData<String> requestRoles() {
        httpCall.getRoles((list, code) -> {
            switch (code) {
                case "200":
                    listRoles.setValue(list);
                    this.codeRoles.setValue(code);
                    break;
                case "null":
                    this.codeRoles.setValue(code);
                    break;
                case "void":
                    this.codeRoles.setValue(code);
                    break;
            }
        });
        return codeRoles;
    }
}