package com.example.appusers.ui.usuario;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.appusers.MainActivity;
import com.example.appusers.R;
import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.roles;
import com.example.appusers.retrofit.interfaceRetrofit;
import com.example.appusers.retrofit.onResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleUsuarioFragment extends Fragment {

    private DetalleUsuarioViewModel mViewModel;

    private View myView;
    private FloatingActionButton fabSave;
    private EditText nombre, apellido_p, apellido_m, usuario, password;
    private TextView id_usuario;
    private Spinner spinRol = null;
    private ImageView imgUser;
    private ImageButton imgBtnCamera, imgBtnGallery;
    private List<roles> listRoles;

    private String accion = "";
    public static DetalleUsuarioFragment newInstance() {
        return new DetalleUsuarioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.detalle_usuario_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.myView = view;
        MainActivity detalleFragment = (MainActivity) getActivity();
        if (detalleFragment != null) {

            fabSave = detalleFragment.findViewById(R.id.fab);
            fabSave.setImageResource(R.drawable.save);
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    
                }
            });
            id_usuario = myView.findViewById(R.id.duCEtxtid);
            nombre = myView.findViewById(R.id.duCEtxtNombre);
            apellido_p = myView.findViewById(R.id.duCEtxtApellidop);
            apellido_m = myView.findViewById(R.id.duCEtxtApelldiom);
            usuario = myView.findViewById(R.id.duCEtxtUsuario);
            password = myView.findViewById(R.id.duCEtxtPassword);
            spinRol = myView.findViewById(R.id.duCSpnRoles);
            imgUser = myView.findViewById(R.id.duCimgVUsuario);
            imgBtnCamera = myView.findViewById(R.id.duCImgBtnCamara);
            imgBtnGallery = myView.findViewById(R.id.duCImgBtnGaleria);
            if (getArguments() != null) {
                accion = getArguments().getString("accion");

                switch (accion) {
                    case "M":

                        id_usuario.setText(getArguments().getString("id_usuario"));
                        nombre.setText(getArguments().getString("nombre"));
                        apellido_p.setText(getArguments().getString("apellido_p"));
                        apellido_m.setText(getArguments().getString("apellido_m"));
                        usuario.setText(getArguments().getString("usuario"));
                        password.setText(getArguments().getString("password"));
                        Picasso.with(getContext()).load(config.getUrlImages() + getArguments().getString("imagen")).fit().into(imgUser);
                        getRoles(new onResponse() {
                            @Override
                            public void roles(List<roles> roles) {
                                int contador=-1;
                                for (roles rol:listRoles) {
                                    contador ++;
                                    if (rol.getId_rol() .equals( getArguments().getString("id_rol"))) {
                                        spinRol.setSelection(contador);
                                    }
                                }
                            }
                        });


                        break;
                    case "N":
                        id_usuario.setText("...");
                        nombre.setText("");
                        apellido_p.setText("");
                        apellido_m.setText("");
                        usuario.setText("");
                        password.setText("");
                        imgUser.setImageResource(0);
                        getRoles(new onResponse() {
                            @Override
                            public void roles(List<roles> roles) {

                            }
                        });
                        break;
                } //fin de switch
            } //fin get arguments
        } //fin detalle fragment
    } //fin onViewCreated

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    private void getRoles(onResponse callBack) {
        interfaceRetrofit retrofit = config.getRetrofit().create(interfaceRetrofit.class);
        Call <List<roles>> call = retrofit.getRoles("actRoles");
        call.enqueue(new Callback<List<roles>>() {
            @Override
            public void onResponse(Call<List<roles>> call, Response<List<roles>> response) {
                ArrayList<String> data = new ArrayList<String>();
                listRoles = response.body();
                for (roles rol: listRoles) {
                    data.add(rol.getNombre());

                }
                ArrayAdapter<String> adapterRol = new ArrayAdapter<String>(getContext(), R.layout.bg_spin_text, data);
                adapterRol.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinRol.setAdapter(adapterRol);
                callBack.roles(response.body());
            }

            @Override
            public void onFailure(Call<List<roles>> call, Throwable t) {

            }
        });
    }

}