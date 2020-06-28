package com.example.myweatherapp;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastFragment extends Fragment {
    @Nullable
    Toolbar toolbar;
    String icon_baseURL="https://openweathermap.org/img/wn/";
    String icon_latterURL="@2x.png";
    String key = "d78bab4aeb7ce7d0d37d28db3c8c7dd2";
    String BaseUrl = "https://api.openweathermap.org";
    static String latitude=null;
    static String longitude=null;
    RecyclerView recyclerView;
    TextView place_search_container,temp_search_container,celsius_search,search_status;
    ImageView weather_icon_search;
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    private final String TAG2="FragmentTAG";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast, container, false);
        place_search_container = view.findViewById(R.id.place_search_container);
        weather_icon_search=view.findViewById(R.id.weather_icon_search);
        temp_search_container=view.findViewById(R.id.temp_search_container);
        celsius_search=view.findViewById(R.id.Celsius_search);
        search_status=view.findViewById(R.id.search_status);
        toolbar=view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        recyclerView=view.findViewById(R.id.forecast_list);
        return view;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search Any City");
        queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e(TAG2, newText);
                return true;
            }
            @Override
            public boolean onQueryTextSubmit(String query) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
                    JSONPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);
                    Call<Home_Fragment_Details> call = jsonPlaceHolderAPI.getPost(query,"metric", key);
                    call.enqueue(new Callback<Home_Fragment_Details>() {
                        @Override
                        public void onResponse(Call<Home_Fragment_Details> call, Response<Home_Fragment_Details> response) {
                                Home_Fragment_Details home_fragment_details = response.body();
                                latitude = home_fragment_details.getCoord().getLat();
                                longitude = home_fragment_details.getCoord().getLon();
                                double temp=Double.parseDouble(home_fragment_details.getMain().getFeels_like());
                                temp_search_container.setText((int)Math.ceil(temp)+"");
                                Spanned superscript= Html.fromHtml("<sup>o</sup>C");
                                celsius_search.setText(superscript);
                                search_status.setText(home_fragment_details.getWeather()[0].getMain());
                                place_search_container.setText(home_fragment_details.getName());
                                Glide.with(getContext()).load(icon_baseURL+home_fragment_details.getWeather()[0].getIcon()+icon_latterURL).into(weather_icon_search);
                            getForecast(latitude,longitude);
                        }
                        @Override
                        public void onFailure(Call<Home_Fragment_Details> call, Throwable t) {

                        }
                    });
                return true;
            }
        };
        searchView.setOnQueryTextListener(queryTextListener);
        super.onCreateOptionsMenu(menu, inflater);
    }

    void getForecast(String latitude,String longitude){
        Retrofit retrofit1 = new Retrofit.Builder().baseUrl(BaseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        JSONPlaceHolderAPI jsonPlaceHolderAPI1 = retrofit1.create(JSONPlaceHolderAPI.class);
        Call<Forecast_Fragment_Details> call1 = jsonPlaceHolderAPI1.getFragPost(latitude, longitude, "minutely,hourly,current","metric", key);
        Log.e("TAG3",call1.request().url().toString());
        call1.enqueue(new Callback<Forecast_Fragment_Details>() {
            @Override
            public void onResponse(Call<Forecast_Fragment_Details> call, Response<Forecast_Fragment_Details> response) {
                Forecast_Fragment_Details forecast_fragment_details = response.body();
                ArrayList<Daily> list=new ArrayList<Daily>(Arrays.asList(forecast_fragment_details.getDaily()));
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
                recyclerView.setAdapter(new Forecast_Adapter(getContext(),list));
                Log.e("TAG5",list.size()+"");
            }

            @Override
            public void onFailure(Call<Forecast_Fragment_Details> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                // Not implemented here
                searchView.setOnQueryTextListener(queryTextListener);
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }
}


