package com.task.mywallpaper.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.task.mywallpaper.R;
import com.task.mywallpaper.ReclerViewClickListner;
import com.task.mywallpaper.models.SuggestedModel;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SuggestedAdapter extends RecyclerView.Adapter<SuggestedAdapter.SuggestedViewHolder> {

    RecyclerView.RecycledViewPool sharedPool = new RecyclerView.RecycledViewPool();

    ArrayList<SuggestedModel> suggestedModels;
    final private ReclerViewClickListner clickListner;

    public SuggestedAdapter(ArrayList<SuggestedModel> suggestedModels,ReclerViewClickListner clickListner) {
        this.suggestedModels = suggestedModels;
        this.clickListner = clickListner;
    }

    @NonNull
    @Override
    public SuggestedAdapter.SuggestedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggested_item,parent,false);
        final  SuggestedViewHolder suggestedViewHolder = new SuggestedViewHolder(view);

        return suggestedViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestedViewHolder holder, int position) {

        SuggestedModel suggestedModel = suggestedModels.get(position);
        holder.image.setImageResource(suggestedModel.getImage());
        holder.title.setText(suggestedModel.getTitle());
    }

    @Override
    public int getItemCount() {
        return suggestedModels.size();
    }

    public  class  SuggestedViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView title;
        public SuggestedViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.suggestedimage);
            title = itemView.findViewById(R.id.suggestedTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    clickListner.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
