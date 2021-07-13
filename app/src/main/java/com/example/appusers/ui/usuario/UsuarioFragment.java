package com.example.appusers.ui.usuario;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appusers.MainActivity;
import com.example.appusers.R;
import com.example.appusers.adaptadores.adapterusuario;
import com.example.appusers.configuracion.config;
import com.example.appusers.databinding.FragmentUsuarioBinding;
import com.example.appusers.modelos.usuarios;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.interfaceRetrofit;
import com.example.appusers.retrofit.onResponseUsuarios;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioFragment extends Fragment {

    private UsuarioViewModel usuarioViewModel;
    private FragmentUsuarioBinding binding;

    private RecyclerView myRecycler;
    private View myView;
    private adapterusuario myAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usuarioViewModel =
                new ViewModelProvider(this).get(UsuarioViewModel.class);

        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        usuarioViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Obtener el MainActivity para poder manipular el floatingActionButton
        final MainActivity navigation = (MainActivity) getActivity();
        myView = view;
        //Antes de manipular al mainActivituy debemos comprobar que no sea nulo.
        if (navigation != null) {
            FloatingActionButton fabactionUser = navigation.findViewById(R.id.fab);
            fabactionUser.setImageResource(R.drawable.guardar_usuario);
            fabactionUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle accion =  new Bundle();
                    accion.putString("accion", "N");
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.detalleUsuarioFragment, accion);
                }
            });
        }

        mostrarRecycler();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //private void getUsuarios() {
        //Variable para iniciar la petición Retrofit
      //  interfaceRetrofit peticion = config.getRetrofit().create(interfaceRetrofit.class);
        //Preparar la petición call (llamar)
      //  Call<List<usuarios>> call = peticion.getusuarios("actUser");
        //Iniciar la petición con enqueue. el método incluye dos apartados para saber si la petición se llevó con éxito o fracaso
        //onResponse-onFailure
        //call.enqueue(new Callback<List<usuarios>>() {
          //  @Override
         //   public void onResponse(Call<List<usuarios>> call, Response<List<usuarios>> response) {
                //En caso de éxito
                //la variable response es la encargada de almacenar la respuesta del servidor.
              //  mostrarRecycler(response.body());
           // }
           // @Override
           // public void onFailure(Call<List<usuarios>> call, Throwable t) {
                //en caso de fracaso

             //   Snackbar msjPersonalizado = Snackbar.make(myView, "Servidor inaccesible", Snackbar.LENGTH_SHORT);
              //  msjPersonalizado.show();
           // }
       // });
   // }  //fin get usuarios

    public void mostrarRecycler() {

        myRecycler = (RecyclerView) myView.findViewById(R.id.recvCUsuarios); //Definiendo el recycler
        httpCall.getUsuarios(list -> {
            myAdapter = new adapterusuario(getContext(), list); //Construyendo el adaptador
            myRecycler.setLayoutManager(new LinearLayoutManager(getContext())); //Agregando un contraintLayout al recicler
            myRecycler.setAdapter(myAdapter);//Volcar los datos al recycler
            //Generar la función clic del adaptador, implementado en el adaptador
            myAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle datosUsuario = new Bundle();
                    String id_usuario = list.get(myRecycler.getChildLayoutPosition(v)).getId_usuario();
                    String id_rol = list.get(myRecycler.getChildLayoutPosition(v)).getId_rol();
                    String nombre = list.get(myRecycler.getChildLayoutPosition(v)).getNombre();
                    String apellido_p = list.get(myRecycler.getChildLayoutPosition(v)).getApellido_p();
                    String apellido_m = list.get(myRecycler.getChildLayoutPosition(v)).getApellido_m();
                    String usuario = list.get(myRecycler.getChildLayoutPosition(v)).getUsuario();
                    String password = list.get(myRecycler.getChildLayoutPosition(v)).getPassword();
                    String imagen = list.get(myRecycler.getChildLayoutPosition(v)).getImagen();
                    String  rol = list.get(myRecycler.getChildLayoutPosition(v)).getRoles().getNombre();
                    datosUsuario.putString("id_usuario", id_usuario);
                    datosUsuario.putString("id_rol", id_rol);
                    datosUsuario.putString("nombre", nombre);
                    datosUsuario.putString("apellido_p", apellido_p);
                    datosUsuario.putString("apellido_m", apellido_m);
                    datosUsuario.putString("usuario", usuario);
                    datosUsuario.putString("password", password);
                    datosUsuario.putString("imagen", imagen);
                    datosUsuario.putString("rol", rol);
                    datosUsuario.putString("accion", "M");
                    NavHostFragment.findNavController(getParentFragment()).navigate(R.id.detalleUsuarioFragment, datosUsuario);
                } //fin de onclick()
            }); //fin de onclick listener

        }, myView); //fin de onResponseUsuarios

    }

}