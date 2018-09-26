package rox.lu;

import android.util.Log;
import android.opengl.GLES20;
import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GlVbo {

  /* -------------------------------------------------------------- */
  
  private IntBuffer id = IntBuffer.allocate(1);  
  
  /* -------------------------------------------------------------- */

  public void create() {

    if (0 != id.get(0)) {
      throw new RuntimeException("GlVbo already created.");
    }

    GLES20.glGenBuffers(1, id);
  }

  public void uploadStaticData(float[] data) {

    if (0 == id.get(0)) {
      throw new RuntimeException("GlVbo not created, cannot upload data. Call `create()` first.");
    }

    int nbytes = data.length * 4;
    FloatBuffer buf = ByteBuffer.allocateDirect(nbytes).order(ByteOrder.nativeOrder()).asFloatBuffer();
    buf.put(data).position(0);
    
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id.get(0));
    GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, nbytes, buf, GLES20.GL_STATIC_DRAW);
  }

  public void bind() {
    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id.get(0));
  }

  public int getId() {
    return id.get(0);
  }

  public void vertexAttribPointer(int attrib, int numElements, int type, boolean normalize, int stride, int offset) {
    GLES20.glVertexAttribPointer(attrib, numElements, type, normalize, stride, offset); 
  }

  public void drawTriangleStrip(int offset, int count) {
    GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, offset, count);
  }
  
  /* -------------------------------------------------------------- */
};
