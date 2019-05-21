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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.AnimationGroup;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.animation.ScaleAnimation3D;
import org.rajawali3d.animation.SplineTranslateAnimation3D;
import org.rajawali3d.animation.TranslateAnimation3D;
import org.rajawali3d.cameras.OrthographicCamera;
import org.rajawali3d.curves.CatmullRomCurve3D;
import org.rajawali3d.lights.ALight;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.LoaderAWD;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.plugins.FogMaterialPlugin;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.materials.textures.TextureManager;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.primitives.Line3D;
import org.rajawali3d.primitives.Plane;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.TextureView;

import java.util.Random;
import java.util.Stack;
import java.util.concurrent.Semaphore;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_DEPTH_BUFFER_BIT;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

/**
 * Created by Sourabh Gupta on 14/5/19.
 */
public class AlphaFragment extends Fragment {
    TextureView textureView2;
    Renderer renderer;
    Semaphore semaphore = new Semaphore(1, true);

    public static AlphaFragment newInstance(String name) {

        Bundle args = new Bundle();
        AlphaFragment fragment = new AlphaFragment();
        args.putString("name", name);
        fragment.setArguments(args);
        return fragment;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.animation_fragment, container, false);
        textureView2 = v.findViewById(R.id.texture_view2);
        renderer = new PlaneRenderer(getContext());
        textureView2.setSurfaceRenderer(renderer);
        return v;
    }

    class BasicRenderer extends Renderer {

        public BasicRenderer(Context context, String name) {
            super(context);
        }

        @Override
        protected void initScene() {

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

    private class OrthographicRenderer extends Renderer {
        public OrthographicRenderer(Context context) {
            super(context);
        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            super.onRenderSurfaceCreated(config, gl, width, height);
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

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }
        private DirectionalLight mLight;
        private Object3D mRoad;

        @Override
        protected void initScene() {
            mLight = new DirectionalLight(0, -1, -1);
            mLight.setPower(.5f);
            Texture rajawaliSign = new Texture("rajawaliSign", R.drawable.sign);
            Texture warning = new Texture("warning", R.drawable.warning);
            Texture roadTex = new Texture("roadTex", R.drawable.road);

            getCurrentScene().addLight(mLight);

            int fogColor = Color.parseColor("#00f0f0");

//            getCurrentScene().setBackgroundColor(fogColor);
            getCurrentScene().setFog(new FogMaterialPlugin.FogParams(FogMaterialPlugin.FogType.LINEAR, fogColor, 1, 15));

            LoaderOBJ objParser = new LoaderOBJ(mContext.getResources(),
                    mTextureManager, R.raw.road);
            try {
                objParser.parse();
                mRoad = objParser.getParsedObject();
                mRoad.setZ(5);
                mRoad.setRotY(180);
                getCurrentScene().addChild(mRoad);

                Material roadMaterial = new Material();
                roadMaterial.enableLighting(true);
                roadMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());

                roadMaterial.addTexture(roadTex,this);
                roadMaterial.setColorInfluence(0);
                mRoad.getChildByName("Road").setMaterial(roadMaterial);

                Material signMaterial = new Material();
                signMaterial.enableLighting(true);
                signMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
                signMaterial.addTexture(rajawaliSign,this);

                signMaterial.setColorInfluence(0);
                mRoad.getChildByName("WarningSign").setMaterial(signMaterial);

                Material warningMaterial = new Material();
                warningMaterial.enableLighting(true);
                warningMaterial.setDiffuseMethod(new DiffuseMethod.Lambert());
                warningMaterial.addTexture(warning,this);
                warningMaterial.setColorInfluence(0);
                mRoad.getChildByName("Warning").setMaterial(warningMaterial);
            } catch (Exception e) {
                e.printStackTrace();
            }

            TranslateAnimation3D camAnim = new TranslateAnimation3D(
                    new Vector3(0, 2, 0),
                    new Vector3(0, 2, -23));
            camAnim.setDurationMilliseconds(8000);
            camAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            camAnim.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
            camAnim.setTransformable3D(getCurrentCamera());
            getCurrentScene().registerAnimation(camAnim);
            camAnim.play();
        }

        @Override
        public void onRenderFrame(GL10 gl) {
            glClear(GL_COLOR_BUFFER_BIT);
            super.onRenderFrame(gl);
        }
    }
    private class PlaneRenderer extends Renderer{
        private long mStartTime;
        private Material mMaterial;
        private PlanesGaloreMaterialPlugin mMaterialPlugin;
        @Override
        protected void initScene() {
            DirectionalLight light = new DirectionalLight(0, 0, 1);

            getCurrentScene().addLight(light);
            getCurrentCamera().setPosition(0, 0, -16);

            final PlanesGalore planes = new PlanesGalore();
            mMaterial = planes.getMaterial();
            mMaterial.setColorInfluence(0);
            try {
                mMaterial.addTexture(new Texture("flickrPics", R.drawable.flickrpics),renderer);
            } catch (ATexture.TextureException e) {
                e.printStackTrace();
            }

            mMaterialPlugin = planes.getMaterialPlugin();

            planes.setDoubleSided(true);
            planes.setZ(4);
            getCurrentScene().addChild(planes);

            Object3D empty = new Object3D();
            getCurrentScene().addChild(empty);

            CatmullRomCurve3D path = new CatmullRomCurve3D();
            path.addPoint(new Vector3(-4, 0, -20));
            path.addPoint(new Vector3(2, 1, -10));
            path.addPoint(new Vector3(-2, 0, 10));
            path.addPoint(new Vector3(0, -4, 20));
            path.addPoint(new Vector3(5, 10, 30));
            path.addPoint(new Vector3(-2, 5, 40));
            path.addPoint(new Vector3(3, -1, 60));
            path.addPoint(new Vector3(5, -1, 70));

            final SplineTranslateAnimation3D anim = new SplineTranslateAnimation3D(path);
            anim.setDurationMilliseconds(20000);
            anim.setRepeatMode(Animation.RepeatMode.REVERSE_INFINITE);
            anim.setTransformable3D(getCurrentCamera());
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            getCurrentScene().registerAnimation(anim);
            anim.play();

            getCurrentCamera().setLookAt(new Vector3(0, 0, 30));
        }

        @Override
        public void onRenderSurfaceCreated(EGLConfig config, GL10 gl, int width, int height) {
            super.onRenderSurfaceCreated(config, gl, width, height);
            mStartTime = System.currentTimeMillis();
        }

        @Override
        protected void onRender(long elapsedRealtime, double deltaTime) {
            super.onRender(elapsedRealtime, deltaTime);
            mMaterial.setTime((System.currentTimeMillis() - mStartTime) / 1000f);
            mMaterialPlugin.setCameraPosition(getCurrentCamera().getPosition());
        }
        public PlaneRenderer(Context context) {
            super(context);
        }

        @Override
        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

        }

        @Override
        public void onTouchEvent(MotionEvent event) {

        }
    }
}
