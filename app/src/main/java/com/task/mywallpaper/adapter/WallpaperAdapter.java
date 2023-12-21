package com.task.mywallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.task.mywallpaper.R;
import com.task.mywallpaper.activities.FullScreenwallpaper;
import com.task.mywallpaper.models.WallpaperModel;

import java.util.List;

public class WallpaperAdapter extends RecyclerView.Adapter<WallpaperViewHolder> {

   private Context context;
   private List<WallpaperModel> wallpaperModelList;

    public WallpaperAdapter(Context context, List<WallpaperModel> wallpaperModelList) {
        this.context = context;
        this.wallpaperModelList = wallpaperModelList;
    }

    @NonNull
    @Override
    public WallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wallpaper_item,parent,false);
        return new WallpaperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallpaperViewHolder holders, int position) {

        final WallpaperViewHolder holder = holders;
        WallpaperModel model = wallpaperModelList.get(position);

        Glide.with(context).load(model.getMediumUrl()).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.startActivity(new Intent(context, FullScreenwallpaper.class)
                        .putExtra("originalUrl",model.getOriginalUrl()));

            }
        });
    }

    @Override
    public int getItemCount() {

        return wallpaperModelList.size();
    }
}
class







WallpaperViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView photographerName;
    public WallpaperViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView  = itemView.findViewById(R.id.imagViewItem);
        photographerName = itemView.findViewById(R.id.photographername);

    }
}
