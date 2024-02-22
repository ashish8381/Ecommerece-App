package com.demo.ecommereceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.ecommereceapp.adapter.ViewPagerAdapter;
import com.demo.ecommereceapp.adapter.productAdapter;
import com.demo.ecommereceapp.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductDetailActivity extends AppCompatActivity {

    TextView mtitle, mdesc, mprice, mcat,mreview;
    ViewPager mimage;
    private String[] imageUrls;
    RatingBar mrating;
    int p_id;
    ProgressBar pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        mtitle = findViewById(R.id.detail_productname);
        mdesc = findViewById(R.id.detail_desc);
        mprice = findViewById(R.id.detail_price);
        mcat = findViewById(R.id.detail_category);
        mreview=findViewById(R.id.detail_review);
        pd=findViewById(R.id.detail_progress);
        pd.setVisibility(View.VISIBLE);

        mimage=findViewById(R.id.view_pager);

        mrating=findViewById(R.id.detail_rating);

        imageUrls=new String[3];

        SharedPreferences myPreferences
                = PreferenceManager.getDefaultSharedPreferences(ProductDetailActivity.this);
        //dailylimit = myPreferences.getInt("limit", 0);
        p_id = myPreferences.getInt("p_id", 0);
        getProducts();
    }

    private void getProducts() {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fakestoreapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<Product> call = retrofitAPI.getProductDetail(p_id);
        call.enqueue(new Callback<Product>() {
            @Override
            public void onResponse(Call<Product> call, Response<Product> response) {
                if (response.isSuccessful()) {
                    Product product = response.body();
                    if (product != null) { // Assuming you want the first product in the list

                        mtitle.setText(product.getTitle());
                        mdesc.setText(product.getDescription());
                        mcat.setText(product.getCategory());
                        mprice.setText("₹ " + (float) product.getPrice());
                        imageUrls[0]=product.getImage();
                        imageUrls[1]=product.getImage();
                        imageUrls[2]=product.getImage();
//                        Picasso.get().load(product.getImage()).into(mimage);
                        mrating.setRating((float) product.getRating().getRate());
                        mreview.setText(product.getRating().getCount()+" reviews");
                        pd.setVisibility(View.GONE);
                        ViewPagerAdapter adapter = new ViewPagerAdapter(ProductDetailActivity.this, imageUrls);
                        mimage.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<Product> call, Throwable t) {
                Log.e("abcd", "Failed " + call.toString());
                Log.e("abcd", "Failed to fetch products: " + t.getMessage());
                Toast.makeText(ProductDetailActivity.this, "Failed to get the data..", Toast.LENGTH_SHORT).show();
                pd.setVisibility(View.GONE);
            }
        });
    }

}