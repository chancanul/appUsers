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
import com.example.appusers.retrofit.onResponseRoles;
import com.example.appusers.retrofit.onResponseUsuarios;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import kotlin.text.UStringsKt;

public class UsuarioFragmentViewModel extends ViewModel {
    //Capturar todos los valores del modelo usuario
    private MutableLiveData <String> accion;
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
   // public UsuarioFragmentViewModel() {
        /**nombre = new MutableLiveData<>();
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
        accion = new MutableLiveData<>();**/
   // }

    //Los metodos para escuchar y establelcer los cambios
    public void setIdUsuario(String id_usuario) {
        if (this.id_usuario == null) {
            this.id_usuario = new MutableLiveData<>();
        }
        this.id_usuario.setValue(id_usuario);
    }
    public MutableLiveData<String> getIdUsuario() {
        if (this.id_usuario == null) {
            this.id_usuario = new MutableLiveData<>();
        }
        return this.id_usuario;
    }
    public void setNombre(String nombre) {
        if (this.nombre == null) {
            this.nombre = new MutableLiveData<>();
        }
        this.nombre.setValue(nombre);
    }
    public MutableLiveData<String> getNombre() {
        if(this.nombre == null) {
            this.nombre = new MutableLiveData<>();
        }
        return this.nombre;
    }
    public void setApellidoP(String apellidop) {
        if (this.apellido_p == null) {
            this.apellido_p = new MutableLiveData<>();
        }
       this.apellido_p.setValue(apellidop);
    }
    public MutableLiveData<String> getApellidoP() {
        if (this.apellido_p == null) {
            this.apellido_p = new MutableLiveData<>();
        }
        return apellido_p;
    }
    public void setApellidoM(String apellidom) {
        if (this.apellido_m == null) {
            this.apellido_m = new MutableLiveData<>();
        }
        this.apellido_m.setValue(apellidom);
    }
    public MutableLiveData<String> getApellidoM() {
        if (this.apellido_m == null) {
            this.apellido_m = new MutableLiveData<>();
        }
        return this.apellido_m;
    }
    public void setUsuario(String usuario) {
        if (this.id_usuario == null) {
            this.id_usuario = new MutableLiveData<>();
        }
        this.usuario.setValue(usuario);
    }
    public MutableLiveData<String> getUsuario() {
        if (this.usuario == null) {
            this.usuario = new MutableLiveData<>();
        }
        return usuario;
    }
    public void setPassword(String password) {
        if (this.password == null) {
            this.password = new MutableLiveData<>();
        }
        this.password.setValue(password);
    }
    public MutableLiveData<String> getPassword() {
        if (this.password == null) {
            new MutableLiveData<>();
        }
        return password;
    }
    public void setImage(String imagen) {
        if (this.imagen == null) {
            this.imagen = new MutableLiveData<>();
        }
        this.imagen.setValue(imagen);
    }
    public MutableLiveData<String> getImage() {
        if (this.imagen == null) {
            this.imagen = new MutableLiveData<>();
        }
        return this.imagen;
    }
    public void setIdrol(String id_rol) {
        if (this.id_rol == null) {
            this.id_rol = new MutableLiveData<>();
        }
        this.id_rol.setValue(id_rol);
    }
    public LiveData<String> getIdRol() {
        if (this.id_rol == null) {
            this.id_rol = new MutableLiveData<>();
        }
        return id_rol;
    }
    public LiveData<String> getCodeRoles() {
        if (this.codeRoles == null) {
            this.codeRoles = new MutableLiveData<>();
        }
        return codeRoles;
    }
    public LiveData<List<roles>> getRoles() {
        if (this.listRoles == null) {
            this.listRoles = new MutableLiveData<>();
        }
        return listRoles;
    }


    public MutableLiveData<Bitmap> getBitImage() {
        if (this.bitImage == null) {
            this.bitImage = new MutableLiveData<>();
        }
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
        if (this.bitImage == null) {
            bitImage = new MutableLiveData<>();
        }
       this.bitImage.setValue(images.getGuardarImagen(uri, rotate, fragment));
        return this.bitImage;
        //Asignar la imagen al ImageView del layout.
        //binding.duCimgVUsuario.setImageBitmap(thumbnail);
        //imgUser.setImageURI(imageUri);
        //this.imageUri.setValue(uri);
    }
    public MutableLiveData<Uri> setImageUri(Uri uri) {
        if (this.imageUri == null) {
            this.imageUri = new MutableLiveData<>();
        }
        this.imageUri.setValue(uri);
        return this.imageUri;
    }
    public MutableLiveData<Uri> getImageUri(){
        if (this.imageUri == null) {
            this.imageUri = new MutableLiveData<>();
        }
        return this.imageUri;
    }

    public void setSelected(usuarios item) {
       // selected.setValue(item);
        if (this.id_usuario == null) {
            this.id_usuario = new MutableLiveData<>();
        }
        this.id_usuario.setValue(item.getId_usuario());
        if (this.nombre == null) {
            this.nombre = new MutableLiveData<>();
        }
        this.nombre.setValue(item.getNombre());
        if (this.apellido_p == null) {
            this.apellido_p = new MutableLiveData<>();
        }
        this.apellido_p.setValue(item.getApellido_p());
        if (this.apellido_m == null) {
            this.apellido_m = new MutableLiveData<>();
        }
        this.apellido_m.setValue(item.getApellido_m());
        if (this.usuario == null) {
            this.usuario = new MutableLiveData<>();
        }
        this.usuario.setValue(item.getUsuario());
        if (this.password == null) {
            this.password = new MutableLiveData<>();
        }
        this.password.setValue(item.getPassword());
        if (this.imagen == null) {
            this.imagen = new MutableLiveData<>();
        }
        this.imagen.setValue(item.getImagen());
        if (this.id_rol == null) {
            this.id_rol = new MutableLiveData<>();
        }
        this.id_rol.setValue(item.getRoles().getId_rol());
    }
    public MutableLiveData<List<usuarios>> getUsers() {
        if (this.listUsers == null) {
            this.listUsers = new MutableLiveData<>();
        }
       return this.listUsers;
    }
    public MutableLiveData<usuarios> getSelected() {
        if (this.selected == null) {
            this.selected = new MutableLiveData<>();
        }
      return this.selected;
    }
    public MutableLiveData<String> requestUsuarios() {
        if (this.code == null) {
            this.code = new MutableLiveData<>();
        }
        httpCall.getUsuarios((list, Code) -> {
            switch (Code) {
                case "200":
                    if (this.listUsers == null) {
                        this.listUsers = new MutableLiveData<>();
                    }
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

    public void requestRoles() {
        if (this.codeRoles == null) {
            this.codeRoles = new MutableLiveData<>();
        }
        httpCall.getRoles((list, code) -> {
            switch (code) {
                case "200":
                    if (this.listRoles == null) {
                        this.listRoles = new MutableLiveData<>();
                    }
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
    }

    //Segunda
      private MutableLiveData<ArrayList<String>> itemRoles;
      private MutableLiveData<Integer> indexSpinner;

    public void setIndexSpinner(Integer index) {
        if (this.indexSpinner == null) {
            this.indexSpinner = new MutableLiveData<>();
        }
        this.indexSpinner.setValue(index);
    }
    public MutableLiveData<Integer> getIndexSpinner() {
        if (this.indexSpinner == null) {
            this.indexSpinner = new MutableLiveData<>();
        }
        return indexSpinner;
    }
    public void setItemRoles(ArrayList<String> itemRoles) {
        if(this.itemRoles == null) {
            this.itemRoles = new MutableLiveData<>();
        }
        this.itemRoles.setValue(itemRoles);
    }
    public LiveData<ArrayList<String>> getItemRoles() {
        if (this.itemRoles == null) {
            this.itemRoles = new MutableLiveData<>();
        }
        return itemRoles;
    }


    public void setAccion(String accion) {
        String id_rol = "";
        if (this.accion == null) {
            this.accion = new MutableLiveData<>();
        }
        this.accion.setValue(accion);
      httpCall.getRoles((list, Code) -> {
          ArrayList<String> item = new ArrayList<String>();
          for (roles rol: list) {
              item.add(rol.getNombre());
          }
          setItemRoles(item);





          
      });
       // if (requestRoles().getValue().equals("200")) {
            // ArrayList<String> item = new ArrayList<String>();
             //for (roles rol: getRoles().getValue()) {
              //   item.add(rol.getNombre());
           //  }
           //  setItemRoles(item);
      //  }
        if (accion.equals("M")) {
            setIdrol(getSelected().getValue().getId_rol());
            setNombre(getSelected().getValue().getNombre());
            setApellidoP(getSelected().getValue().getApellido_p());
            setApellidoM(getSelected().getValue().getApellido_m());
            setUsuario(getSelected().getValue().getUsuario());
            setPassword(getSelected().getValue().getPassword());
            setImage(getSelected().getValue().getImagen());
            id_rol = getIdRol().getValue();
            int count = -1;
            for (roles rol: getRoles().getValue()) {
                count ++;
                if (rol.getId_rol().equals(id_rol)) {
                    setIndexSpinner(count);
                }
            }

        } else {
            setIdUsuario("");
            setIdrol("");
            setNombre("");
            setApellidoP("");
            setApellidoM("");
            setUsuario("");
            setPassword("");
            setImage("");
        }
    }
}