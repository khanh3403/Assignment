package ASsigment.fptpoly.qqbook.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ASsigment.fptpoly.qqbook.Adapter.FavoriteAdapter;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FavoFragment extends Fragment {
    private ArrayList<Comic> arrayList = new ArrayList<>();
    private FavoriteAdapter adapter;
    private RecyclerView recyclerView;

    public FavoFragment() {
    }

    public static FavoFragment newInstance() {
        FavoFragment fragment = new FavoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favo, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recy_favorite);

        getDataFromAPI();
    }

    private void getDataFromAPI() {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String userID = sharedPreferences.getString("USERID", "");


        InterfaceAPI.apiservice.getFavorite(userID).enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if(response.isSuccessful()){
                    arrayList.clear();
                    arrayList.addAll(response.body());
                    adapter= new FavoriteAdapter(getContext(), arrayList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//                    Spacing spacing = new Spacing(20);
//                    recyclerView.addItemDecoration(spacing);
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    Log.d("zzzzz", "onResponse: " +response.toString());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comic>> call, Throwable t) {
                Log.e("LoginActivity", "Error: " + t.getMessage());
                Toast.makeText(getContext(), "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });

    }
}