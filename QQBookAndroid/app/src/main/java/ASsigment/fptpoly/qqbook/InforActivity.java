package ASsigment.fptpoly.qqbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import ASsigment.fptpoly.qqbook.Adapter.ViewPagerAdapter;
import ASsigment.fptpoly.qqbook.Fragment.CommentFragment;
import ASsigment.fptpoly.qqbook.Fragment.InforFragment;

public class InforActivity extends AppCompatActivity {
    ImageButton btnBack;


    public class Constants {
        public static final String BASE_URL = "http://10.0.2.2:3000";
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infor);
        String dataComicName = getIntent().getStringExtra("name");

        btnBack = findViewById(R.id.btn_infor_comic_back);
        TextView tvTitle = findViewById(R.id.tv_infor_comic_name_title);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InforFragment(), "Giới Thiệu");
        adapter.addFragment(new CommentFragment(), "Bình luận");
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);

        tvTitle.setText(dataComicName);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
