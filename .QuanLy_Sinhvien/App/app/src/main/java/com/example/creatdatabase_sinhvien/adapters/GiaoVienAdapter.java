package com.example.creatdatabase_sinhvien.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.GiaoVien;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách giáo viên
 */
public class GiaoVienAdapter extends RecyclerView.Adapter<GiaoVienAdapter.ViewHolder> {
    private final Context context;
    private List<GiaoVien> giaoVienList;
    private OnItemClickListener listener;
    private static final String BASE_URL = "https://nguyenha-001-site1.ltempurl.com";

    public interface OnItemClickListener {
        void onItemClick(GiaoVien giaoVien);
    }

    public GiaoVienAdapter(Context context, List<GiaoVien> giaoVienList) {
        this.context = context;
        this.giaoVienList = giaoVienList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_giaovien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GiaoVien gv = giaoVienList.get(position);
        
        if (gv != null) {
            // Hiển thị tên với học hàm/học vị
            String displayName = "";
            if (gv.getHocVi() != null && !gv.getHocVi().isEmpty()) {
                displayName = gv.getHocVi() + ". ";
            }
            if (gv.getHocHam() != null && !gv.getHocHam().isEmpty()) {
                displayName += gv.getHocHam() + ". ";
            }
            displayName += (gv.getHoten() != null ? gv.getHoten() : "");
            holder.tvHoTen.setText(displayName);
            
            // Hiển thị khoa
            String khoaText = "Khoa: " + (gv.getTenKhoa() != null ? gv.getTenKhoa() : "");
            holder.tvKhoa.setText(khoaText);
            
            // Load ảnh từ URL
            String imagePath = gv.getAnh();
            if (imagePath != null && !imagePath.isEmpty()) {
                // Ghép BASE_URL với đường dẫn ảnh từ API
                // API trả về: /images/giaovien/img2.jpg
                // Kết quả: https://nguyenha-001-site1.ltempurl.com/images/giaovien/img2.jpg
                String fullImageUrl;
                if (imagePath.startsWith("/")) {
                    fullImageUrl = BASE_URL + imagePath;
                } else {
                    fullImageUrl = BASE_URL + "/" + imagePath;
                }
                
                android.util.Log.d("GiaoVienAdapter", "Loading image from URL: " + fullImageUrl);
                
                Glide.with(context)
                    .load(fullImageUrl)
                    .circleCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.ic_person_placeholder) // Placeholder hình người xám
                    .error(R.drawable.ic_person_placeholder) // Ảnh lỗi - hiển thị placeholder
                    .fallback(R.drawable.ic_person_placeholder) // Fallback nếu URL null
                    .into(holder.imgAvatar);
            } else {
                // Hiển thị ảnh mặc định nếu không có ảnh
                android.util.Log.d("GiaoVienAdapter", "No image path, using placeholder");
                Glide.with(context)
                    .load(R.drawable.ic_person_placeholder)
                    .circleCrop()
                    .into(holder.imgAvatar);
            }
            
            // Xử lý click
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(gv);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return giaoVienList != null ? giaoVienList.size() : 0;
    }

    public void updateList(List<GiaoVien> newList) {
        this.giaoVienList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView tvHoTen;
        TextView tvKhoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgGiaoVienAvatar);
            tvHoTen = itemView.findViewById(R.id.tvGiaoVienHoTen);
            tvKhoa = itemView.findViewById(R.id.tvGiaoVienKhoa);
        }
    }
}

