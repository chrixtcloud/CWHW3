package com.example.cwhw3.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.cwhw3.Adapters.CatDetailsAdapter;
import com.example.cwhw3.CatBreed;
import com.example.cwhw3.CatDetails;
import com.example.cwhw3.CatImage;
import com.example.cwhw3.cats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private CatBreed clickedCat;
    private ArrayList<CatImage> catImageAll;
    private CatImage catImage;

    private RecyclerView statsRecyclerView;
    private CatDetailsAdapter statsAdapter;
    private RecyclerView.LayoutManager statsLayoutManager;
    private ArrayList<CatDetails> statsList;

    private String catImageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if(getActionBar() != null){ getActionBar().setDisplayHomeAsUpEnabled(true); }

        Intent intent = getIntent();
        clickedCat = (CatBreed) intent.getSerializableExtra("clickedCat");
        loadUI();

        RequestQueue queue = Volley.newRequestQueue(this);
        catImageURL = "https://api.thecatapi.com/v1/images/search?breed_id=" + clickedCat.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, catImageURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type catImageType = new TypeToken<ArrayList<CatImage>>(){}.getType();
                catImageAll = gson.fromJson(response, catImageType);
                catImage = catImageAll.get(0);
                loadImage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        ) {@Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("x-api-key", "99c5c080-ad1d-481e-b5c3-f5e3572732a0");
                return headers;
            }
        };

        queue.add(stringRequest);
    }

    public void loadUI() {
        TextView catNameView = findViewById(R.id.name_detail);

        catNameView.setText(clickedCat.getName());

        statsRecyclerView = findViewById(R.id.stats_recyclerView);

        statsLayoutManager = new LinearLayoutManager(this);

        statsList = addStats(clickedCat);
        statsAdapter = new CatDetailsAdapter(statsList);

        statsRecyclerView.setLayoutManager(statsLayoutManager);
        statsRecyclerView.setAdapter(statsAdapter);


        ImageButton favButton = findViewById(R.id.favoriteButton_detail);
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();

            }
        });
    }

    public void loadImage() {
        ImageView catImageView = findViewById(R.id.image_detail);
        Picasso.get().load(catImage.getUrl()).fit().centerInside().into(catImageView);
    }

    public ArrayList<CatDetails> addStats(CatBreed cat) {
        ArrayList<CatDetails> catStats = new ArrayList<>();

        catStats.add(new CatDetails("Weight", cat.getWeight().getMetric()));
        catStats.add(new CatDetails("Origin", cat.getOrigin()));
        catStats.add(new CatDetails("Dog Friendliness Level", cat.getDog_friendly()));
        catStats.add(new CatDetails("Life Span", cat.getLife_span()));
        catStats.add(new CatDetails("Temperament", cat.getTemperament()));
        catStats.add(new CatDetails("Wikipedia URL", cat.getWikipedia_url()));
        catStats.add(new CatDetails("Description", cat.getDescription()));


        return catStats;
    }

    private void clear(){


    }


    private void add() {
        ArrayList<CatBreed> favouritesList;
        CatBreed favourite = clickedCat;

        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        String json = sharedPreferences.getString("favourites", null);
        Gson gson = new Gson();

        Type favouritesJson = new TypeToken<ArrayList<CatBreed>>(){}.getType();
        favouritesList = gson.fromJson(json, favouritesJson);

        favouritesList.add(favourite);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        json = gson.toJson(favouritesList);

        editor.putString("favourites", json);

        editor.apply();

        Toast.makeText(getApplicationContext(),"Added!", Toast.LENGTH_SHORT).show();
    }


}
