package com.astro.rajawalisample;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


import org.rajawali3d.animation.Animation;
import org.rajawali3d.materials.RajawaliContext;


/**
 * Created by Sourabh Gupta on 14/5/19.
 */
public class MainActivity extends AppCompatActivity {
    AnimationFragment fragment1;
    AlphaFragment fragment2;
    AlphaFragment fragment3;
    AlphaFragment fragment4;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        RajawaliContext.getInstance().setContext(this);
        fragment1 = AnimationFragment.newInstance("name3");
        getSupportFragmentManager().beginTransaction().add(R.id.frame_view4, fragment1).commit();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                fragment2 = AlphaFragment.newInstance("name1");
                getSupportFragmentManager().beginTransaction().add(R.id.frame_view2, fragment2).commit();
//            }
//        }, 200);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                fragment3 = AlphaFragment.newInstance("name1");
                getSupportFragmentManager().beginTransaction().add(R.id.frame_view3, fragment3).commit();
//            }
//        }, 400);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
                fragment4 = AlphaFragment.newInstance("name1");
                getSupportFragmentManager().beginTransaction().add(R.id.frame_view, fragment4).commit();
//            }
//        }, 600);
    }
    boolean fragmentShowing = true;
    public void remove(View view) {
        if(fragmentShowing) {
            getSupportFragmentManager().beginTransaction().remove(fragment1).remove(fragment2).remove(fragment3).remove(fragment4).commit();
            fragmentShowing = false;
        }else{
            getSupportFragmentManager().beginTransaction().add(R.id.frame_view4, fragment1).commit();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_view2, fragment2).commit();
                }
            }, 200);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_view3, fragment3).commit();
                }
            }, 400);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction().add(R.id.frame_view, fragment4).commit();
                }
            }, 600);
            fragmentShowing = true;
        }
    }
}
