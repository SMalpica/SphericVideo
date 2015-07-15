package com.android.fitur.sphericvideo.Objects;

import com.android.fitur.sphericvideo.SphereVertices;

import java.util.ArrayList;
import java.util.List;

import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.GL_TRIANGLES;

/**
 * Created by Fitur on 15/07/2015.
 */
public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;
    private float[] vertexData;
    private static int offset = 0;
    private final List<DrawCommand> drawList = new ArrayList<DrawCommand>();

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    public static interface DrawCommand {
        void draw();
    }

    /*holder class so that we can return both the vertex data and the draw list in a single object*/
    public static class GeneratedData {
        final float[] vertexData;
        final List<DrawCommand> drawList;
        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

    private GeneratedData build() {
        return new GeneratedData(vertexData, drawList);
    }

    //our sphere is made out of 382 points, has 760 faces, 3040 vertices defined
    public static GeneratedData createSphereObject(int numPoints){
        final int startVertex = offset / FLOATS_PER_VERTEX;
        ObjectBuilder builder = new ObjectBuilder(numPoints);
        builder.appendSphere();
        builder.drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                glDrawArrays(GL_TRIANGLES, startVertex, Sphere.numVertices);
            }
        });
        return builder.build();
    }

    private void appendSphere(){
        vertexData = Sphere.VERTEX_DATA;
    }
}
