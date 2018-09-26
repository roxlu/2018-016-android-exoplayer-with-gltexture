package rox.lu;

import android.content.Context;
import android.opengl.GLES20;
import android.util.Log;
import android.opengl.GLSurfaceView;
import android.opengl.GLES11Ext;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.google.android.exoplayer2.*;

public class MyGlRenderer implements GLSurfaceView.Renderer {

  /* -------------------------------------------------------------- */

  private static final String FULLSCREEN_VS = ""
    + "attribute vec2 a_pos;\n"
    + "attribute vec2 a_tex;\n"
    + "varying vec2 v_tex;\n"
    + "void main() {\n"
    + "  gl_Position = vec4(a_pos.x, a_pos.y, 0.0, 1.0);\n"
    + "  v_tex = a_tex;\n"
    + "}\n";

  private static final String FULLSCREEN_FS = ""
    + "#extension GL_OES_EGL_image_external : require\n"
    + "precision mediump float;\n"
    + "uniform samplerExternalOES u_tex;\n"
    + "varying vec2 v_tex;\n"
    + "void main() {\n"
    + " gl_FragColor = vec4(v_tex.x, v_tex.y, 1.0, 1.0);\n"
    + "}"
    + "";

  private GlShader fullscreen_vs;
  private GlShader fullscreen_fs;
  private GlProgram fullscreen_prog;
  private GlVbo fullscreen_vbo;

  /* ExoPlayer to Texture */
  private IntBuffer decoded_tex = IntBuffer.allocate(1);
  public SurfaceTexture decoded_surf;
  private SimpleExoPlayer exo_player;
  
  /* -------------------------------------------------------------- */
  
  public void onSurfaceCreated(GL10 unused, EGLConfig config) {

    if (null == exo_player) {
      throw new RuntimeException("exo_player member is null; should have been set.");
    }
    
    GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);

    if (null == fullscreen_vs) {
      fullscreen_vs = new GlShader();
      fullscreen_vs.createVertexShader(FULLSCREEN_VS);
    }
    
    if (null == fullscreen_fs) {
      fullscreen_fs = new GlShader();
      fullscreen_fs.createFragmentShader(FULLSCREEN_FS);
    }

    if (null == fullscreen_prog) {
      fullscreen_prog = new GlProgram();
      fullscreen_prog.create();
      fullscreen_prog.attachShader(fullscreen_vs.getId());
      fullscreen_prog.attachShader(fullscreen_fs.getId());
      fullscreen_prog.bindAttribLocation("a_pos", 0);
      fullscreen_prog.bindAttribLocation("a_tex", 1);
      fullscreen_prog.link();
    }

    if (null == fullscreen_vbo) {
      
      float[] verts = {
        -1.0f,  1.0f, 0.0f, 1.0f,
        -1.0f, -1.0f, 0.0f, 0.0f,
        1.0f,  1.0f, 1.0f, 1.0f,
        1.0f, -1.0f, 1.0f, 0.0f
      };

      fullscreen_vbo = new GlVbo();
      fullscreen_vbo.create();
      fullscreen_vbo.uploadStaticData(verts);
    }

    if (0 == decoded_tex.get(0)) {
      GLES20.glGenTextures(1, decoded_tex);
      GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, decoded_tex.get(0));
      GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
      GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
      GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
      GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
    }

    if (null == decoded_surf
        && 0 != decoded_tex.get(0))
      {
        decoded_surf = new SurfaceTexture(decoded_tex.get(0));
        decoded_surf.setOnFrameAvailableListener(new SurfaceTexture.OnFrameAvailableListener() {
            @Override
            public void onFrameAvailable(SurfaceTexture surfaceTexture) {
              Log.v("msg", "> onFrameAvailable");
            }
          });
        
        exo_player.setVideoSurface(new Surface(decoded_surf));
    }
    
    Log.v("msg", "Vertex shader: " +fullscreen_vs.getId());
    Log.v("msg", "Fragment shader: " +fullscreen_fs.getId());
    Log.v("msg", "Program: " +fullscreen_prog.getId());
    Log.v("msg", "VBO: " +fullscreen_vbo.getId());
  }

  public void onDrawFrame(GL10 unused) {

    decoded_surf.updateTexImage(); /* only necessary when we received a new video frame. */
    
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    fullscreen_prog.use();
    fullscreen_vbo.bind();
    fullscreen_prog.enableAttrib(0);
    fullscreen_prog.enableAttrib(1);
    fullscreen_vbo.vertexAttribPointer(0, 2, GLES20.GL_FLOAT, true, 16, 0); /* pos */
    fullscreen_vbo.vertexAttribPointer(1, 2, GLES20.GL_FLOAT, true, 16, 8); /* tex */
    fullscreen_vbo.drawTriangleStrip(0, 4);
  }

  public void onSurfaceChanged(GL10 unused, int width, int height) {
    GLES20.glViewport(0, 0, width, height);
  }

  public void setExoPlayer(SimpleExoPlayer ep) {
    exo_player = ep;
    Log.v("msg", "Setting ExoPlayer.");
  }
};
