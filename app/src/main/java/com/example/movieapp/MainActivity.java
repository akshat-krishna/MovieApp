package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.channels.AsynchronousChannelGroup;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, MainFragment.onFragmentBtnSelected{
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String popularMoviesURL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=b5883dff923c543f0b602baf020b0e77";
    ArrayList<Movie> list;
    OkHttpClient client=new OkHttpClient();
    MainFragment frag;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        list=new ArrayList<>();
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();
        helpme();


        //load default fragment
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new MainFragment());
        fragmentTransaction.commit();
        Log.d("MAINNNN", String.valueOf(list.size()));



    }

    private void helpme() {
        try{
            run();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(popularMoviesURL)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                if (response.isSuccessful())
                    manageJSon(myResponse);
            }
        });
    }

    public void manageJSon(String myResponse) {

        try {
            JSONObject json = new JSONObject(myResponse);
            JSONArray resArray = json.getJSONArray("results");
//                            Log.d("MAIN ACTIVITY", String.valueOf(resArray.length()));//Getting the results object
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                Movie movie = new Movie(); //New Movie object
                movie.setId(jsonObject.getInt("id"));
                movie.setVoteAverage(jsonObject.getInt("vote_average"));
                movie.setVoteCount(jsonObject.getInt("vote_count"));
                movie.setOriginalTitle(jsonObject.getString("original_title"));
                movie.setTitle(jsonObject.getString("title"));
                movie.setPopularity(jsonObject.getDouble("popularity"));
                movie.setBackdropPath(jsonObject.getString("backdrop_path"));
                movie.setOverview(jsonObject.getString("overview"));
                movie.setReleaseDate(jsonObject.getString("release_date"));
                movie.setPosterPath(jsonObject.getString("poster_path"));
                //Adding a new movie object into ArrayList
                Log.d("MOVIE TITLE", movie.getTitle());
                list.add(movie);

                Log.d("MAIN:", list.size() + " " + list.get(list.size() - 1).getTitle() + " added successfully");
            }
            Log.d("MAIN AFTER LIST READY", String.valueOf(list.size()));
            frag.setOb(list);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            JSONObject json = new JSONObject(myResponse);
//                            JSONArray resArray = json.getJSONArray("results");
////                            Log.d("MAIN ACTIVITY", String.valueOf(resArray.length()));//Getting the results object
//                            for (int i = 0; i < resArray.length(); i++) {
//                                JSONObject jsonObject = resArray.getJSONObject(i);
//                                Movie movie = new Movie(); //New Movie object
//                                movie.setId(jsonObject.getInt("id"));
//                                movie.setVoteAverage(jsonObject.getInt("vote_average"));
//                                movie.setVoteCount(jsonObject.getInt("vote_count"));
//                                movie.setOriginalTitle(jsonObject.getString("original_title"));
//                                movie.setTitle(jsonObject.getString("title"));
//                                movie.setPopularity(jsonObject.getDouble("popularity"));
//                                movie.setBackdropPath(jsonObject.getString("backdrop_path"));
//                                movie.setOverview(jsonObject.getString("overview"));
//                                movie.setReleaseDate(jsonObject.getString("release_date"));
//                                movie.setPosterPath(jsonObject.getString("poster_path"));
//                                //Adding a new movie object into ArrayList
//                                Log.d("MOVIE TITLE", movie.getTitle());
//                                list.add(movie);
//
//                                Log.d("MAIN:",list.size()+" "+list.get(list.size()-1).getTitle()+" added successfully");
//                            }
//                            frag.setOb(list);
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                });

//            }
//        });
//    }
//





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        Log.d("In NAVIGATION PANNEL", String.valueOf(list.size()));
        if(item.getItemId()==R.id.menu_search) {
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new MainFragment());
            fragmentTransaction.commit();
        }
         if(item.getItemId()==R.id.menu_movie){
             fragmentManager=getSupportFragmentManager();
             fragmentTransaction=fragmentManager.beginTransaction();
             fragmentTransaction.replace(R.id.container_fragment,new FragmentSecond());
             fragmentTransaction.commit();
        }
         if(item.getItemId()==R.id.menu_tv){
             Log.d("Debug:","TV show clicked");
            Toast.makeText(getApplicationContext(), "TV shows will be available shortly", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onButtonSelected() {
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment,new FragmentSecond());
        fragmentTransaction.commit();

    }
}

