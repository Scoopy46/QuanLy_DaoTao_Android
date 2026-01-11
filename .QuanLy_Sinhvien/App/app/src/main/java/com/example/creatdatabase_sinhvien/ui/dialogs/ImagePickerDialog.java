package com.example.creatdatabase_sinhvien.ui.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.example.creatdatabase_sinhvien.utils.ImageHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Dialog để chọn ảnh từ thư mục images
 */
public class ImagePickerDialog {
    private final Context context;
    private AlertDialog dialog;
    private OnImageSelectedListener listener;

    public interface OnImageSelectedListener {
        void onImageSelected(String imageName, String imagePath);
    }

    public ImagePickerDialog(Context context) {
        this.context = context;
    }

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.listener = listener;
    }

    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Chọn ảnh");

        File imageDir = new File(context.getFilesDir(), "images");
        List<String> imageFiles = new ArrayList<>();

        if (imageDir.exists() && imageDir.isDirectory()) {
            File[] files = imageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && isImageFile(file.getName())) {
                        imageFiles.add(file.getName());
                    }
                }
            }
        }

        if (imageFiles.isEmpty()) {
            builder.setMessage("Không có ảnh nào trong thư mục");
            builder.setPositiveButton("Đóng", null);
            dialog = builder.create();
            dialog.show();
            return;
        }

        GridView gridView = new GridView(context);
        gridView.setNumColumns(3);
        gridView.setAdapter(new ImageGridAdapter(context, imageFiles));

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            String imageName = imageFiles.get(position);
            String imagePath = new File(imageDir, imageName).getAbsolutePath();
            if (listener != null) {
                listener.onImageSelected(imageName, imagePath);
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        builder.setView(gridView);
        builder.setNegativeButton("Hủy", null);
        dialog = builder.create();
        dialog.show();
    }

    private boolean isImageFile(String fileName) {
        String lower = fileName.toLowerCase();
        return lower.endsWith(".jpg") || lower.endsWith(".jpeg") || 
               lower.endsWith(".png") || lower.endsWith(".gif") || 
               lower.endsWith(".bmp") || lower.endsWith(".webp");
    }

    private static class ImageGridAdapter extends BaseAdapter {
        private final Context context;
        private final List<String> imageFiles;

        public ImageGridAdapter(Context context, List<String> imageFiles) {
            this.context = context;
            this.imageFiles = imageFiles;
        }

        @Override
        public int getCount() {
            return imageFiles.size();
        }

        @Override
        public Object getItem(int position) {
            return imageFiles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            String imageName = imageFiles.get(position);
            File imageDir = new File(context.getFilesDir(), "images");
            File imageFile = new File(imageDir, imageName);
            
            if (imageFile.exists()) {
                ImageHelper.loadImage(context, imageView, imageFile.getAbsolutePath());
            }

            return imageView;
        }
    }
}

