package com.example.appusers.adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appusers.R;
import com.example.appusers.configuracion.config;
import com.example.appusers.databinding.DetalleUsuarioFragmentBinding;
import com.example.appusers.databinding.FragmentUsuarioBinding;
import com.example.appusers.databinding.ViewUsersBinding;
import com.example.appusers.modelos.usuarios;
import com.example.appusers.ui.usuario.DetalleUsuarioFragment;
import com.example.appusers.ui.usuario.UsuarioFragment;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class adapterusuario extends RecyclerView.Adapter<adapterusuario.customViewHolder> {
    private ViewUsersBinding binding;
    Context contexto;
    List<usuarios> listado;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void onClick(View v, int position);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public usuarios getItemAt(int position) {
        return listado.get(position);
    }

    public adapterusuario(Context contexto, List listado) {
        this.contexto = contexto;
        this.listado = listado;
    }
    @NonNull
    @NotNull
    @Override
    public customViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        binding = ViewUsersBinding.inflate(inflater, parent, false);
        return new customViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull customViewHolder holder, int position) {
        String image = config.getUrlImages();
        holder.binding.txtVUCId.setText(listado.get(position).getId_usuario());
        holder.binding.txtVUCNombre.setText(listado.get(position).getNombre());
        holder.binding.txtVUCApAm.setText(listado.get(position).getApellido_p());
        holder.binding.txtVUCRol.setText(listado.get(position).getRoles().getNombre());
        Picasso.with(contexto).load(image + listado.get(position).getImagen()).fit().into(holder.binding.imgVUser);
    }

    @Override
    public int getItemCount() {
        return listado.size();
    }
     //Este es el evento onclick del implement View.OnClickListener de la clase.
    //La variable listener es definida a nivel de la clase
    //@Override
    //public void onClick(View v) {
      //if(listener != null) {
         // listener.onClick(v);
     // }
    //}
   // public void setOnClickListener (View.OnClickListener listener) {
       // this.listener = listener;
    //}
    //Esta clase es la primera en definirse, su objetivo es vincular la vista view_users con la
    //clase adapter es decir esta clase
    public class customViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Declarar los controles a utilizar
        private ViewUsersBinding binding;
        public customViewHolder(ViewUsersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            listener.onClick(v, getAdapterPosition());
        }
    }
}
