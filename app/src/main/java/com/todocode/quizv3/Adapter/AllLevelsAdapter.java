package com.todocode.quizv3.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.todocode.quizv3.Model.Level;
import com.todocode.quizv3.R;

import java.util.ArrayList;

public class AllLevelsAdapter extends  RecyclerView.Adapter<AllLevelsAdapter.AllLevelsHolder> {
    private Context context;
    private ArrayList<Level> levels;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public class AllLevelsHolder extends RecyclerView.ViewHolder {
        private TextView catName;
        private TextView catDetail;
        public AllLevelsHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            catName = itemView.findViewById(R.id.all_level_name);
            catDetail = itemView.findViewById(R.id.all_level_details);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
        public void setDetails(Level level) {
            catName.setText(level.getName());
            catDetail.setText(level.getDetails());
//            String urlImg = context.getResources().getString(R.string.domain_name);
            //String allUrl = urlImg+"/assets/uploads/categories/"+category.getImageUrl();
           // Picasso.get().load(allUrl).fit().centerInside().into(catImageUrl);
        }
    }
    public AllLevelsAdapter(Context context, ArrayList<Level> levels) {
        this.context = context;
        this.levels = levels;
    }
    @NonNull
    @Override
    public AllLevelsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_all_category_layout, parent, false);
        return new AllLevelsHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AllLevelsHolder holder, int position) {
        Level level = levels.get(position);
        holder.setDetails(level);
    }

    @Override
    public int getItemCount() {
        return levels.size();
    }
}






