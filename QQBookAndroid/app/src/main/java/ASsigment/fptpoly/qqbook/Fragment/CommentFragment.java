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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ASsigment.fptpoly.qqbook.Adapter.CommentAdapter;
import ASsigment.fptpoly.qqbook.Model.Comment;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentFragment extends Fragment {
    ArrayList<Comment> listComment = new ArrayList<>();
    ImageButton btn_comment;
    CommentAdapter commentAdapter;
    EditText edComment;

    RecyclerView recyclerView;



    public CommentFragment() {
        // Required empty public constructor
    }

    public static CommentFragment newInstance(String param1, String param2) {
        CommentFragment fragment = new CommentFragment();
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
        return inflater.inflate(R.layout.fragment_comment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recy_comment);
        btn_comment = view.findViewById(R.id.btn_infor_add_comment);
        edComment = view.findViewById(R.id.ed_fragment_comment);

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

        getListCommentById(dataComicId);


        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editedComment = edComment.getText().toString();

                Comment updatedComment = new Comment(dataComicId, userId,userName, editedComment);

                InterfaceAPI.apiservice.postComment(updatedComment).enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()) {
                            getListCommentById(dataComicId);
                            commentAdapter.notifyDataSetChanged();
                            edComment.setText("");
                            Toast.makeText(getContext(), "Bình luận đã được đăng", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Không thể đăng", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Log.e("EditComment", "Lỗi khi chỉnh sửa bình luận: " + t.getMessage());
                        Toast.makeText(getContext(), "Lỗi khi đăng", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void getListCommentById(String comicId) {
        InterfaceAPI.apiservice.getCommentWithComcId(comicId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()){
                    listComment.clear();
                    listComment.addAll(response.body());
                    Collections.reverse(listComment);
                    commentAdapter = new CommentAdapter(getContext(), listComment);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                    layoutManager.setOrientation(RecyclerView.VERTICAL);
                    Log.d("xxxxx", "onResponse: " +response.body());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(commentAdapter);
                    commentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi không hiển thị truyện", Toast.LENGTH_SHORT).show();
                Log.e("BUY_COMIC_ERROR", "Failed to connect to server: " + t.getMessage());
            }
        });
    }
}