package com.example.creatdatabase_sinhvien.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.SinhVien;
import com.example.creatdatabase_sinhvien.utils.ImageHelper;
import java.util.List;

/**
 * Adapter cho ListView hiển thị danh sách sinh viên
 */
public class SinhVienAdapter extends BaseAdapter {
    private final Context context;
    private List<SinhVien> sinhVienList;
    private final LayoutInflater inflater;

    public SinhVienAdapter(Context context, List<SinhVien> sinhVienList) {
        this.context = context;
        this.sinhVienList = sinhVienList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return sinhVienList != null ? sinhVienList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return sinhVienList != null ? sinhVienList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sinhvien, parent, false);
            holder = new ViewHolder();
            holder.imgAvatar = convertView.findViewById(R.id.imgItemAvatar);
            holder.tvMaSV = convertView.findViewById(R.id.tvItemMaSV);
            holder.tvHoTen = convertView.findViewById(R.id.tvItemHoTen);
            holder.tvNamSinh = convertView.findViewById(R.id.tvItemNamSinh);
            holder.tvLop = convertView.findViewById(R.id.tvItemLop);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = sinhVienList.get(position);

        if (sv != null) {
            holder.tvMaSV.setText("Mã SV: " + (sv.getMaSV() != null ? sv.getMaSV().trim() : ""));
            holder.tvHoTen.setText("Họ tên: " + (sv.getHoTen() != null ? sv.getHoTen() : ""));
            holder.tvNamSinh.setText("Năm sinh: " + sv.getNamSinh());
            holder.tvLop.setText("Lớp: " + (sv.getLop() != null ? sv.getLop() : ""));

            // Load ảnh nếu có
            if (sv.getAnh() != null && !sv.getAnh().isEmpty()) {
                ImageHelper.loadImage(context, holder.imgAvatar, sv.getAnh());
            } else {
                holder.imgAvatar.setImageResource(0);
            }
        }

        return convertView;
    }

    public void updateList(List<SinhVien> newList) {
        this.sinhVienList = newList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        ImageView imgAvatar;
        TextView tvMaSV;
        TextView tvHoTen;
        TextView tvNamSinh;
        TextView tvLop;
    }
}

