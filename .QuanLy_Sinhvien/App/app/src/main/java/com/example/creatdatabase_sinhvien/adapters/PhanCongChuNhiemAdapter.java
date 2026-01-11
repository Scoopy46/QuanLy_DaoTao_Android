package com.example.creatdatabase_sinhvien.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.PhanCongChuNhiem;

import java.util.List;

/**
 * Adapter cho danh sách phân công GVCN theo lớp
 */
public class PhanCongChuNhiemAdapter extends RecyclerView.Adapter<PhanCongChuNhiemAdapter.ViewHolder> {
    private final Context context;
    private List<PhanCongChuNhiem> list;
    private OnUpdateClickListener updateClickListener;

    public interface OnUpdateClickListener {
        void onUpdateClick(PhanCongChuNhiem item);
    }

    public PhanCongChuNhiemAdapter(Context context, List<PhanCongChuNhiem> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnUpdateClickListener(OnUpdateClickListener listener) {
        this.updateClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phan_cong_chu_nhiem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhanCongChuNhiem item = list.get(position);
        if (item == null) return;

        String tenLop = item.getTenLop() != null && !item.getTenLop().isEmpty() ? item.getTenLop() : item.getMaLop();
        holder.tvLop.setText("Lớp: " + (tenLop != null ? tenLop : ""));

        boolean hasGvcn = item.getMaGV() != null && !item.getMaGV().isEmpty();
        if (hasGvcn) {
            holder.tvGvcn.setText("GVCN: " + (item.getTenGV() != null ? item.getTenGV() : ""));
            holder.tvGvcn.setTextColor(0xFF1976D2); // xanh
        } else {
            holder.tvGvcn.setText("Chưa có GVCN");
            holder.tvGvcn.setTextColor(0xFFF44336); // đỏ
        }

        String ngay = item.getNgayBatDau() != null ? item.getNgayBatDau() : "";
        holder.tvNgay.setText(ngay.isEmpty() ? "" : ("Từ ngày: " + ngay));
        holder.tvNgay.setVisibility(ngay.isEmpty() ? View.GONE : View.VISIBLE);

        holder.btnCapNhat.setOnClickListener(v -> {
            if (updateClickListener != null) updateClickListener.onUpdateClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void updateList(List<PhanCongChuNhiem> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLop;
        TextView tvGvcn;
        TextView tvNgay;
        Button btnCapNhat;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLop = itemView.findViewById(R.id.tvLop);
            tvGvcn = itemView.findViewById(R.id.tvGvcn);
            tvNgay = itemView.findViewById(R.id.tvNgay);
            btnCapNhat = itemView.findViewById(R.id.btnCapNhat);
        }
    }
}


