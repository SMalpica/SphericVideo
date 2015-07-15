package com.android.fitur.sphericvideo;
/**
 * Autor: Sandra Malpica Mallo
 *
 * Fecha: 10/07/2015
 *
 * Clase: OpenGLRenderer.java
 *
 * Comments: OpenGL renderer. In charge of the operations to be executed in the openGL rendering
 * thread. Draws each frame, the surface generated and takes charge of surface changes.
 */
import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;
import static android.opengl.GLES20.glUniformMatrix4fv;
import static android.opengl.Matrix.*;
import static android.opengl.GLES20.glEnable;
import static android.opengl.GLES20.GL_CULL_FACE;
import static android.opengl.GLES20.glCullFace;
import static android.opengl.GLES20.GL_FRONT;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.android.fitur.sphericvideo.Objects.Sphere;
import com.android.fitur.sphericvideo.programs.ColorShaderProgram;
import com.android.fitur.sphericvideo.programs.TextureShaderProgram;
import com.android.fitur.sphericvideo.util.Geometry;
import com.android.fitur.sphericvideo.util.LoggerConfig;
import com.android.fitur.sphericvideo.util.ShaderHelper;
import com.android.fitur.sphericvideo.util.TextResourceReader;
import com.android.fitur.sphericvideo.util.TextureHelper;

public class OpenGLRenderer implements Renderer {
    public final Context context;
    private final float[] projectionMatrix = new float[16];
    private final float[] modelMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] viewProjectionMatrix = new float[16];
    private final float[] modelViewProjectionMatrix = new float[16];
    private Sphere sphere;
//    private Mallet mallet;
    private TextureShaderProgram textureProgram;
    private ColorShaderProgram colorProgram;
    private int texture;
    private int fps;
    private long lastTime;
    private boolean spherePressed = false;
    private Geometry.Point spherePosition;

    public OpenGLRenderer(Context context){
        this.context = context;
        lastTime = System.currentTimeMillis();
    }

    @Override
    public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        sphere = new Sphere();
//        mallet = new Mallet();
        textureProgram = new TextureShaderProgram(context);
        colorProgram = new ColorShaderProgram(context);
        texture = TextureHelper.loadTexture(context, R.drawable.air_hockey_surface);
        spherePosition = new Geometry.Point(0f, 0f, 0f);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_FRONT);
    }

    /**
     * onSurfaceChanged is called whenever the surface has changed. This is
     * called at least once when the surface is initialized. Keep in mind that
     * Android normally restarts an Activity on rotation, and in that case, the
     * renderer will be destroyed and a new one created.
     *
     * @param width
     *            The new width, in pixels.
     * @param height
     *            The new height, in pixels.
     */
    @Override
    public void onSurfaceChanged(GL10 glUnused, int width, int height) {
        // Set the OpenGL viewport to fill the entire surface.
        glViewport(0, 0, width, height);

//        final float aspectRatio = width > height ?
//                (float) width / (float) height :
//                (float) height / (float) width;
//        if (width > height) {
//            // Landscape
//            orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f);
//        } else {
//            // Portrait or square
//            orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f);
//        }

        // Set the OpenGL viewport to fill the entire surface.
//        glViewport(0, 0, width, height);
        ShaderHelper.perspectiveM(projectionMatrix, 45, (float) width
                / (float) height, 1f, 10f);
        /*setLookAtM(float[] rm, int rmOffset, float eyeX, float eyeY, float eyeZ, float centerX, float centerY,
        float centerZ, float upX, float upY, float upZ)
        eye: where the eye will be, center: where the eye is looking,
        up:where the head would be pointing (yUp=1 -> straight up)*/
        setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f);
//        ShaderHelper.perspectiveM(projectionMatrix, 45, (float) width
//                / (float) height, 1f, 10f);

//        setIdentityM(modelMatrix, 0);
//        translateM(modelMatrix, 0, 0f, 0f, 2f);
        /*translateM(modelMatrix, 0, 0f, 0f, -2.5f);
        rotateM(modelMatrix, 0, -60f, 1f, 0f, 0f);*/
//        //multiply two transformation matrices
//        final float[] temp =i7t new float[16];
//        multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0);
//        System.arraycopy(temp, 0, projectionMatrix, 0, temp.length);

    }

    /**
     * OnDrawFrame is called whenever a new frame needs to be drawn. Normally,
     * this is done at the refresh rate of the screen.
     */
    @Override
    public void onDrawFrame(GL10 glUnused) {
        // Clear the rendering surface.
        glClear(GL_COLOR_BUFFER_BIT);
        multiplyMM(viewProjectionMatrix, 0, projectionMatrix, 0, viewMatrix, 0);
        positionSphereInScene(0f, 0f, 0f);
        textureProgram.useProgram();
        textureProgram.setUniforms(modelViewProjectionMatrix, texture);
        sphere.bindData(textureProgram);
        sphere.draw();
/*        // Draw the table.
        textureProgram.useProgram();
        textureProgram.setUniforms(projectionMatrix, texture);
        sphere.bindData(textureProgram);
        sphere.draw();
        // Draw the mallets.
//        colorProgram.useProgram();
//        colorProgram.setUniforms(projectionMatrix);
//        mallet.bindData*/

    }

    public void positionSphereInScene(float x, float y, float z){
        setIdentityM(modelMatrix, 0);
        translateM(modelMatrix, 0, x, y, z);
        multiplyMM(modelViewProjectionMatrix, 0, viewProjectionMatrix,
                0, modelMatrix, 0);
    }

    public void handleTouchDrag(float normalizedX, float normalizedY) {
    }

    public void handleTouchPress(float normalizedX, float normalizedY) {
    }
}
