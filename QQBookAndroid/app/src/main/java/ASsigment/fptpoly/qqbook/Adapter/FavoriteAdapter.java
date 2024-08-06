package ASsigment.fptpoly.qqbook.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ASsigment.fptpoly.qqbook.InforActivity;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoriteAdapter extends  RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    Context context;
    private ArrayList<Comic> list;

    private ArrayList<User> userList;


    public FavoriteAdapter(Context context, ArrayList<Comic> list) {
        this.context = context;
        this.list = list;

    }

    public void setData(ArrayList<Comic> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_listfavorite, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comic comic = list.get(position);

        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USERID", "");

        String comicId = comic.get_id();

        holder.tv_name.setText(list.get(position).getName());
        holder.tv_year.setText(comic.getPublishYear());
        Picasso.get()
                .load(InforActivity.Constants.BASE_URL+"/images/" + comic.getCoverImage())
//                .placeholder(R.drawable.fpt)
//                .error(R.drawable.fptremovebgpreview)
                .into(holder.iv_img);

        holder.tv_author.setText(list.get(position).getAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InforActivity.class);
                i.putExtra("comicId", comic.get_id());
                i.putExtra("name", comic.getName());
                i.putExtra("des", comic.getDescription());
                i.putExtra("price", comic.getPrice());
                i.putExtra("author", comic.getAuthor());
                i.putExtra("type", comic.getType());
                i.putExtra("publishYear", comic.getPublishYear());
                i.putExtra("coverImage", comic.getCoverImage());
                i.putExtra("images", comic.getImages());
                context.startActivity(i);

            }
        });

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
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
                        InterfaceAPI.apiservice.deleteFavorite(userId,comicId).enqueue(new Callback<User>() {
                            @Override
                            public void onResponse(Call<User> call, Response<User> response) {
                                if (response.isSuccessful()) {
                                    list.remove(position);
                                    notifyItemRemoved(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(context, "Truyện đã được xoá khỏi danh sách", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(context, "Lỗi khi xoá truyện", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<User> call, Throwable t) {
                                Toast.makeText(context, "Lỗi khi xoá truyện", Toast.LENGTH_SHORT).show();
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
        ImageView iv_img;
        TextView tv_name, tv_author, tv_year;
        ImageView btn_delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_item_favorite_coverImg);
            tv_name = itemView.findViewById(R.id.tv_item_favorite_name);
            tv_year = itemView.findViewById(R.id.tv_item_favorite_year);
            tv_author = itemView.findViewById(R.id.tv_item_favorite_author);
            btn_delete = itemView.findViewById(R.id.btn_item_favorite_delete);

        }
    }
}
