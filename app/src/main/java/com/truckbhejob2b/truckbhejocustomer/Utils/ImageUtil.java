package com.truckbhejob2b.truckbhejocustomer.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Base64OutputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageUtil {

    public static Bitmap convert(String base64Str) throws IllegalArgumentException
    {
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",")  + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static String convert(Bitmap bitmap)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        //Log.d("TAG", "convert: "+Base64.encodeToString(imageBytes, Base64.DEFAULT));
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);


    }
    public static String imageBase64(Bitmap photo){
       // Log.d("TAG", "image Function call");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory() + File.separator + "TBM");

        if (!destination.exists()) {
            destination.mkdir();
            // Log.e("File","Folder Is Created ");
            FileOutputStream fo;
            try {
                File saveFile = new File(destination, "TBM" + System.currentTimeMillis() + ".PNG");
                saveFile.createNewFile();
                fo = new FileOutputStream(saveFile);
                fo.write(bytes.toByteArray());
                fo.close();
               // Log.e("File","File Is Save ");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            FileOutputStream fo;
            try {
                File saveFile = new File(destination, "TBM" + System.currentTimeMillis() + ".PNG");
                saveFile.createNewFile();
                fo = new FileOutputStream(saveFile);
                fo.write(bytes.toByteArray());
                fo.close();
                 // Log.e("File","File Is Save ");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String base64 = getStringImage(photo);
       // Log.d("TAG", "imageBase64: "+base64);
        return base64;
    }
    private static String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
      //  Log.d("TAG", "getStringImage: "+Base64.encodeToString(imageBytes, Base64.DEFAULT));
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public static String getStringFile(File f) {
//        Log.d("Tag", "getStringFile: "+f.getAbsolutePath());
        InputStream inputStream = null;
        String encodedFile= "", lastVal;
        try {
            inputStream = new FileInputStream(f.getAbsolutePath());

            byte[] buffer = new byte[10240];//specify the size to allow
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            Base64OutputStream output64 = new Base64OutputStream(output, Base64.DEFAULT);

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output64.write(buffer, 0, bytesRead);
            }
            output64.close();
            encodedFile =  output.toString();
        }
        catch (FileNotFoundException e1 ) {
            e1.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        lastVal = encodedFile;
        return lastVal;
    }
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


}



