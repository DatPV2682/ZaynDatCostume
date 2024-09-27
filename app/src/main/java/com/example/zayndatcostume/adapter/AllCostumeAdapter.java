package com.example.zayndatcostume.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.zayndatcostume.R;
import com.example.zayndatcostume.models.Costume;

import java.util.List;

public class AllCostumeAdapter extends RecyclerView.Adapter<AllCostumeAdapter.CostumeViewHolder> {

    private Context mContext;
    private List<Costume> costumeList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public AllCostumeAdapter(Context context, List<Costume> costumes) {
        mContext = context;
        costumeList = costumes;
    }

    @NonNull
    @Override
    public CostumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.all_costume_item, parent, false);
        return new CostumeViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull CostumeViewHolder holder, int position) {
        Costume currentCostume = costumeList.get(position);
        System.out.println(currentCostume.getImageLink());
        holder.nameTextView.setText(currentCostume.getName());
        holder.publisherTextView.setText(currentCostume.getPublisher());
        Glide.with(mContext).load(currentCostume.getImageLink()).into(holder.costumeImageView);
    }

    @Override
    public int getItemCount() {
        return costumeList.size();
    }

    public static class CostumeViewHolder extends RecyclerView.ViewHolder {

        public ImageView costumeImageView;
        public TextView nameTextView;
        public TextView publisherTextView;
        public Button editButton;
        public Button deleteButton;

        public CostumeViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            costumeImageView = itemView.findViewById(R.id.image_costume);
            nameTextView = itemView.findViewById(R.id.name_costume);
            publisherTextView = itemView.findViewById(R.id.publisher_costume);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onEditClick(position);
                        }
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
