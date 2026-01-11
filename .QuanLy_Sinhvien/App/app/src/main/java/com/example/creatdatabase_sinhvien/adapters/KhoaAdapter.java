package com.example.creatdatabase_sinhvien.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Khoa;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách Khoa
 */
public class KhoaAdapter extends RecyclerView.Adapter<KhoaAdapter.KhoaViewHolder> {
    private List<Khoa> khoaList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Khoa khoa);
    }

    public KhoaAdapter(List<Khoa> khoaList) {
        this.khoaList = khoaList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Khoa> newList) {
        this.khoaList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public KhoaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_khoa, parent, false);
        return new KhoaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull KhoaViewHolder holder, int position) {
        Khoa khoa = khoaList.get(position);
        holder.bind(khoa);
    }

    @Override
    public int getItemCount() {
        return khoaList != null ? khoaList.size() : 0;
    }

    class KhoaViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTenKhoa, txtMaKhoa;

        public KhoaViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenKhoa = itemView.findViewById(R.id.txtTenKhoa);
            txtMaKhoa = itemView.findViewById(R.id.txtMaKhoa);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(khoaList.get(position));
                }
            });
        }

        public void bind(Khoa khoa) {
            // Tên khoa: Bold, Blue, Font lớn
            txtTenKhoa.setText(khoa.getTenKhoa() != null ? khoa.getTenKhoa() : "");
            
            // Mã khoa
            String maKhoa = khoa.getMaKhoa() != null ? khoa.getMaKhoa() : "";
            txtMaKhoa.setText("Mã: " + maKhoa);
        }
    }
}

