package com.example.appusers.ui.usuario;

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

import com.example.appusers.MainActivity;
import com.example.appusers.R;
import com.example.appusers.adaptadores.adapterusuario;
import com.example.appusers.databinding.FragmentUsuarioBinding;
import com.example.appusers.modelos.usuarios;
import com.example.appusers.retrofit.httpCall;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UsuarioFragment extends Fragment {

    private UsuarioFragmentViewModel usuarioFragmentViewModel;
    private DetalleUsuarioViewModel detalleUsuarioViewModel;
    private FragmentUsuarioBinding binding;
    private adapterusuario myAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        usuarioFragmentViewModel =
                new ViewModelProvider(requireActivity()).get(UsuarioFragmentViewModel.class);
        detalleUsuarioViewModel =
                new ViewModelProvider(requireActivity()).get(DetalleUsuarioViewModel.class);
        binding = FragmentUsuarioBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textHome;
        usuarioFragmentViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
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
         mostrarRecycler();
        //Obtener el MainActivity para poder manipular el floatingActionButton
        final MainActivity navigation = (MainActivity) getActivity();
        //Antes de manipular al mainActivity debemos comprobar que no sea nulo.
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
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    public void mostrarRecycler() {
        usuarioFragmentViewModel.requestUsuarios().observe(getViewLifecycleOwner(), s -> {
            if (s.equals("200")) {
                usuarioFragmentViewModel.getUsers().observe(getViewLifecycleOwner(), usuarios -> {
                    myAdapter = new adapterusuario(getContext(), usuarios);
                    binding.recvCUsuarios.setLayoutManager(new LinearLayoutManager(getContext()));
                    binding.recvCUsuarios.setAdapter(myAdapter);
                    myAdapter.setListener((v, position) -> {
                        Bundle accion =  new Bundle();
                        accion.putString("accion", "M");
                        detalleUsuarioViewModel.setSelected(myAdapter.getItemAt(position));
                        NavHostFragment.findNavController(getParentFragment()).navigate(R.id.detalleUsuarioFragment, accion);
                    }); //fin Myadapter.setListener
                }); //End observe getUsers
            } //fin de s.equals
        });
    }
}