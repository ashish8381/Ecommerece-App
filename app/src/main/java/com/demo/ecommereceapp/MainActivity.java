package com.demo.ecommereceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.demo.ecommereceapp.adapter.productAdapter;
import com.demo.ecommereceapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    RecyclerView mrecyclerview;
    private ArrayList<Product> ldata;
    private productAdapter mAdapter;
    ProgressBar pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mrecyclerview = findViewById(R.id.menu_recyclerview);
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));

        pd=findViewById(R.id.progress);
        pd.setVisibility(View.VISIBLE);

        getProducts();

    }

    private void getProducts() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<ArrayList<Product>> call = retrofitAPI.getProducts();
        call.enqueue(new Callback<ArrayList<Product>>() {
            @Override
            public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                if (response.isSuccessful()) {
                    ArrayList<Product> productList = response.body();
                    mAdapter = new productAdapter(productList, MainActivity.this,new CustomItemClickListener() {
                        @Override
                        public void onItemClick(Product product, int position) {

                            Toast.makeText(getApplicationContext(),""+product.getId(),Toast.LENGTH_SHORT).show();

                        }
                    });
                    mrecyclerview.setAdapter(mAdapter);
                    pd.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Product>> call, Throwable t) {
                Log.e("abcd", "Failed to fetch products: " + t.getMessage());
                Toast.makeText(MainActivity.this, "Failed to get the data..", Toast.LENGTH_SHORT).show();
                pd.setVisibility(View.GONE);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);

        MenuItem searchItem = menu.findItem(R.id.menu_search);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mAdapter.getFilter().filter(newText);
                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

}