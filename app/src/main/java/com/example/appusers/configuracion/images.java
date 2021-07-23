package com.example.appusers.configuracion;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.io.File;
import java.io.FileOutputStream;

public class images {
    public static String getPath(Uri uri, FragmentActivity fragment) {
        //Arreglo para procesar la ruta por medio de un cursor.
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = fragment.getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int colum_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s = cursor.getString(colum_index);
        cursor.close();
        return s; // Devuelve la ruta.
    }

    /**
     * Funcióm getGuardarImagen, desompone una imagen en una matriz para luego procesarla en un mapa de bits
     * Devuelve un bitMap con la imagen comprimida.
     * @param recurso
     * @param giro
     * @return
     */
    public static Bitmap getGuardarImagen(Uri recurso, int giro, FragmentActivity fragment) {
        String path = "";
        Bitmap picture = null;
        File imagen;
        try { //Del getBitMap
            path = getPath(recurso,fragment); //Traer la ruta del recurso Uri.
            picture = MediaStore.Images.Media.getBitmap(fragment.getContentResolver(), recurso); //Alamacenar en la variable la imagen nueva guardada.
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
}
