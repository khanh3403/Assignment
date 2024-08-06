package ASsigment.fptpoly.qqbook.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ASsigment.fptpoly.qqbook.Adapter.HomeComicAdapter;
import ASsigment.fptpoly.qqbook.Adapter.SlideAdapter;
import ASsigment.fptpoly.qqbook.GetAllActivity;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.Model.Slide;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    RecyclerView recyclerView;
    private ArrayList<Comic> arrayList =  new ArrayList<>();


    private HomeComicAdapter adapter;
    ViewPager viewPager;
    SlideAdapter slideAdapter;
    ArrayList<Slide> listPhoto;
    Timer timer;
    private GradientDrawable selectedBorder;
    private GradientDrawable normalBorder;
    private View selectedView = null;
    SharedPreferences sharedPreferences;
    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recy_fragment_home_listFood);

//        ImageButton img_noti = view.findViewById(R.id.btn_fragment_home_noti);
        ImageButton btnSearch = view.findViewById(R.id.btn_fragment_home_search);

        TextView tvGetAll = view.findViewById(R.id.tv_home_getAll);


        TextView  tvHellUser;
        tvHellUser =view.findViewById(R.id.tvHelloUser);


        sharedPreferences = getContext().getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String fullname = sharedPreferences.getString("USERNAME", "");
        String id = sharedPreferences.getString("USERID", "");
        Log.d("TAG", "onViewCreated: "+id);

        tvHellUser.setText("Xin Chào " +fullname);

        viewPager = view.findViewById(R.id.viewPager);

        listPhoto = getListPhoto();
        slideAdapter = new SlideAdapter(getActivity(), listPhoto);
        viewPager.setAdapter(slideAdapter);


        selectedBorder = new GradientDrawable();
        selectedBorder.setShape(GradientDrawable.RECTANGLE);
        selectedBorder.setStroke(5, Color.BLACK); // Màu viền khi được chọn
        selectedBorder.setCornerRadius(10);

        normalBorder = new GradientDrawable();
        normalBorder.setShape(GradientDrawable.RECTANGLE);
        normalBorder.setStroke(0, Color.TRANSPARENT); // Màu viền bình thường
        normalBorder.setCornerRadius(10);

        tvGetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GetAllActivity.class);
                startActivity(i);
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), GetAllActivity.class);
                startActivity(i);
            }
        });

//        img_noti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), Notification.class);
//                startActivity(i);
//            }
//        });
//        img_tapsearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (edSearch.length()>0){
//                    String searchName = edSearch.getText().toString().trim();
//                    LinearLayoutManager linearLayoutManager = new GridLayoutManager(getContext(), 1);
//                    recyclerView.setLayoutManager(linearLayoutManager);
//                    HomeDAO homeDAO1 = new HomeDAO(getContext());
//                    listHome = new ArrayList<>();
//                    listHome = homeDAO1.Search(searchName);
//                    adapter.setData(listHome);
//                    recyclerView.setAdapter(adapter);
//                }else {
//                    reloadData();
//                }
//            }
//        });

//        tvGetAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reloadData();
//            }
//        });

        autoSlideShow();
        getDataFromAPI();


    }

    private ArrayList<Slide> getListPhoto(){
        ArrayList<Slide> list = new ArrayList<>();
        list.add(new Slide(R.drawable.banner_home1));
        list.add(new Slide(R.drawable.banner_home2));
        list.add(new Slide(R.drawable.banner_home3));
        list.add(new Slide(R.drawable.banner_home4));
        return list;
    }
    private void autoSlideShow(){
        if (listPhoto == null || listPhoto.isEmpty() || viewPager == null){
            return;
        }
        if (timer == null){
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        int currentItem = viewPager.getCurrentItem();
                        int totalItem = listPhoto.size()-1;
                        if (currentItem < totalItem){
                            currentItem ++;
                            viewPager.setCurrentItem(currentItem);
                        }else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }
        }, 500, 3000);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null){
            timer.cancel();
            timer = null;
        }
    }
    private void getDataFromAPI() {
        InterfaceAPI.apiservice.getComic().enqueue(new Callback<List<Comic>>() {
            @Override
            public void onResponse(Call<List<Comic>> call, Response<List<Comic>> response) {
                if(response.isSuccessful()){
                    arrayList.clear();
                    arrayList.addAll(response.body());
                    adapter= new HomeComicAdapter(getContext(), arrayList);
                    Collections.reverse(arrayList);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(RecyclerView.HORIZONTAL);
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