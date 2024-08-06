package ASsigment.fptpoly.qqbook.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import ASsigment.fptpoly.qqbook.Model.Comment;
import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Comment> list;

    public CommentAdapter(Context context, ArrayList<Comment> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(ArrayList<Comment> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listcomment, parent, false);
        return new ViewHolder(view);
    }

    private void getUsernameWithUserId(String id) {
        InterfaceAPI.apiservice.getUsernameWithUserId(id).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comment comment = list.get(position);

        String sender = comment.getUserId();
        String commentId = comment.get_id();
        String comicId = comment.getComicId();
        String userId = comment.getUserId();
        String timeStamp = comment.getTimestamp();

        holder.tvName.setText(list.get(position).getUsername());
        holder.tvTime.setText(comment.getTimestamp());
        holder.tvContent.setText(comment.getContent());

        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("USERID", "");

        if (username.equals(sender)) {
            holder.btnEdit.setVisibility(View.VISIBLE);
            holder.btnDelete.setVisibility(View.VISIBLE);
        } else {
            holder.btnEdit.setVisibility(View.GONE);
            holder.btnDelete.setVisibility(View.GONE);
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_edit_comment);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                EditText etEditComment = dialog.findViewById(R.id.et_edit_comment);
                etEditComment.setText(comment.getContent());

                AppCompatButton btnSubmit = dialog.findViewById(R.id.btn_dialog_item_submit);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btn_dialog_item_cancel);

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String editedComment = etEditComment.getText().toString();

                        Comment updatedComment = new Comment(commentId, comicId, userId, editedComment, timeStamp);

                        InterfaceAPI.apiservice.updateComment(commentId, updatedComment).enqueue(new Callback<Comment>() {
                            @Override
                            public void onResponse(Call<Comment> call, Response<Comment> response) {
                                if (response.isSuccessful()) {
                                    Comment updatedComment = response.body();
                                    list.set(position, updatedComment);
                                    notifyItemChanged(position);
                                    notifyDataSetChanged();
                                    dialog.dismiss();
                                    Toast.makeText(context, "Bình luận đã được chỉnh sửa", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Không thể chỉnh sửa bình luận", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Comment> call, Throwable t) {
                                Log.e("EditComment", "Lỗi khi chỉnh sửa bình luận: " + t.getMessage());
                                Toast.makeText(context, "Lỗi khi chỉnh sửa bình luận", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });


        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_item_submit);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                AppCompatButton btnSubmit = dialog.findViewById(R.id.btn_dialog_item_add_cart);
                AppCompatButton btnCancel = dialog.findViewById(R.id.btn_dialog_item_cancel_cart);
                TextView tvContent = dialog.findViewById(R.id.tv_dialog_submit_content);
                tvContent.setText("Bạn có muốn xoá bình luận không ?");

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        InterfaceAPI.apiservice.deleteComment(commentId).enqueue(new Callback<Comment>() {
                            @Override
                            public void onResponse(Call<Comment> call, Response<Comment> response) {
                                if (response.isSuccessful()) {
                                    list.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Bình luận đã được xoá", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Lỗi khi xoá bình luận", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Comment> call, Throwable t) {
                                Toast.makeText(context, "Lỗi khi xoá bình luận", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }




    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvTime, tvContent;
        ImageButton btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_comment_username);
            tvTime = itemView.findViewById(R.id.tv_comment_time);
            tvContent = itemView.findViewById(R.id.tv_comment_content);

            btnEdit = itemView.findViewById(R.id.btn_comment_edit);
            btnDelete = itemView.findViewById(R.id.btn_comment_delete);
        }
    }
}
