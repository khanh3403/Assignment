package ASsigment.fptpoly.qqbook.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ASsigment.fptpoly.qqbook.InforActivity;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.Model.Favorite;
import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.R;
import ASsigment.fptpoly.qqbook.api.InterfaceAPI;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeComicAdapter extends RecyclerView.Adapter<HomeComicAdapter.ViewHolder> {
    Context context;
    private ArrayList<Comic> list;

    public HomeComicAdapter(Context context, ArrayList<Comic> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_listcomic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Comic comic = list.get(position);
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USERID", "");

        String comicId = comic.get_id();

        holder.tv_name.setText(list.get(position).getName());
        holder.tv_author.setText(comic.getAuthor());
        Picasso.get()
                .load(InforActivity.Constants.BASE_URL + "/images/" + comic.getCoverImage())
                .into(holder.iv_img);

        checkComicInFavorite(comicId, holder.btn_favorite);
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

        holder.btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.dialog_item_submit);

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                AppCompatButton btnSubmit, btnCancel;
                TextView tvContent;
                btnSubmit = dialog.findViewById(R.id.btn_dialog_item_add_cart);
                btnCancel = dialog.findViewById(R.id.btn_dialog_item_cancel_cart);
                tvContent = dialog.findViewById(R.id.tv_dialog_submit_content);

                tvContent.setText("Bạn có muốn thêm vào danh sách yêu thích không ?");

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addComicToFavorite(userId, comicId);
                        notifyDataSetChanged();
                        dialog.dismiss();
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

    private void checkComicInFavorite(String comicId, ImageView btn_favorite) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USERID", "");

        InterfaceAPI.apiservice.getComicIdWithUser(userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject jsonObject = response.body();
                    if (jsonObject != null && jsonObject.has("favoriteComicIds")) {
                        JsonArray jsonArray = jsonObject.getAsJsonArray("favoriteComicIds");
                        boolean isComicInFavorite = false;
                        for (JsonElement jsonElement : jsonArray) {
                            String favoriteComicId = jsonElement.getAsString();
                            if (favoriteComicId.equals(comicId)) {
                                btn_favorite.setImageResource(R.drawable.icon_favorite);
                                btn_favorite.setClickable(false);
                                isComicInFavorite = true;
                                break;
                            }
                        }
                        if (!isComicInFavorite) {
                            btn_favorite.setImageResource(R.drawable.icon_favorite_boder);
                            btn_favorite.setClickable(true);
                        }
                    }
                } else {
                    Toast.makeText(context, "Đã xảy ra lỗi khi kiểm tra yêu thích", Toast.LENGTH_SHORT).show();
                    Log.e("CHECK_FAVORITE_ERROR", "Error code: " + response.code());
                    Log.e("CHECK_FAVORITE_ERROR", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(context, "Không thể kết nối đến máy chủ", Toast.LENGTH_SHORT).show();
                Log.e("CHECK_FAVORITE_ERROR", "Failed to connect to server: " + t.getMessage());
            }
        });
    }

    private void addComicToFavorite(String userId, String comicId) {
        InterfaceAPI.apiservice.buyComic(userId, new Favorite(comicId)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Thêm vào danh sách yêu thích thành công", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Đã xảy ra lỗi khi mua truyện", Toast.LENGTH_SHORT).show();
                    Log.e("BUY_COMIC_ERROR", "Error code: " + response.code());
                    Log.e("BUY_COMIC_ERROR", response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(context, "Không mua được truyện", Toast.LENGTH_SHORT).show();
                Log.e("BUY_COMIC_ERROR", "Failed to connect to server: " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_name, tv_author;
        ImageView btn_favorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_item_comic_coverImg);
            tv_name = itemView.findViewById(R.id.tv_item_comic_name);
            tv_author = itemView.findViewById(R.id.tv_item_comic_author);
            btn_favorite = itemView.findViewById(R.id.btn_item_comic_favorite);
        }
    }
}
