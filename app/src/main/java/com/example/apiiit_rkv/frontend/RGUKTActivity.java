package com.example.apiiit_rkv.frontend;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class RGUKTActivity extends Fragment {

    ViewPager viewPager;
    LinearLayout slidedots;
    private  int dotscount;
    private ImageView[] dots;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_rgukt, container, false);

        viewPager = (ViewPager)root.findViewById(R.id.rgukt_viewpager);
        slidedots = (LinearLayout)root.findViewById(R.id.slidedots);
        custem_swip custemSwip = new custem_swip(getActivity());
        viewPager.setAdapter(custemSwip);

        dotscount = custemSwip.getCount();
        dots = new ImageView[dotscount];

        for(int i=0;i<dotscount;i++) {

            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 0, 8, 0);
            slidedots.addView(dots[i],params);

        }
        dots[0].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for(int i=0; i<dotscount;i++) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.nonactive_dot));
                }
                dots[position].setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.active_dot));

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new MyTimeTask(), 2000,3000);

        return root;
    }


    class MyTimeTask extends TimerTask {
        @Override
        public void run() {
            try

            {
                getActivity().runOnUiThread(new Runnable() {


                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() == 0) {
                            viewPager.setCurrentItem(1);
                        } else if (viewPager.getCurrentItem() == 1) {
                            viewPager.setCurrentItem(2);
                        } else if (viewPager.getCurrentItem() == 2) {
                            viewPager.setCurrentItem(3);
                        } else if (viewPager.getCurrentItem() == 3) {
                            viewPager.setCurrentItem(4);
                        } else if (viewPager.getCurrentItem() == 4) {
                            viewPager.setCurrentItem(5);
                        } else if (viewPager.getCurrentItem() == 5) {
                            viewPager.setCurrentItem(6);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }

                });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}

