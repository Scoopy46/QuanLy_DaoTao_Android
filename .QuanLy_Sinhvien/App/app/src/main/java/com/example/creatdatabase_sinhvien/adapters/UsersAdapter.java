package com.example.creatdatabase_sinhvien.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.creatdatabase_sinhvien.R;
import com.example.creatdatabase_sinhvien.models.Users;
import java.util.List;

/**
 * Adapter cho RecyclerView hiển thị danh sách Users
 */
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private List<Users> usersList;
    private OnItemClickListener listener;
    private OnItemLongClickListener longClickListener;

    public interface OnItemClickListener {
        void onItemClick(Users user);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(Users user);
    }

    public UsersAdapter(List<Users> usersList) {
        this.usersList = usersList;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    public void updateList(List<Users> newList) {
        this.usersList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Users user = usersList.get(position);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return usersList != null ? usersList.size() : 0;
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        private TextView txtUserName, txtFullName, txtType, txtMaGV;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            txtType = itemView.findViewById(R.id.txtType);
            txtMaGV = itemView.findViewById(R.id.txtMaGV);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onItemClick(usersList.get(position));
                }
            });

            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && longClickListener != null) {
                    return longClickListener.onItemLongClick(usersList.get(position));
                }
                return false;
            });
        }

        public void bind(Users user) {
            txtUserName.setText(user.getUserName() != null ? user.getUserName() : "");
            txtFullName.setText(user.getFullName() != null ? user.getFullName() : "");
            
            String type = user.getType() != null ? user.getType() : "";
            txtType.setText(type);
            
            if (user.getMaGV() != null && !user.getMaGV().isEmpty()) {
                txtMaGV.setText("GV: " + user.getMaGV());
                txtMaGV.setVisibility(View.VISIBLE);
            } else {
                txtMaGV.setVisibility(View.GONE);
            }
        }
    }
}

