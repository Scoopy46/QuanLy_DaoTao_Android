package com.example.creatdatabase_sinhvien.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.PhanCongGiangDay;

import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách phân công giảng dạy
 */
public class PhanCongGiangDayAdapter extends RecyclerView.Adapter<PhanCongGiangDayAdapter.ViewHolder> {
    private final Context context;
    private List<PhanCongGiangDay> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PhanCongGiangDay item);
    }

    public PhanCongGiangDayAdapter(Context context, List<PhanCongGiangDay> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phan_cong_giang_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhanCongGiangDay item = list.get(position);
        if (item == null) return;

        holder.tvTenMon.setText(item.getTenMon() != null ? item.getTenMon() : "");
        holder.tvTenGV.setText(item.getTenGV() != null ? item.getTenGV() : "");

        Integer stc = item.getSoTinChi();
        holder.tvSoTinChi.setText(stc != null ? String.valueOf(stc) + " TC" : "");

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void updateList(List<PhanCongGiangDay> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTenMon;
        TextView tvTenGV;
        TextView tvSoTinChi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTenMon = itemView.findViewById(R.id.tvTenMon);
            tvTenGV = itemView.findViewById(R.id.tvTenGV);
            tvSoTinChi = itemView.findViewById(R.id.tvSoTinChi);
        }
    }
}


