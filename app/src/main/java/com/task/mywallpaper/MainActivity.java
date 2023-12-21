package com.task.mywallpaper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.task.mywallpaper.activities.AboutActivity;
import com.task.mywallpaper.adapter.SuggestedAdapter;
import com.task.mywallpaper.adapter.WallpaperAdapter;
import com.task.mywallpaper.models.SuggestedModel;
import com.task.mywallpaper.models.WallpaperModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReclerViewClickListner {

    static final float END_SCALE = 0.7f;
    ImageView menuIcon;
    LinearLayout contentView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RecyclerView suggestedrecycle, trendingrecycle;
    RecyclerView.Adapter adapter;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;

    ArrayList<SuggestedModel> suggestedModels = new ArrayList<>();
    Boolean isScrolling = false;
    int currentItems, totalItems, scrollItems;

    ProgressBar progressBar;

    TextView replaceTitle;

    EditText searchEv;
    ImageView searchimg;


    int pagenumber = 1; //we need more pages
    String url = "https://api.pexels.com/v1/curated?page=" + pagenumber + "&per_page=80";
    //String url =   "https://www.pexels.com/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuIcon = findViewById(R.id.menu_icon);
        contentView = findViewById(R.id.content_view);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);

        navigationDrawer();

        //navigation profile
        View headerView = navigationView.getHeaderView(0);
        ImageView appLogo = headerView.findViewById(R.id.app_image);

        suggestedrecycle = findViewById(R.id.suggestedrecycle);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        suggestedrecycle.setLayoutManager(layoutManager);
        suggestedrecycle.setHasFixedSize(true);

       // .setLayoutManager(new LinearLayoutManager(this));

        trendingrecycle = findViewById(R.id.trendingrecycle);
        //trendingrecycle.setLayoutManager(new LinearLayoutManager(this));


        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);
        wallpaperAdapter.notifyDataSetChanged();
        suggestedrecycle.setAdapter(wallpaperAdapter);
        trendingrecycle.setAdapter(wallpaperAdapter);


        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        trendingrecycle.setLayoutManager(gridLayoutManager);

//scrooling behavikour
        suggestedrecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = false;

                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                //doubtful
                scrollItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }

            }
        });

        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);


        replaceTitle = (TextView) findViewById(R.id.topmostTitle);
        fetchWallpaper();
        suggestedItems();


        //search edittext
        searchEv = findViewById(R.id.searchEv);
        searchimg = findViewById(R.id.searchimg);
        searchimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "work is in progress", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });
        animateNavigationDrawer();
    }

    private void animateNavigationDrawer() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                //scale view on current slide

                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                //translate the view accounting

                final float xOffset = drawerLayout.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        int itemId = item.getItemId();
        if (itemId == R.id.nav_home) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_trending) {
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=trending";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);

        } else if (itemId == R.id.nav_mos_viewd) {

            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=travel";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);

        } else if (itemId == R.id.nav_Logout) {
            Toast.makeText(this, "Exit", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_about) {
            Toast.makeText(this, "Info", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);

        }
        return true;
    }


    private void suggestedItems() {
        suggestedrecycle.setHasFixedSize(true);
        suggestedrecycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Trending"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Nature"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Architechture"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "People"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Business"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Health"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Film"));
        suggestedModels.add(new SuggestedModel(R.mipmap.ic_launcher_round, "Travel"));

        try {
            adapter = new SuggestedAdapter(suggestedModels, MainActivity.this);
            adapter.notifyDataSetChanged();
            suggestedrecycle.setAdapter(adapter);

        }catch (Exception e){

        }


    }


    private void fetchWallpaper() {

        //fetch url respose from api
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("photos");

                    int length = jsonArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        int id = object.getInt("id");
                        String photographerName = object.getString("photographer");
                        JSONObject objectImage = object.getJSONObject("src");
                        String originalUrl = objectImage.getString("original");
                        String mediumlUrl = objectImage.getString("medium");

                        WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumlUrl, photographerName);
                        wallpaperModelList.add(wallpaperModel);


                    }

                    wallpaperAdapter.notifyDataSetChanged();
                    pagenumber++;
                } catch (JSONException e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("Authorization", "ycn3X12fBMjq8KAk3ms7iRiGSZjcdnMZsNwviCqW7CxXlRGJOUewnVQ1");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }

    @Override
    public void onItemClick(int position) {

        progressBar.setVisibility(View.VISIBLE);
        if (position==0){
            replaceTitle.setText("Trending");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=trending";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);
        } else if (position==1) {
            replaceTitle.setText("Nature");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=nature";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);

        } else if (position==2) {
            replaceTitle.setText("Agriculture");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=agriculture";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);


        }else if (position==3) {
            replaceTitle.setText("People");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=people";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);


        }else if (position==4) {
            replaceTitle.setText("Business");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=business";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);


        }else if (position==5) {
            replaceTitle.setText("Health");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=health";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);


        }else if (position==6) {
            replaceTitle.setText("Film");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=film";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);


        }else if (position==7) {
            replaceTitle.setText("Travel");
            url = "https://api.pexels.com/v1/search?page=" + pagenumber + "&per_page=80&query=travel";
            wallpaperModelList.clear();
            fetchWallpaper();
            progressBar.setVisibility(View.GONE);


        }
    }
}