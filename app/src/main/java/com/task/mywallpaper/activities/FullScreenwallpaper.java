package com.task.mywallpaper.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.artjimlop.altex.AltexImageDownloader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.task.mywallpaper.R;

import java.io.IOException;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class FullScreenwallpaper extends AppCompatActivity {

    String originalUrl="";
    PhotoView photoView;
    CircularProgressButton buttonSetWallpaper,buttonDownloadWallpaper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screenwallpaper);

        Intent intent = getIntent();
        originalUrl = intent.getStringExtra("originalUrl");

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        photoView = (PhotoView) findViewById(R.id.photoView);
        buttonDownloadWallpaper = (CircularProgressButton) findViewById(R.id.buttonDownloadWallpaper);
        buttonSetWallpaper =(CircularProgressButton)  findViewById(R.id.buttonSetWallpaper);


        Glide.with(this).load(originalUrl).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
               progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable resource, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);
                return false;
            }
        }).into(photoView);

        buttonSetWallpaper.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {

                WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullScreenwallpaper.this);
                Bitmap bitmap  = ((BitmapDrawable)photoView.getDrawable()).getBitmap();
                try {
                    wallpaperManager.setBitmap(bitmap);
                    Toast.makeText(FullScreenwallpaper.this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            /*    AsyncTask<String,String,String> demoSetWallpaper= new AsyncTask<String, String, String>() {
                    @Nullable
                    @Override
                    protected String doInBackground( String... strings) {
                        try {
                            Thread.sleep(300);

                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);

                        if (s!=null && s.equals("done")){

                            WallpaperManager wallpaperManager = WallpaperManager.getInstance(FullScreenwallpaper.this);
                            Bitmap bitmap = ((BitmapDrawable) photoView.getDrawable()).getBitmap();


                            try {
                                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);

                                wallpaperManager.setBitmap(bitmap);
                                Toast.makeText(FullScreenwallpaper.this, "image is set as wallpaper", Toast.LENGTH_SHORT).show();
                                 buttonSetWallpaper.doneLoadingAnimation(Color.parseColor("#333693"),BitmapFactory.
                                    decodeResource(getResources(),R.drawable.baseline_done_24));

                            }catch (IOException e){
                                e.printStackTrace();
                            }
                        }

                    }
                };
*/                buttonSetWallpaper.startAnimation();
               // demoSetWallpaper.execute();
            }
        });
        buttonDownloadWallpaper.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {


                DownloadManager downloadManager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(originalUrl);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                downloadManager.enqueue(request);
                Toast.makeText(FullScreenwallpaper.this, "Downloading Start", Toast.LENGTH_SHORT).show();

//                AsyncTask<String,String,String> demoSetWallpaper= new AsyncTask<String, String, String>() {
//                    @Nullable
//                    @Override
//                    protected String doInBackground( String... strings) {
//                        try {
//                            Thread.sleep(300);
//
//                        }catch (InterruptedException e){
//                            e.printStackTrace();
//                        }
//
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(String s) {
//                        super.onPostExecute(s);
//
//                        if (s!=null && s.equals("done")){
//
//                            AltexImageDownloader.writeToDisk(FullScreenwallpaper.this, originalUrl,"pexels");  // image folder
//                            Toast.makeText(FullScreenwallpaper.this, "Download starts", Toast.LENGTH_SHORT).show();
//                            buttonDownloadWallpaper.doneLoadingAnimation(Color.parseColor("#333693"),BitmapFactory.
//                                    decodeResource(getResources(),R.drawable.baseline_done_24));
//
//                        }
//
//                    }
//                };
       buttonDownloadWallpaper.startAnimation();
               // demoSetWallpaper.execute();
            }
        });


    }
}