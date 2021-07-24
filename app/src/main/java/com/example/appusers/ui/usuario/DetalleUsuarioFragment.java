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
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.example.appusers.MainActivity;
import com.example.appusers.R;
import com.example.appusers.adaptadores.adapterSpiner;
import com.example.appusers.configuracion.config;
import com.example.appusers.databinding.DetalleUsuarioFragmentBinding;
import com.example.appusers.modelos.roles;
import com.example.appusers.retrofit.httpCall;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
//Notas: el requiereContext() es la nueva manera de llamar a getContext()

import static android.app.Activity.RESULT_OK;

public class DetalleUsuarioFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    private UsuarioFragmentViewModel usuarioFragmentViewModel;
    private DetalleUsuarioFragmentBinding binding;
    private FloatingActionButton fabSave;
    private adapterSpiner fill;
    private Uri imageUri;
    ActivityResultLauncher<Intent> activityResultLauncherCamera;
    private Bitmap thumbnail;//Variable para almacenar el mapa de bits.
    private List<roles> listRoles;
    private String id_rol="";
    private String accion = "";

    public static DetalleUsuarioFragment newInstance() {
        return new DetalleUsuarioFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        usuarioFragmentViewModel = new ViewModelProvider(requireActivity()).get(UsuarioFragmentViewModel.class);
        binding = DetalleUsuarioFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
       // return inflater.inflate(R.layout.detalle_usuario_fragment, container, false);
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        MainActivity detalleFragment = (MainActivity) getActivity();
        if (detalleFragment != null) {
            fabSave = detalleFragment.findViewById(R.id.fab);
            fabSave.setImageResource(R.drawable.save);
            fabSave.setOnClickListener(this);
            binding.duCImgBtnGaleria.setOnClickListener(v->getImageGallery.launch("image/*"));
            binding.duCImgBtnCamara.setOnClickListener(this);
            binding.duCSpnRoles.setOnItemSelectedListener(this);
             //Llenar el spiner roles
            if (getArguments() != null) {
                accion = getArguments().getString("accion");
                usuarioFragmentViewModel.setAccion(accion);
            }

            usuarioFragmentViewModel.getItemRoles().observe(getViewLifecycleOwner(), itemRoles -> {
                fill = new adapterSpiner(getContext(), binding.duCSpnRoles,itemRoles);
                fill.fillSpiner();
            });
            /**
            usuarioFragmentViewModel.requestRoles().observe(getViewLifecycleOwner(), code -> {
                if (code.equals("200")) {
                    usuarioFragmentViewModel.getRoles().observe(getViewLifecycleOwner(), listRoles -> {
                        ArrayList<String> data = new ArrayList<String>();
                        for (roles rol: listRoles) {
                            data.add(rol.getNombre());
                        }
                            fill = new adapterSpiner(getContext(), binding.duCSpnRoles,data);
                            fill.fillSpiner();
                            if (getArguments() != null) {
                                accion = getArguments().getString("accion");
                                switch (accion) {
                                    case "M":
                                        usuarioFragmentViewModel.getIdUsuario().observe(getViewLifecycleOwner(), id_usuario -> {
                                            binding.duCEtxtid.setText(id_usuario);
                                        });
                                        usuarioFragmentViewModel.getNombre().observe(getViewLifecycleOwner(), nombre -> {
                                            binding.duCEtxtNombre.setText(nombre);
                                        });
                                        usuarioFragmentViewModel.getApellidoP().observe(getViewLifecycleOwner(), apellidop -> {
                                            binding.duCEtxtApellidop.setText(apellidop);
                                        });
                                        usuarioFragmentViewModel.getApellidoM().observe(getViewLifecycleOwner(), apellidom -> {
                                            binding.duCEtxtApelldiom.setText(apellidom);
                                        });
                                        usuarioFragmentViewModel.getIdUsuario().observe(getViewLifecycleOwner(), usuario -> {
                                            binding.duCEtxtUsuario.setText(usuario);
                                        });
                                        usuarioFragmentViewModel.getPassword().observe(getViewLifecycleOwner(), password -> {
                                            binding.duCEtxtPassword.setText(password);
                                        });
                                        usuarioFragmentViewModel.getImage().observe(getViewLifecycleOwner(), imagen -> {
                                            Picasso.with(getContext()).load(config.getUrlImages() + imagen).fit().into(binding.duCimgVUsuario);
                                        });
                                        usuarioFragmentViewModel.getRoles().observe(getViewLifecycleOwner(), list -> {
                                            usuarioFragmentViewModel.getIdRol().observe(getViewLifecycleOwner(), id_rol -> {
                                                int contador = -1;
                                                for (roles rol: list) {
                                                    contador++;
                                                    if (rol.getId_rol().equals(id_rol))
                                                        binding.duCSpnRoles.setSelection(contador);
                                                }
                                            });
                                });


                                        // usuarioFragmentViewModel.getSelected().observe(getViewLifecycleOwner(), item ->{
                                       //  binding.duCEtxtid.setText(item.getId_usuario());
                                       //  binding.duCEtxtNombre.setText(item.getNombre());
                                        // binding.duCEtxtApellidop.setText(item.getApellido_m());
                                       //  binding.duCEtxtUsuario.setText(item.getUsuario());
                                       //  binding.duCEtxtPassword.setText(item.getPassword());
                                       //  Picasso.with(getContext()).load(config.getUrlImages() + item.getImagen()).fit().into(binding.duCimgVUsuario);
                                       //  });

                                        break;
                                    case "N":
                                        binding.duCEtxtid.setText("");
                                        binding.duCEtxtNombre.setText("");
                                        binding.duCEtxtApellidop.setText("");
                                        binding.duCEtxtApelldiom.setText("");
                                        binding.duCEtxtUsuario.setText("");
                                        binding.duCEtxtPassword.setText("");
                                        binding.duCimgVUsuario.setImageResource(0);
                                        break;
                                } //fin de switch
                            } //fin get arguments

                    });
                }
            });
             **/
            activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        if (result.getResultCode() == RESULT_OK) {
                            usuarioFragmentViewModel.setImageBitMap(imageUri, getActivity()).observe(getViewLifecycleOwner(), bitMap -> {
                                binding.duCimgVUsuario.setImageBitmap(bitMap);
                            });
                            /**
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
                            binding.duCimgVUsuario.setImageBitmap(thumbnail);
                            //imgUser.setImageURI(imageUri);
                            **/
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
                        usuarioFragmentViewModel.setImageUri(result).observe(getViewLifecycleOwner(), imageUri -> {
                                binding.duCimgVUsuario.setImageURI(imageUri);
                        });
                    }
                }
            }
    );
    @Override
    public void onClick(View v) {
        //La sentencia switch en este caso a partir de la versión 7 ya no son definitivos por lo tanto para
        //tener un mejor rendimiento se utiliza if/else. Esto según la documentación oficial se puede utilizar
        //la sentencia Switch pero debemos tener en cuenta que pierde eficiencia como en bucles o renderizados
        int identifier = v.getId();
        if (R.id.fab == identifier) {
            if (accion.equals("N")) {
                //Construir los datos para mandar a la clase httpCall
                ContentValues container = config.containerBuid();
                container.put("id_rol", this.id_rol);
                container.put("nombre", binding.duCEtxtNombre.getText().toString().trim());
                container.put("apellido_p", binding.duCEtxtApellidop.getText().toString().trim());
                container.put("apellido_m", binding.duCEtxtApelldiom.getText().toString().trim());
                container.put("usuario", binding.duCEtxtUsuario.getText().toString().trim());
                container.put("password", binding.duCEtxtPassword.getText().toString().trim());
                binding.duCimgVUsuario.setDrawingCacheEnabled(true);
                binding.duCimgVUsuario.buildDrawingCache();
                Bitmap bit = ((BitmapDrawable) binding.duCimgVUsuario.getDrawable()).getBitmap();
                File imgFile = fileConverter(bit);
                /**
                httpCall.saveUser(list -> {
                    config.showMessageUser(v, "EL USUARIO " + list.get(0).getNombre() + " " + list.get(0).getApellido_p() + "" +
                            " " + list.get(0).getApellido_m() + " HA SIGO GUARDADO");
                    NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.nav_home);
                }, v, container, imgFile);
                 **/
            } else {
                ContentValues container = config.containerBuid();
                container.put("id_usuario", binding.duCEtxtid.getText().toString().trim());
                container.put("id_rol", this.id_rol);
                container.put("nombre", binding.duCEtxtNombre.getText().toString().trim());
                container.put("apellido_p", binding.duCEtxtApellidop.getText().toString().trim());
                container.put("apellido_m", binding.duCEtxtApelldiom.getText().toString().trim());
                container.put("usuario", binding.duCEtxtUsuario.getText().toString().trim());
                container.put("password", binding.duCEtxtPassword.getText().toString().trim());
                binding.duCimgVUsuario.setDrawingCacheEnabled(true);
                binding.duCimgVUsuario.buildDrawingCache();
                Bitmap bit = ((BitmapDrawable) binding.duCimgVUsuario.getDrawable()).getBitmap();
                File imgFile = fileConverter(bit);
                /**
                httpCall.updateUser(list -> {
                       config.showMessageUser(v, "Los datos del usuario No: " + list.get(0).getId_usuario());
                       NavHostFragment.findNavController(requireParentFragment()).navigate(R.id.nav_home);
                }, v, container,imgFile);
                 **/
            }
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
        Cursor cursor = requireActivity().getContentResolver().query(uri, projection, null, null, null);
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
            picture = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), recurso); //Alamacenar en la variable la imagen nueva guardada.
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
        File f = new File(requireContext().getCacheDir(), "ferchio");
        /**
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
         **/
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        usuarioFragmentViewModel.getRoles().observe(getViewLifecycleOwner(), listRoles -> {
            usuarioFragmentViewModel.setIdrol(listRoles.get(position).getId_rol());
        });

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}