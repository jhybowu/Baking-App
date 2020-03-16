package com.example.bakingapp.util;


import android.net.DnsResolver;
import com.example.bakingapp.data.Recipe;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class NetworkUtils {
    private static final String sBaseEndPointAddress =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    private interface RecipeService {
        @GET("baking.json")
        Call<ArrayList<Recipe>> listRecipes();
    }

    public static void getRecipesFromNetwork(DnsResolver.Callback<ArrayList<Recipe>> callback) {
        // Build the retrofit element.
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl(sBaseEndPointAddress).
                addConverterFactory(GsonConverterFactory.create()).
                client(new OkHttpClient()).
                build();

        // Get the client that will be associated with the retrofit element.
        RecipeService recipeServiceClient = retrofit.create(RecipeService.class);

        // Get the call instance that is associated with the client.
        Call<ArrayList<Recipe>> call = recipeServiceClient.listRecipes();

        // Enqueue the call to be handled by the retrofit element associated with the client that
        // asynchronously handles the specified callback.
        call.enqueue((Callback<ArrayList<Recipe>>) callback);
    }
}
