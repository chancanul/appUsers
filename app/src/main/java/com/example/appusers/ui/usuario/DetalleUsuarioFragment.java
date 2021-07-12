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
import com.example.appusers.adaptadores.adapterSpiner;
import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.roles;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.interfaceRetrofit;
import com.example.appusers.retrofit.onResponseRoles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleUsuarioFragment extends Fragment implements View.OnClickListener{

    private DetalleUsuarioViewModel mViewModel;

    private View myView;
    private FloatingActionButton fabSave;
    private EditText nombre, apellido_p, apellido_m, usuario, password;
    private TextView id_usuario;
    private Spinner spinRol;
    private ImageView imgUser;
    private ImageButton imgBtnCamera, imgBtnGallery;
    private adapterSpiner fill;

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

            fabSave = detalleFragment.findViewById(R.id.fab);
            fabSave.setImageResource(R.drawable.save);
            fabSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

             //Llenar el spiner roles
            httpCall.getRoles(list -> {
                ArrayList<String> data = new ArrayList<String>();
                for (roles rol: list) {
                    data.add(rol.getNombre());
                }
                fill = new adapterSpiner(getContext(), spinRol,data);
                fill.fillSpiner();
                if (getArguments() != null) {
                    accion = getArguments().getString("accion");
                    switch (accion) {
                        case "M":
                            id_usuario.setText(getArguments().getString("id_usuario"));
                            int contador=-1;
                            for (roles rol:list) {
                                contador ++;
                                if (rol.getId_rol() .equals( getArguments().getString("id_rol"))) {
                                    spinRol.setSelection(contador);
                                }
                            }
                            nombre.setText(getArguments().getString("nombre"));
                            apellido_p.setText(getArguments().getString("apellido_p"));
                            apellido_m.setText(getArguments().getString("apellido_m"));
                            usuario.setText(getArguments().getString("usuario"));
                            password.setText(getArguments().getString("password"));
                            Picasso.with(getContext()).load(config.getUrlImages() + getArguments().getString("imagen")).fit().into(imgUser);
                            break;
                        case "N":
                            id_usuario.setText("...");
                            nombre.setText("");
                            apellido_p.setText("");
                            apellido_m.setText("");
                            usuario.setText("");
                            password.setText("");
                            imgUser.setImageResource(0);
                            break;
                    } //fin de switch
                } //fin get arguments
            }); //Fin OnResponse
        } //fin detalle fragment
    } //fin onViewCreated

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        
    }
}