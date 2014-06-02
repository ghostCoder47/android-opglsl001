package com.janas.airhockey;

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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import com.janas.airhockey.util.LoggerConfig;
import com.janas.airhockey.util.ShaderHelper;
import com.janas.airhockey.util.TextResourceReader;

public class AirHockeyRenderer implements Renderer {
    
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int BYTES_PER_FLOAT = 4;
    
    private final FloatBuffer m_vertexData;
    private final Context m_context;
    private int m_program;
    
    private static final String U_COLOR = "u_Color";
    private int m_uColorLocation;
    private static final String A_POSITION = "a_Position";
    private int m_aPositionLocation;
    
    
    public AirHockeyRenderer(Context context) {
        
        this.m_context = context;
        
        float[] tableVerticesWithTriangles = {                
                
                // triangle 1
                -0.5f, -0.5f,
                0.5f, 0.5f,
                -0.5f, 0.5f,
                
                // triangle 2
                -0.5f, -0.5f,
                0.5f, -0.5f,
                0.5f, 0.5f,
                
                // line 1
                -0.5f, 0f,
                0.5f, 0f,
                
                // mallets
                0f, -0.25f,
                0f, 0.25f,
                
                // puck
                -0.05f, -0.05f,
                0.05f, 0.05f,
                -0.05f, 0.05f,
                
                0.05f, 0.05f, 
                -0.05f, -0.05f,
                0.05f, -0.05f,
                
             
                
                
        };
        
        m_vertexData = ByteBuffer
                .allocateDirect(tableVerticesWithTriangles.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        
        m_vertexData.put(tableVerticesWithTriangles);
    }
    

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        String vertexShaderSource = TextResourceReader.readTextFileFromResource(m_context, R.raw.svs);
        String fragmentShaderSource = TextResourceReader.readTextFileFromResource(m_context, R.raw.sfs);
        
        int vertexShaderId = ShaderHelper.compileVertexShader(vertexShaderSource);
        int fragmentShaderId = ShaderHelper.compileFragmentShader(fragmentShaderSource);
        m_program = ShaderHelper.linkProgram(vertexShaderId, fragmentShaderId);
        
        if (LoggerConfig.ON) {
            ShaderHelper.validateProgram(m_program);
        }
        
        glUseProgram(m_program);
        m_uColorLocation = glGetUniformLocation(m_program, U_COLOR);
        m_aPositionLocation = glGetAttribLocation(m_program, A_POSITION);
        
        m_vertexData.position(0);
        glVertexAttribPointer(m_aPositionLocation, POSITION_COMPONENT_COUNT, GL_FLOAT, false, 0, m_vertexData);    
        glEnableVertexAttribArray(m_aPositionLocation);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        
        glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        
        glClear(GL_COLOR_BUFFER_BIT);
              
        glUniform4f(m_uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 6);
        
        glUniform4f(m_uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 6, 2);
        
        glUniform4f(m_uColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
        glDrawArrays(GL_POINTS, 8, 1);
        
        glUniform4f(m_uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 9, 1);
        
        glUniform4f(m_uColorLocation, 0.5f, 0.5f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 10, 6);
        
        
    }

}
