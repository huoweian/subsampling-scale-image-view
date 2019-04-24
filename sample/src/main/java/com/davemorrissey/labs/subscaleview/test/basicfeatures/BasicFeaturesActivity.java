package com.davemorrissey.labs.subscaleview.test.basicfeatures;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.AbstractPagesActivity;
import com.davemorrissey.labs.subscaleview.test.Page;
import com.davemorrissey.labs.subscaleview.test.R.id;

import org.beyka.tiffbitmapfactory.TiffBitmapFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.SCALE_TYPE_CUSTOM;
import static com.davemorrissey.labs.subscaleview.test.R.string.*;
import static com.davemorrissey.labs.subscaleview.test.R.layout.*;

public class BasicFeaturesActivity extends AbstractPagesActivity {

    public BasicFeaturesActivity() {
        super(basic_title, pages_activity, Arrays.asList(
                new Page(basic_p1_subtitle, basic_p1_text),
                new Page(basic_p2_subtitle, basic_p2_text),
                new Page(basic_p3_subtitle, basic_p3_text),
                new Page(basic_p4_subtitle, basic_p4_text),
                new Page(basic_p5_subtitle, basic_p5_text)
        ));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SubsamplingScaleImageView view = findViewById(id.imageView);

        view.setMinimumScaleType(SCALE_TYPE_CENTER_CROP);

//        view.setMinimumScaleType(SCALE_TYPE_CUSTOM);
//        view.setMinScale(1.0f);
//        view.setImage(ImageSource.asset("sanmartino.jpg"));
//        view.setImage(ImageSource.asset("test.bmp"));

//        view.setImage(ImageSource.asset("eye.bmp"));



        //需要申请存储权限，如果不申请的话，需要设置taretSDK
        File file = new File("/sdcard/better/tif.tif");
        Log.e("better",file.length()+"");

//Read data about image to Options object
        TiffBitmapFactory.Options optionsA = new TiffBitmapFactory.Options();
        optionsA.inJustDecodeBounds = false;
        Bitmap bitmap = TiffBitmapFactory.decodeFile(file, optionsA);
        view.setImage(ImageSource.bitmap(bitmap));


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            BitmapFactory.decodeStream(getAssets().open("tif.tif"), null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String type = options.outMimeType;
//        Log.e("better", type);

        int size = getMaximumTextureSize();
        Log.e("better", "size:"+size);


        view.setOnImageEventListener(new SubsamplingScaleImageView.DefaultOnImageEventListener(){
            @Override
            public void onReady() {
                super.onReady();
            }

            @Override
            public void onImageLoaded() {
                super.onImageLoaded();
            }

            @Override
            public void onPreviewLoadError(Exception e) {
                super.onPreviewLoadError(e);
            }

            @Override
            public void onImageLoadError(Exception e) {
                super.onImageLoadError(e);
                Log.e("better", new Canvas().getMaximumBitmapWidth() /8 + "");


                view.setImage(ImageSource.asset("eye.bmp").tilingDisabled());
            }

            @Override
            public void onTileLoadError(Exception e) {
                super.onTileLoadError(e);
            }

            @Override
            public void onPreviewReleased() {
                super.onPreviewReleased();
            }
        });

    }

    public int getMaximumTextureSize()
    {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        // Initialise
        int[] version = new int[2];
        egl.eglInitialize(display, version);

        // Query total number of configurations
        int[] totalConfigurations = new int[1];
        egl.eglGetConfigs(display, null, 0, totalConfigurations);

        // Query actual list configurations
        EGLConfig[] configurationsList = new EGLConfig[totalConfigurations[0]];
        egl.eglGetConfigs(display, configurationsList, totalConfigurations[0], totalConfigurations);

        int[] textureSize = new int[1];
        int maximumTextureSize = 0;

        // Iterate through all the configurations to located the maximum texture size
        for (int i = 0; i < totalConfigurations[0]; i++)
        {
            // Only need to check for width since opengl textures are always squared
            egl.eglGetConfigAttrib(display, configurationsList[i], EGL10.EGL_MAX_PBUFFER_WIDTH, textureSize);

            // Keep track of the maximum texture size
            if (maximumTextureSize < textureSize[0])
            {
                maximumTextureSize = textureSize[0];
            }

            Log.i("GLHelper", Integer.toString(textureSize[0]));
        }

        // Release
        egl.eglTerminate(display);
        Log.i("GLHelper", "Maximum GL texture size: " + Integer.toString(maximumTextureSize));

        return maximumTextureSize;
    }

    private void showTif(){
        File file = new File("/sdcard/image.tif");

//Read data about image to Options object
        TiffBitmapFactory.Options options = new TiffBitmapFactory.Options();
        options.inJustDecodeBounds = true;
        TiffBitmapFactory.decodeFile(file, options);
    }

}
