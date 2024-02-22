package com.demo.ecommereceapp;

import com.demo.ecommereceapp.model.Product;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface RetrofitAPI {

    // as we are making get request
    // so we are displaying GET as annotation.
    // and inside we are passing
    // last parameter for our url.


    // as we are calling data from array
    // so we are calling it with json object
    @GET("products")
    Call<ArrayList<Product>> getProducts();

    @GET("products/{pid}")
    Call<Product> getProductDetail(@Path("pid") int pid);


//    @GET
//    Call<RecipeDetailModel>getDetail(@Url String url);
}
