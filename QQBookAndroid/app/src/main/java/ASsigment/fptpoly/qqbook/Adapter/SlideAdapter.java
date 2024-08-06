package ASsigment.fptpoly.qqbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ASsigment.fptpoly.qqbook.Model.Slide;
import ASsigment.fptpoly.qqbook.R;

public class SlideAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<Slide> list;

    public SlideAdapter(Context context, ArrayList<Slide> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.slide_show, container,false);
        ImageView iv_img = view.findViewById(R.id.img_slide);
        Slide slide = list.get(position);
        if(slide != null ){
            Glide.with(context).load(slide.getSrcId()).into(iv_img);
        }
        container.addView(view);


        return view;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}


