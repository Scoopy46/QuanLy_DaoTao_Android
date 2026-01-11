package com.example.creatdatabase_sinhvien.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import java.io.File;

/**
 * Utility class để xử lý ảnh
 */
public class ImageHelper {
    private static final String TAG = "ImageHelper";
    private static final String IMAGE_DIR = "images";

    /**
     * Copy ảnh từ assets vào thư mục internal storage
     */
    public static void copyImagesFromAssets(Context context) {
        try {
            String[] imageFiles = context.getAssets().list(IMAGE_DIR);
            if (imageFiles == null || imageFiles.length == 0) {
                Log.w(TAG, "No images found in assets/images");
                return;
            }

            File imageDir = new File(context.getFilesDir(), IMAGE_DIR);
            if (!imageDir.exists()) {
                imageDir.mkdirs();
            }

            for (String fileName : imageFiles) {
                File destFile = new File(imageDir, fileName);
                if (!destFile.exists()) {
                    try (java.io.InputStream is = context.getAssets().open(IMAGE_DIR + "/" + fileName);
                         java.io.FileOutputStream fos = new java.io.FileOutputStream(destFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
            }
        } catch (java.io.IOException e) {
            Log.e(TAG, "Error copying images from assets", e);
        }
    }

    /**
     * Lấy đường dẫn thư mục ảnh
     */
    public static String getImageDirectoryPath(Context context) {
        File imageDir = new File(context.getFilesDir(), IMAGE_DIR);
        return imageDir.getAbsolutePath();
    }

    /**
     * Load ảnh vào ImageView
     */
    public static void loadImage(Context context, ImageView imageView, String imagePath) {
        if (imagePath == null || imagePath.isEmpty()) {
            imageView.setImageBitmap(null);
            return;
        }

        try {
            Bitmap bitmap = null;
            
            // Nếu là đường dẫn file local đầy đủ
            if (imagePath.startsWith("/")) {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                } else {
                    // Thử tìm trong thư mục images của app
                    String fileName = new File(imagePath).getName();
                    File localFile = new File(context.getFilesDir(), IMAGE_DIR + "/" + fileName);
                    if (localFile.exists()) {
                        bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    }
                }
            } else {
                // Nếu chỉ có tên file (hoặc đường dẫn tương đối), tìm trong thư mục images
                String fileName = imagePath;
                // Nếu có chứa "/", lấy tên file cuối cùng
                if (fileName.contains("/")) {
                    fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                }
                File localFile = new File(context.getFilesDir(), IMAGE_DIR + "/" + fileName);
                if (localFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                }
            }

            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                imageView.setImageResource(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading image: " + imagePath, e);
            imageView.setImageResource(0);
        }
    }
}

