package com.android.fitur.sphericvideo;
/**
 * Autor: Sandra Malpica Mallo
 *
 * Fecha: 10/07/2015
 *
 * Clase: PlayerActivity.java
 *
 * Comments: this class displays a 360 video using OpenGL ES 2.0
 */
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;


public class PlayerActivity extends Activity {
    private GLSurfaceView glSurfaceView;
    private boolean rendererSet = false;
    private OpenGLRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //TODO: make a custom view that extends GLSurfaceView to add the video player bar over it
        //TODO: or use a layout with 2 views in the activity (SLSurface and video bar)
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        /*//full-screen
        getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //horizontal orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        final View contentView = findViewById(android.R.id.content);
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE);*/
        glSurfaceView = new GLSurfaceView(this);
        renderer = new OpenGLRenderer(this);
        final ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo =
                activityManager.getDeviceConfigurationInfo();

//        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        //see if the device supports OpenGL2 (physical and emulator)
        final boolean supportsEs2 =
                configurationInfo.reqGlEsVersion >= 0x20000
                        || (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1
                        && (Build.FINGERPRINT.startsWith("generic")
                        || Build.FINGERPRINT.startsWith("unknown")
                        || Build.MODEL.contains("google_sdk")
                        || Build.MODEL.contains("Emulator")
                        || Build.MODEL.contains("Android SDK built for x86")));
        //configure the rendering surface
        if (supportsEs2) {
            // Request an OpenGL ES 2.0 compatible context.
            glSurfaceView.setEGLContextClientVersion(2);
            // Assign our renderer.
            glSurfaceView.setRenderer(renderer);
            rendererSet = true;
        } else {
            Toast.makeText(this, "This device does not support OpenGL ES 2.0.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        setContentView(glSurfaceView);
        glSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event != null) {
                    // Convert touch coordinates into normalized device
                    // coordinates, keeping in mind that Android's Y
                    // coordinates are inverted.
                    final float normalizedX =
                            (event.getX() / (float) v.getWidth()) * 2 - 1;
                    final float normalizedY =
                            -((event.getY() / (float) v.getHeight()) * 2 - 1);
                    //        setContentView(R.layout.activity_player);
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                renderer.handleTouchPress(
                                        normalizedX, normalizedY);
                            }
                        });
                    } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                        glSurfaceView.queueEvent(new Runnable() {
                            @Override
                            public void run() {
                                renderer.handleTouchDrag(
                                        normalizedX, normalizedY);
                            }
                        });
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rendererSet) {
            glSurfaceView.onPause();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (rendererSet) {
            glSurfaceView.onResume();
        }
    }
}
