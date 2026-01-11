package com.example.creatdatabase_sinhvien.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.DiemThi;
import java.util.List;

/**
 * Adapter cho ListView hiển thị danh sách điểm thi
 */
public class DiemThiAdapter extends BaseAdapter {
    private final Context context;
    private List<DiemThi> diemThiList;
    private final LayoutInflater inflater;

    public DiemThiAdapter(Context context, List<DiemThi> diemThiList) {
        this.context = context;
        this.diemThiList = diemThiList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return diemThiList != null ? diemThiList.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return diemThiList != null ? diemThiList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_diemthi, parent, false);
            holder = new ViewHolder();
            holder.tvMaSV = convertView.findViewById(R.id.tvItemMaSV);
            holder.tvMaMH = convertView.findViewById(R.id.tvItemMaMH);
            holder.tvHoTen = convertView.findViewById(R.id.tvItemHoTen);
            holder.tvTenMon = convertView.findViewById(R.id.tvItemTenMon);
            holder.tvDiemLan1 = convertView.findViewById(R.id.tvItemDiemLan1);
            holder.tvDiemLan2 = convertView.findViewById(R.id.tvItemDiemLan2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DiemThi dt = diemThiList.get(position);

        if (dt != null) {
            holder.tvMaSV.setText("Mã SV: " + (dt.getMaSV() != null ? dt.getMaSV() : ""));
            holder.tvMaMH.setText("Mã MH: " + (dt.getMaMH() != null ? dt.getMaMH() : ""));
            holder.tvHoTen.setText("Họ tên: " + (dt.getHoTen() != null ? dt.getHoTen() : ""));
            holder.tvTenMon.setText("Môn: " + (dt.getTenMon() != null ? dt.getTenMon() : ""));

            if (dt.getDiemLan1() != null) {
                holder.tvDiemLan1.setText(String.valueOf(dt.getDiemLan1()));
            } else {
                holder.tvDiemLan1.setText("--");
            }

            if (dt.getDiemLan2() != null) {
                holder.tvDiemLan2.setText(String.valueOf(dt.getDiemLan2()));
            } else {
                holder.tvDiemLan2.setText("--");
            }
        }

        return convertView;
    }

    public void updateList(List<DiemThi> newList) {
        this.diemThiList = newList;
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView tvMaSV;
        TextView tvMaMH;
        TextView tvHoTen;
        TextView tvTenMon;
        TextView tvDiemLan1;
        TextView tvDiemLan2;
    }
}

