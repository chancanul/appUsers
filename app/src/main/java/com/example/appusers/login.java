package com.example.appusers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.usuarios;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.interfaceRetrofit;
import com.example.appusers.retrofit.onResponseValidar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class login extends AppCompatActivity {

    private EditText usuario, password;
    private FloatingActionButton fbLogin;
    private int permisoRequerido = 11;
    private ConstraintLayout backProgress;
    private TextView pgsTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        usuario = findViewById(R.id.edTxtCUsuario);
        password = findViewById(R.id.edTxtPass);
        fbLogin = findViewById(R.id.fabCLogin);
        backProgress = findViewById(R.id.lytProgress);
        pgsTxt = findViewById(R.id.txtVCProgress);

        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pgsTxt.setText("Validando credenciales");
                backProgress.setVisibility(View.VISIBLE);
                validar(usuario.getText().toString(), password.getText().toString(), v);
            }
        });
        getPermisos();
    }

    private void validar(String usuario, String password, View v) {
        //Variable para iniciar la petición Retrofit
       // interfaceRetrofit peticion = config.getRetrofit().create(interfaceRetrofit.class);
        //Preparar la petición call (llamar)
        //Call<List<usuarios>> call = peticion.validar(usuario, password);
        //Iniciar la petición con enqueue. el método incluye dos apartados para saber si la petición se llevó con éxito o fracaso
        //onResponse-onFailure
       // call.enqueue(new Callback<List<usuarios>>() {
          //  @Override
           // public void onResponse(Call<List<usuarios>> call, Response<List<usuarios>> response) {
                //En caso de éxito
                //la variable response es la encargada de almacenar la respuesta del servidor.

             //   List <usuarios> users = response.body();
                //Si la respuesta es correcta se llama al navigation drawer.
               // if(users.get(0) != null)
               // {
                  //  backProgress.setVisibility(View.GONE);
                    //Intent principal = new Intent(getApplicationContext(), MainActivity.class);
                   // principal.putExtra("nombre", users.get(0).getNombre());
                   // principal.putExtra("apellido_p", users.get(0).getApellido_p());
                   // principal.putExtra("imagen", users.get(0).getImagen());
                   // startActivity(principal);
               // } else {
                 //   backProgress.setVisibility(View.GONE);
                   // Snackbar msjPersonalizado = Snackbar.make(v, "Usuario o contraseña no válidos", Snackbar.LENGTH_SHORT);
                   // msjPersonalizado.show();
               // }
           // }

           // @Override
            //public void onFailure(Call<List<usuarios>> call, Throwable t) {
                //en caso de fracaso
              //  backProgress.setVisibility(View.GONE);
                //Snackbar msjPersonalizado = Snackbar.make(v, "Servidor inaccesible", Snackbar.LENGTH_SHORT);
                //msjPersonalizado.show();

           // }
       // });

        httpCall.getValidar(list -> {
            if (list.get(0) != null) {
                backProgress.setVisibility(View.GONE);
                Intent principal = new Intent(getApplicationContext(), MainActivity.class);
                principal.putExtra("nombre", list.get(0).getNombre());
                principal.putExtra("apellido_p", list.get(0).getApellido_p());
                principal.putExtra("imagen", list.get(0).getImagen());
                startActivity(principal);
            } else {
                backProgress.setVisibility(View.GONE);
                Snackbar msjPersonalizado = Snackbar.make(v, "Usuario o contraseña no válidos", Snackbar.LENGTH_SHORT);
                msjPersonalizado.show();
            }
        }, v, usuario, password);
    } //fin validar

    private void getPermisos() {
        int accesLeerSD = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int accesCamara = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int accesEscribirSD = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (accesLeerSD != getPackageManager().PERMISSION_GRANTED
                || accesCamara != getPackageManager().PERMISSION_GRANTED
                || accesEscribirSD != getPackageManager().PERMISSION_GRANTED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, permisoRequerido);
        }


    }

}