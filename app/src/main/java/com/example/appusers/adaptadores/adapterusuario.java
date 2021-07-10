package com.example.appusers.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appusers.R;
import com.example.appusers.configuracion.config;
import com.example.appusers.modelos.usuarios;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class adapterusuario extends RecyclerView.Adapter<adapterusuario.customViewHolder> implements View.OnClickListener {
    Context contexto;
    List<usuarios> listado;
    private View myView;

    public adapterusuario(Context contexto, List listado) {
        this.contexto = contexto;
        this.listado = listado;
    }
    @NonNull
    @NotNull
    @Override
    public customViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater mostrarControl = LayoutInflater.from(parent.getContext());
        myView = mostrarControl.inflate(R.layout.view_users, parent, false);
        myView.setOnClickListener(this);
        return new customViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull customViewHolder holder, int position) {
        String image = config.getUrlImages();
        holder.rcvId.setText(listado.get(position).getId_usuario());
        holder.rcvNombre.setText(listado.get(position).getNombre());
        holder.rcvApellido.setText(listado.get(position).getApellido_p());
        holder.rcvRol.setText(listado.get(position).getRoles().getNombre());
        Picasso.with(contexto).load(image + listado.get(position).getImagen()).fit().into(holder.rcvImgagen);
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }

    @Override
    public void onClick(View v) {

    }
    public class customViewHolder extends RecyclerView.ViewHolder {
        //Declarar los controles a utilizar
        TextView rcvId, rcvNombre, rcvApellido, rcvRol;
        ImageView rcvImgagen;
        final View myView;
        public customViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            myView = itemView;
            rcvId = myView.findViewById(R.id.txtVUCId);
            rcvNombre = myView.findViewById(R.id.txtVUCNombre);
            rcvApellido = myView.findViewById(R.id.txtVUCApAm);
            rcvRol = myView.findViewById(R.id.txtVUCRol);
            rcvImgagen = myView.findViewById(R.id.imgVUser);
        }
    }
}
