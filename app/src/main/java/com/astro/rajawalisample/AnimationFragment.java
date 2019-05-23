package com.astro.rajawalisample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.animation.TranslateAnimation3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.TextureView;

import java.util.Random;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Sourabh Gupta on 14/5/19.
 */
public class AnimationFragment extends Fragment {
    TextureView textureView2;
    Renderer renderer;
    Semaphore semaphore = new Semaphore(1, true);

    public static AnimationFragment newInstance(String name) {

        Bundle args = new Bundle();
        AnimationFragment fragment = new AnimationFragment();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.animation_fragment, container, false);
        textureView2 = v.findViewById(R.id.texture_view2);
        renderer = new BasicRenderer(getContext(), getArguments().getString("name"));
        textureView2.setSurfaceRenderer(renderer);
        return v;
    }

    class BasicRenderer extends Renderer {
        Random random = new Random();

        public BasicRenderer(Context context,String name) {
            super(context);
        }

        Vector3 randomOctant() {
            float x = random.nextBoolean() ? 1 : -1;
            float y = random.nextBoolean() ? 1 : -1;
            float z = random.nextBoolean() ? 1 : -1;
            return new Vector3(x, y, z);
        }

//        int randomColor() {
//            int colors[] = {
//                    Color.RED,
//                    Color.YELLOW,
//                    Color.GREEN,
//                    Color.CYAN,
//                    Color.LTGRAY,
//                    Color.MAGENTA,
//                    Color.WHITE,
//                    Color.DKGRAY
//            };
//            return colors[random.nextInt(colors.length - 1)];
//        }

        @Override
        protected void initScene() {
            DirectionalLight directionalLight = new DirectionalLight(0,0,-1f);
            directionalLight.setPower(1);
            getCurrentScene().addLight(directionalLight);
            getCurrentCamera().setPosition(0, 0, 16);

            try {
                Plane monkey = new Plane();
//                monkey.setMaterial();
//                Object3D monkey = new Cube(2.0f);
                Material material = new Material();
//                material.setColorInfluence(0);
//                material.enableLighting(true);
//                material.setDiffuseMethod(new DiffuseMethod.Toon());
                try {
                    material.addTexture(new Texture("abc", R.drawable.animation_image),this);
                }catch (Exception e){}
                monkey.setMaterial(material);
                monkey.setColor(0xffff8C00);
                monkey.setScale(2);
                getCurrentScene().addChild(monkey);
                TranslateAnimation3D anim = new TranslateAnimation3D(new Vector3(0,0,0),new Vector3(3,3,3));
                anim.setDurationMilliseconds(1000);
                anim.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
                anim.setInterpolator(new OvershootInterpolator());
                anim.setTransformable3D(monkey);
                getCurrentScene().registerAnimation(anim);
                anim.play();
                RotateOnAxisAnimation animation = new RotateOnAxisAnimation(Vector3.Axis.Y,360);
                animation.setDurationMilliseconds(1000);
                animation.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
                animation.setInterpolator(new OvershootInterpolator());
                animation.setTransformable3D(monkey);
                getCurrentScene().registerAnimation(animation);
                animation.play();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            try {
//                DirectionalLight key = new DirectionalLight(-3, 4, 5);
//                getCurrentScene().setBackgroundColor(Color.BLUE);
//                key.setPower(5.0f);
//                getCurrentScene().addLight(key);
//
//                Material material = new Material();
//                material.setColor(randomColor());
//                material.setDiffuseMethod(new DiffuseMethod.Lambert());
//                material.setAmbientColor(Color.LTGRAY);
//                material.enableLighting(true);
//
//                Object3D obj = new Cube(2);
//                obj.setMaterial(material);
//                getCurrentScene().addChild(obj);
//
//                Animation3D anim = new RotateOnAxisAnimation(randomOctant(), 360);
//                anim.setRepeatMode(Animation.RepeatMode.INFINITE);
//                anim.setDurationMilliseconds(4321 + random.nextInt(1234));
//                anim.setTransformable3D(obj);
//                getCurrentScene().registerAnimation(anim);
//                anim.play();
//
//                getCurrentCamera().setPosition(3, 4, 5);
//                getCurrentCamera().setLookAt(obj.getPosition());
//            } catch (Exception e) {
//                Log.d(getClass().getSimpleName(), e.getMessage());
//            }
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }

        @Override
        public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
            try {
                semaphore.acquire();
                super.onRenderSurfaceSizeChanged(gl, width, height);
            } catch (Exception e) {
                Log.d(getClass().getSimpleName(), e.getMessage());
            } finally {
                semaphore.release();
            }
        }
    }
//    private class SampleAnimationRenderer extends Renderer {
//        private final String name;
//
//        public SampleAnimationRenderer(Context context, String name) {
//            super(context);
//            this.name = name;
//        }
//
//        @Override
//        public void onRenderSurfaceSizeChanged(GL10 gl, int width, int height) {
//            try {
//                semaphore.acquire();
//                super.onRenderSurfaceSizeChanged(gl, width, height);
//            } catch (Exception e) {
//                Log.d(getClass().getSimpleName(), e.getMessage());
//            } finally {
//                semaphore.release();
//            }
//        }
//
//        @Override
//        protected void initScene() {
//            DirectionalLight directionalLight = new DirectionalLight(0,0,-1f);
//            directionalLight.setPower(1);
//            getCurrentScene().addLight(directionalLight);
//            getCurrentCamera().setPosition(0, 0, 16);
//
//            try {
//                Object3D monkey = new Cube(2.0f);
//                Material material = new Material();
//                material.setColorInfluence(0);
////                material.enableLighting(true);
////                material.setDiffuseMethod(new DiffuseMethod.Toon());
//                material.addTexture(new Texture(name,R.drawable.animation_image));
//                monkey.setMaterial(material);
////                monkey.setColor(0xffff8C00);
//                monkey.setScale(2);
//                getCurrentScene().addChild(monkey);
//                TranslateAnimation3D anim = new TranslateAnimation3D(new Vector3(0,0,0),new Vector3(3,3,3));
//                anim.setDurationMilliseconds(1000);
//                anim.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
//                anim.setInterpolator(new OvershootInterpolator());
//                anim.setTransformable3D(monkey);
//                getCurrentScene().registerAnimation(anim);
//                anim.play();
//                RotateOnAxisAnimation animation = new RotateOnAxisAnimation(Vector3.Axis.Y,360);
//                animation.setDurationMilliseconds(1000);
//                animation.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
//                animation.setInterpolator(new OvershootInterpolator());
//                animation.setTransformable3D(monkey);
//                getCurrentScene().registerAnimation(animation);
//                animation.play();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
//
//        }
//
//        @Override
//        public void onTouchEvent(MotionEvent event) {
//
//        }
//    }
}
