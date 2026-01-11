package com.example.creatdatabase_sinhvien.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.MonHoc;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách Môn học
 */
public class MonHocAdapter extends RecyclerView.Adapter<MonHocAdapter.MonHocViewHolder> {
    private List<MonHoc> monHocList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MonHoc monHoc);
    }

    public MonHocAdapter(List<MonHoc> monHocList) {
        this.monHocList = monHocList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<MonHoc> newList) {
        this.monHocList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MonHocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_monhoc, parent, false);
        return new MonHocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonHocViewHolder holder, int position) {
        MonHoc monHoc = monHocList.get(position);
        holder.bind(monHoc);
    }

    @Override
    public int getItemCount() {
        return monHocList != null ? monHocList.size() : 0;
    }

    class MonHocViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTenMon, txtMaMon, txtSoTinChi;

        public MonHocViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTenMon = itemView.findViewById(R.id.txtTenMon);
            txtMaMon = itemView.findViewById(R.id.txtMaMon);
            txtSoTinChi = itemView.findViewById(R.id.txtSoTinChi);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(monHocList.get(position));
                }
            });
        }

        public void bind(MonHoc monHoc) {
            // Tên môn: Bold, Blue, Font lớn
            txtTenMon.setText(monHoc.getTenMon() != null ? monHoc.getTenMon() : "");
            
            // Mã môn
            String maMon = monHoc.getMaMH() != null ? monHoc.getMaMH() : "";
            txtMaMon.setText("Mã: " + maMon);
            
            // Số tín chỉ
            Integer soTinChi = monHoc.getSoTinChi() != null ? monHoc.getSoTinChi() : 0;
            txtSoTinChi.setText(soTinChi + " Tín chỉ");
        }
    }
}

