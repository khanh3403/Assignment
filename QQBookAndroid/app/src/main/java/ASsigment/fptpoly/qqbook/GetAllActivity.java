package ASsigment.fptpoly.qqbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ASsigment.fptpoly.qqbook.Adapter.GetAllAdapter;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetAllActivity extends AppCompatActivity {
    private ArrayList<Comic> arrayList = new ArrayList<>();
    private GetAllAdapter adapter;

    private RecyclerView recyclerView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all);
        EditText edSearchName = findViewById(R.id.ed_getall_search);
        ImageButton btnSearch = findViewById(R.id.btn_getall_search);
        ImageButton imgBack = findViewById(R.id.btn_getall_comic_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recy_getall);
        getDataFromAPI();
        String name = edSearchName.getText().toString();
        edSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getDataByName(s.toString());
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDataByName(name);
            }
        });



    }
    private void getDataFromAPI() {
        InterfaceAPI.apiservice.getComic().enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if(response.isSuccessful()){
                    arrayList.clear();
                    arrayList.addAll(response.body());
                    adapter= new GetAllAdapter(GetAllActivity.this, arrayList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(GetAllActivity.this);
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
                Toast.makeText(GetAllActivity.this, "Có lỗi xảy ra", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataByName(String keyword) {
        if (!TextUtils.isEmpty(keyword)) {
            InterfaceAPI.apiservice.searchComicByName(keyword).enqueue(new Callback<List<Comic>>() {
                @Override
                public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                    if(response.isSuccessful()){
                        List<Comic> comics = response.body();
                        if (comics != null && !comics.isEmpty()) {
                            arrayList.clear();
                            arrayList.addAll(comics);
                            adapter.setData(arrayList);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Comic>> call, Throwable t) {

                }
            });

        } else {

            getDataFromAPI();
        }
    }
}