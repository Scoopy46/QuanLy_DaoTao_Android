package com.example.creatdatabase_sinhvien.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Nganh;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách Ngành
 */
public class NganhAdapter extends RecyclerView.Adapter<NganhAdapter.NganhViewHolder> {
    private List<Nganh> nganhList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Nganh nganh);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(Nganh nganh);
    }

    public NganhAdapter(List<Nganh> nganhList) {
        this.nganhList = nganhList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void updateList(List<Nganh> newList) {
        this.nganhList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NganhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_nganh, parent, false);
        return new NganhViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NganhViewHolder holder, int position) {
        Nganh nganh = nganhList.get(position);
        holder.bind(nganh);
    }

    @Override
    public int getItemCount() {
        return nganhList != null ? nganhList.size() : 0;
    }

    class NganhViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTenNganh, txtMaNganh, txtKhoa;

        public NganhViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenNganh = itemView.findViewById(R.id.txtTenNganh);
            txtMaNganh = itemView.findViewById(R.id.txtMaNganh);
            txtKhoa = itemView.findViewById(R.id.txtKhoa);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(nganhList.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                    return longClickListener.onItemLongClick(nganhList.get(position));
                }
                return false;
            });
        }

        public void bind(Nganh nganh) {
            // Tên Ngành: Bold, Blue
            txtTenNganh.setText(nganh.getTenNganh() != null ? nganh.getTenNganh() : "");
            
            // Mã Ngành
            String maNganh = nganh.getMaNganh() != null ? nganh.getMaNganh() : "";
            txtMaNganh.setText("Mã: " + maNganh);
            
            // Khoa: Hiển thị tên khoa nếu có, nếu không thì hiển thị mã khoa
            String khoaText = "";
            if (nganh.getTenKhoa() != null && !nganh.getTenKhoa().isEmpty()) {
                khoaText = "Khoa: " + nganh.getTenKhoa();
            } else if (nganh.getMaKhoa() != null && !nganh.getMaKhoa().isEmpty()) {
                khoaText = "Khoa: " + nganh.getMaKhoa();
            } else {
                khoaText = "Khoa: -";
            }
            txtKhoa.setText(khoaText);
        }
    }
}

