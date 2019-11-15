package com.example.cwhw3.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.example.cwhw3.Adapters.CatAdapter;
import com.example.cwhw3.CatBreed;
import com.example.cwhw3.Activity.DetailActivity;
import com.example.cwhw3.cats.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CatsFragment extends Fragment implements CatAdapter.OnItemClickListener {
    public static String catBreedURL = "catBreedURL";

    private RecyclerView catRecyclerView;
    private CatAdapter catAdapter;
    private ArrayList<CatBreed> catList;

    private String storeResponse;

    @Nullable

    public void getCatBreeds() {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());


        catBreedURL = "https://api.thecatapi.com/v1/breeds";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, catBreedURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Type menuJson = new TypeToken<ArrayList<CatBreed>>(){}.getType();
                catList = gson.fromJson(response, menuJson);
                parseResponse();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> apiKey = new HashMap<String, String>();
                apiKey.put("x-api-key", "56eb9688-11b6-4d39-99ac-39a3f40f0da6");
                return apiKey;
            }
        };

        queue.add(stringRequest);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        catRecyclerView = view.findViewById(R.id.cat_recyclerView);
        catRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        setHasOptionsMenu(true);

        if (catList == null) {
            getCatBreeds(); } else {
            parseResponse(); }

        return view;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();

        inflater.inflate(R.menu.cat_search_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        android.view.MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
            SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

           @Override
            public boolean onQueryTextChange(String newText) {
                catAdapter.getFilter().filter(newText);
                return false;
            }
        });


    }

    private void parseResponse() {
        catAdapter = new CatAdapter(getActivity(), catList);
        catRecyclerView.setAdapter(catAdapter);
        catAdapter.setOnItemClickListener(CatsFragment.this);
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
        CatBreed clickedItem = catList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clickedCat", clickedItem);

        detailIntent.putExtras(bundle);
        startActivity(detailIntent);
    }
}
