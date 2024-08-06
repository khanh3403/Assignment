package ASsigment.fptpoly.qqbook.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ASsigment.fptpoly.qqbook.InforActivity;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.Model.User;
import ASsigment.fptpoly.qqbook.R;

public class GetAllAdapter extends  RecyclerView.Adapter<GetAllAdapter.ViewHolder> {
    Context context;
    private ArrayList<Comic> list;

    private ArrayList<User> userList;


    public GetAllAdapter(Context context, ArrayList<Comic> list) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_comic_getall, null);
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
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_name, tv_author, tv_year;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_img = itemView.findViewById(R.id.iv_item_getall_coverImg);
            tv_name = itemView.findViewById(R.id.tv_item_getall_name);
            tv_year = itemView.findViewById(R.id.tv_item_getall_year);
            tv_author = itemView.findViewById(R.id.tv_item_getall_author);
        }
    }
}

