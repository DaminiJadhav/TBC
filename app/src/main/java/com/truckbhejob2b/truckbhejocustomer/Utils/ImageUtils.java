package com.truckbhejob2b.truckbhejocustomer.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static String imageBase64(Bitmap photo){
        // Log.d("TAG", "image Function call");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        File destination = new File(Environment.getExternalStorageDirectory() + File.separator + "TBV");

        if (!destination.exists()) {
            destination.mkdir();
            // Log.e("File","Folder Is Created ");
            FileOutputStream fo;
            try {
                File saveFile = new File(destination, "TBV" + System.currentTimeMillis() + ".PNG");
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
                File saveFile = new File(destination, "TBV" + System.currentTimeMillis() + ".PNG");
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
    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}
