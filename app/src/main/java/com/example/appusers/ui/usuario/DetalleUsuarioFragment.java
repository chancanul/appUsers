package com.example.appusers.ui.usuario;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

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
    private Uri imageUri;
    private static final int _IMAGE_CAPTURE = 100;
    private static final int _PICK_GALLERY = 100;
    ActivityResultLauncher<Intent> activityResultLauncherCamera;


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
            fabSave.setOnClickListener(this);
            imgBtnGallery.setOnClickListener(v -> getImageGallery.launch("image/*"));
            imgBtnCamera.setOnClickListener(this);


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

            activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                           imgUser.setImageURI(imageUri);
                        }
                    });
        } //fin detalle fragment
    } //fin onViewCreated

    ActivityResultLauncher<String> getImageGallery = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        imgUser.setImageURI(result);
                    }
                }
            }
    );
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DetalleUsuarioViewModel.class);
        // TODO: Use the ViewModel
    }



    @Override
    public void onClick(View v) {
        //La sentencia switch en este caso a partir de la versión 7 ya no son definitivos por lo tanto para
        //tener un mejor rendimiento se utiliza if/else. Esto según la documentación oficial se puede utilizar
        //la sentencia Switch pero debemos tener en cuenta que pierde eficiencia como en bucles o renderizados
        /**
        switch (v.getId()) {
            case R.id.fab:
                break;
            case R.id.duCImgBtnCamara:

                break;
            case R.id.duCImgBtnGaleria:

                break;
        }
         **/
        int identifier = v.getId();
        if (R.id.fab == identifier) {
            }  else if (R.id.duCImgBtnCamara == identifier) {
            //String timeStamp = new SimpleDateFormat("yyyyMMdd.HHmmss").format(new Date());
            //String imageFilename = "JPEG_" + timeStamp + "_";
            //File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            //try {
              // File image = File.createTempFile(
                    //    imageFilename,
                    //   ".jpg",
                    //    storageDir
               // );
               // String currentPhotoPath = image.getAbsolutePath();
               // Intent takepictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              // Uri photoUri = FileProvider.getUriForFile(getContext(), "com.example.appusers", image);
              //  takepictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
               // activityResultLauncherCamera.launch(takepictureIntent);
           // } catch (IOException e) {
            //    e.printStackTrace();
            //}

            ContentValues descriptions = new ContentValues();
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            descriptions.put(MediaStore.Images.Media.TITLE, "My imagen");
            descriptions.put(MediaStore.Images.Media.DESCRIPTION, "Photo Taken On" + System.currentTimeMillis());
            imageUri = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, descriptions);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            activityResultLauncherCamera.launch(intent);
            }  else if (R.id.duCImgBtnGaleria == identifier) {
            // Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);//Intent para abrir la galería.
               //  activityResultLauncherCamera.launch(galeria);
            }
    } // fin del evento onclick


}