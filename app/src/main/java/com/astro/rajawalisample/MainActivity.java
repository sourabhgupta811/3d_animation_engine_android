package com.astro.rajawalisample;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.astro.rajawalisample.R;

import org.rajawali3d.materials.RajawaliContext;


/**
 * Created by Sourabh Gupta on 14/5/19.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        RajawaliContext.getInstance().setContext(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_view4, AnimationFragment.newInstance("name3")).commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_view2, AlphaFragment.newInstance("name1")).commit();
            }
        }, 200);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_view3, AlphaFragment.newInstance("name2")).commit();
            }
        }, 400);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_view, AlphaFragment.newInstance("name")).commit();
            }
        }, 600);
    }
}
