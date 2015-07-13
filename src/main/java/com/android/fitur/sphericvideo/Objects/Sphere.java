package com.android.fitur.sphericvideo.Objects;

import com.android.fitur.sphericvideo.Constants;
import com.android.fitur.sphericvideo.SphereTextCoord;
import com.android.fitur.sphericvideo.SphereVertices;
import com.android.fitur.sphericvideo.data.VertexArray;
import com.android.fitur.sphericvideo.programs.TextureShaderProgram;

import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.glDrawArrays;
/**
 * Created by Fitur on 13/07/2015.
 */
public class Sphere {
    private static final int POSITION_COMPONENT_COUNT = 3;
    private static final int TEXTURE_COORDINATES_COMPONENT_COUNT = 2;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT
            + TEXTURE_COORDINATES_COMPONENT_COUNT) * Constants.BYTES_PER_FLOAT;

    private final VertexArray vertexArray;

    private static final float[] VERTEX_DATA= crearVertexData();

    private static float[] crearVertexData(){
        // Order of coordinates: X, Y, Z, S, T
        float[] resul = new float[SphereVertices.sphere5Verts.length+ SphereTextCoord.sphere5TexCoords.length];
        int posicion = 0;
        int posvertices=0;
        int postextura=0;
        for(int i=0; i<SphereVertices.sphere5Verts.length/3; i++){
            int aux = 0;
            while(aux<3){
                resul[posicion]=SphereVertices.sphere5Verts[posvertices];
                aux++;
                posvertices++;
                posicion++;
            }
            while(aux<5){
                resul[posicion]=SphereTextCoord.sphere5TexCoords[postextura];
                aux++;
                postextura++;
                posicion++;
            }
        }
        for(int i=0; i<resul.length/5; i++){
            System.out.print(resul[i]+", ");
            if((i+1)%5==0){
                System.out.println();
            }
        }
        return resul;
    }


    public Sphere() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(TextureShaderProgram textureProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                textureProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                textureProgram.getTextureCoordinatesAttributeLocation(),
                TEXTURE_COORDINATES_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_TRIANGLES, 0, SphereVertices.sphere5Verts.length);
    }
}
