package com.example.apiiit_rkv.frontend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class custem_swip extends PagerAdapter {
    private Integer [] image_resources = {R.drawable.rgukt_a, R.drawable.rgukt_b, R.drawable.rgukt_c,
            R.drawable.rgukt_d, R.drawable.rgukt_e, R.drawable.rgukt_f, R.drawable.rgukt_g};
    private Context ctx;
    private LayoutInflater layoutInflater;


    public custem_swip(Context ctx) {
        this.ctx=ctx;
    }

    @Override
    public int getCount() {
        return image_resources.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view=layoutInflater.inflate(R.layout.swipe_layout,null);
        ImageView imageView=(ImageView)item_view.findViewById(R.id.image_view);
        imageView.setImageResource(image_resources[position]);
        ViewPager vp = (ViewPager)container;
        vp.addView(item_view,0);
        return item_view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        ViewPager vp = (ViewPager)container;
        View view = (View) object;
        vp.removeView(view);

    }
}