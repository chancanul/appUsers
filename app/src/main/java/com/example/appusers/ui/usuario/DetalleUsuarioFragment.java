package com.example.appusers.ui.usuario;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.lifecycle.ViewModelProvider;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.example.appusers.modelos.usuarios;
import com.example.appusers.retrofit.httpCall;
import com.example.appusers.retrofit.onResponseUsuarios;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//Notas: el requiereContext() es la nueva manera de llamar a getContext()

import static android.app.Activity.RESULT_OK;

public class DetalleUsuarioFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener{

    private DetalleUsuarioViewModel mViewModel;

    private View myView;
    private FloatingActionButton fabSave;
    private EditText eTxtNombre, eTxtApellido_p, eTxtApellido_m, eTxtUsuario, eTxtPassword;
    private TextView id_usuario;
    private Spinner spinRol;
    private ImageView imgUser;
    private ImageButton imgBtnCamera, imgBtnGallery;
    private adapterSpiner fill;
    private Uri imageUri;
    private static final int _IMAGE_CAPTURE = 100;
    private static final int _PICK_GALLERY = 100;
    ActivityResultLauncher<Intent> activityResultLauncherCamera;
    private String mCurrentPhotoPhat;
    private Bitmap thumbnail;//Variable para almacenar el mapa de bits.
    private List<usuarios> listUsuarios;
    private List<roles> listRoles;
    private String id_rol="";



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
            eTxtNombre = myView.findViewById(R.id.duCEtxtNombre);
            eTxtApellido_p = myView.findViewById(R.id.duCEtxtApellidop);
            eTxtApellido_m = myView.findViewById(R.id.duCEtxtApelldiom);
            eTxtUsuario = myView.findViewById(R.id.duCEtxtUsuario);
            eTxtPassword = myView.findViewById(R.id.duCEtxtPassword);
            spinRol = myView.findViewById(R.id.duCSpnRoles);
            imgUser = myView.findViewById(R.id.duCimgVUsuario);
            imgBtnCamera = myView.findViewById(R.id.duCImgBtnCamara);
            imgBtnGallery = myView.findViewById(R.id.duCImgBtnGaleria);

            fabSave = detalleFragment.findViewById(R.id.fab);
            fabSave.setImageResource(R.drawable.save);
            fabSave.setOnClickListener(this);
            imgBtnGallery.setOnClickListener(v -> getImageGallery.launch("image/*"));
            imgBtnCamera.setOnClickListener(this);
            spinRol.setOnItemClickListener(this);

             //Llenar el spiner roles
            httpCall.getRoles(list -> {
                this.listRoles = list; //para poder manipular los roles en esta clase.
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
                            eTxtNombre.setText(getArguments().getString("nombre"));
                            eTxtApellido_p.setText(getArguments().getString("apellido_p"));
                            eTxtApellido_m.setText(getArguments().getString("apellido_m"));
                            eTxtUsuario.setText(getArguments().getString("usuario"));
                            eTxtPassword.setText(getArguments().getString("password"));
                            Picasso.with(getContext()).load(config.getUrlImages() + getArguments().getString("imagen")).fit().into(imgUser);
                            break;
                        case "N":
                            id_usuario.setText("...");
                            eTxtNombre.setText("");
                            eTxtApellido_p.setText("");
                            eTxtApellido_m.setText("");
                            eTxtUsuario.setText("");
                            eTxtPassword.setText("");
                            imgUser.setImageResource(0);
                            break;
                    } //fin de switch
                } //fin get arguments
            }); //Fin OnResponse

            activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            int rotate = 0;
                            try {
                                ExifInterface exif = new ExifInterface(Objects.requireNonNull(getPath(imageUri)));
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
                            thumbnail = getGuardarImagen(imageUri, rotate);
                            //Asignar la imagen al ImageView del layout.
                            imgUser.setImageBitmap(thumbnail);
                            //imgUser.setImageURI(imageUri);
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
            //Construir los datos para mandar a la clase httpCall
            ContentValues container = config.containerBuid();
            container.put("nombre", eTxtNombre.getText().toString().trim());
            container.put("apellido_p", eTxtApellido_p.getText().toString().trim());
            container.put("apellido_m", eTxtApellido_m.getText().toString().trim());
            container.put("usuario", eTxtUsuario.getText().toString().trim());
            container.put("password", eTxtPassword.getText().toString().trim());

            imgUser.setDrawingCacheEnabled(true);
            imgUser.buildDrawingCache();
            Bitmap bit = ((BitmapDrawable) imgUser.getDrawable()).getBitmap();
            File imgFile = fileConverter(bit);
            container.put("imagen", String.valueOf(imgFile));
            httpCall.saveUser(list -> {
                config.showMessageUser(v, "EL USUARIO " + list.get(0).getNombre() + " " + list.get(0).getApellido_p() + "" +
                        " " + list.get(0).getApellido_m() + " HA SIGO GUARDADO");
            },v, container, imgFile);



            }  else if (R.id.duCImgBtnCamara == identifier) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                ContentValues descriptions = new ContentValues();
                descriptions.put(MediaStore.Images.Media.TITLE, "My imagen");
                descriptions.put(MediaStore.Images.Media.DESCRIPTION, "Photo Taken On" + System.currentTimeMillis());
                imageUri = requireContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, descriptions); //El requireContext es utilizado en fragmentos en ces de gertContext()
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                activityResultLauncherCamera.launch(intent);
            }  else if (R.id.duCImgBtnGaleria == identifier) {
                Intent galeria = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);//Intent para abrir la galería.
                activityResultLauncherCamera.launch(galeria);
            }
    } // fin del evento onclick

    /**
     * Función para traer la ruta (path) del recurso (Uri)
     * @param uri recurso path del archivo
     * @return ruta del archivo.
     */
    private String getPath(Uri uri) {
        //Arreglo para procesar la ruta por medio de un cursor.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(colum_index);
        cursor.close();
        return s; // Devuelve la ruta.
    } //Fin de getPath()
    /**
     * Funcióm getGuardarImagen, desompone una imagen en una matriz para luego procesarla en un mapa de bits
     * Devuelve un bitMap con la imagen comprimida.
     * @param recurso
     * @param giro
     * @return
     */
    private Bitmap getGuardarImagen(Uri recurso, int giro) {
        String path = "";
        Bitmap picture = null;
        File imagen;
        try { //Del getBitMap
            path = getPath(recurso); //Traer la ruta del recurso Uri.
            picture = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), recurso); //Alamacenar en la variable la imagen nueva guardada.
                if (path != null) {
                    imagen = new File(path); //Crear
                    FileOutputStream salida = new FileOutputStream(imagen); //Convetir en bytes.
                    picture.compress(Bitmap.CompressFormat.JPEG, 50, salida); //Comprimir.
                    salida.flush();//Limpiar los recursos de memoria
                    salida.close(); //Cerrar el archivo.
                    Matrix matrix = new Matrix(); //Nueva imagen de bits.
                    matrix.postRotate(giro);//Aplicar la rotación de la imagen ya comprimida.
                    //Armar nuevamente la imaen en Bitmap.
                    picture = Bitmap.createBitmap(picture,
                            0,
                            0 , picture.getWidth(),
                            picture.getHeight(),
                            matrix,
                            true);
                }
        } catch (Exception e) {
            Log.e("El siguiente error ha ocurrido: ", e.getMessage());
        } //fin de try getBitmap

        return picture; //Retornar la imagen en mapa de Bits.
    } //Fin de getGuardar()

    private File fileConverter(Bitmap bitmap) {
        //Crear un nuevo archivo para escribir dentro el mapa de bits.
        File f = new File(getContext().getCacheDir(), "ferchio");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Convertir el bitMap a un arreglo de bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,bos);
        byte[] bitmapdata = bos.toByteArray();
        //Escribir mapa de bits a array de bytes.
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(f);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    } //Fin de fileConverter.

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        id_rol = listRoles.get(position).getId_rol();
    }
}