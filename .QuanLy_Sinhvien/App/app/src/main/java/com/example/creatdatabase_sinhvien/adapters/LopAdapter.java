package com.example.creatdatabase_sinhvien.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Lop;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách lớp học
 */
public class LopAdapter extends RecyclerView.Adapter<LopAdapter.ViewHolder> {
    private final Context context;
    private List<Lop> lopList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Lop lop);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(Lop lop);
    }

    public LopAdapter(Context context, List<Lop> lopList) {
        this.context = context;
        this.lopList = lopList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lop lop = lopList.get(position);
        
        if (lop != null) {
            // Mã lớp - in đậm, màu xanh
            holder.tvMaLop.setText(lop.getMaLop() != null ? lop.getMaLop() : "");
            
            // Tên lớp
            holder.tvTenLop.setText(lop.getTenLop() != null ? lop.getTenLop() : "");
            
            // Ngành
            String nganhText = "Ngành: " + (lop.getTenNganh() != null ? lop.getTenNganh() : "");
            holder.tvNganh.setText(nganhText);
            
            // Niên khóa - góc phải
            holder.tvNienKhoa.setText(lop.getNienKhoa() != null ? lop.getNienKhoa() : "");
            
            // Xử lý click
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(lop);
                }
            });
            
            // Xử lý long click (vuốt/nhấn giữ để xóa)
            holder.itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    return longClickListener.onItemLongClick(lop);
                }
                return false;
            });
        }
    }

    @Override
    public int getItemCount() {
        return lopList != null ? lopList.size() : 0;
    }

    public void updateList(List<Lop> newList) {
        this.lopList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaLop;
        TextView tvTenLop;
        TextView tvNganh;
        TextView tvNienKhoa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaLop = itemView.findViewById(R.id.tvMaLop);
            tvTenLop = itemView.findViewById(R.id.tvTenLop);
            tvNganh = itemView.findViewById(R.id.tvNganh);
            tvNienKhoa = itemView.findViewById(R.id.tvNienKhoa);
        }
    }
}

