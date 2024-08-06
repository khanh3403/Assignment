package ASsigment.fptpoly.qqbook.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ASsigment.fptpoly.qqbook.InforActivity;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.ReadComicActivity;


public class InforFragment extends Fragment {
    Button btn_read;


    public InforFragment() {
        // Required empty public constructor
    }

    public static InforFragment newInstance() {
        InforFragment fragment = new InforFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_infor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USERID", "");
        String userName = sharedPreferences.getString("USERNAME", "");

        String dataComicId = getActivity().getIntent().getStringExtra("comicId");
        String dataComicName = getActivity().getIntent().getStringExtra("name");
        String dataComicPrice = getActivity().getIntent().getStringExtra("price");
        String dataComicDes = getActivity().getIntent().getStringExtra("des");
        String dataComicAuthor = getActivity().getIntent().getStringExtra("author");
        String dataComicType = getActivity().getIntent().getStringExtra("type");
        String dataComicPublish = getActivity().getIntent().getStringExtra("publishYear");
        String dataComicCoverIMG = getActivity().getIntent().getStringExtra("coverImage");
        String dataComicImages = getActivity().getIntent().getStringExtra("images");

        ImageButton btn_back = view.findViewById(R.id.btn_infor_comic_back);


        btn_read = view.findViewById(R.id.btn_infor_read);


        ImageView iv_image = view.findViewById(R.id.iv_infor_comic_img);
        TextView tv_title = view.findViewById(R.id.tv_infor_comic_name_title);
        TextView tv_name = view.findViewById(R.id.tv_infor_comic_name);
        TextView tv_des = view.findViewById(R.id.tv_infor_comic_des);
        TextView tv_author = view.findViewById(R.id.tv_infor_comic_author);
        TextView tv_type = view.findViewById(R.id.tv_infor_comic_type);
        TextView tv_publish = view.findViewById(R.id.tv_infor_comic_publishYear);

        Picasso.get()
                .load(InforActivity.Constants.BASE_URL+"/images/" + dataComicCoverIMG)
                .into(iv_image);

        tv_name.setText(dataComicName);
        tv_des.setText(dataComicDes);
        tv_author.setText(dataComicAuthor);
        tv_type.setText(dataComicType);
        tv_publish.setText(dataComicPublish);





        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ReadComicActivity.class);
                i.putExtra("images",dataComicImages );
                startActivity(i);
            }
        });

    }

}