package rox.lu;
import android.opengl.GLES20;

public class GlShader {

  /* -------------------------------------------------------------- */
  
  private int id = -1;

  /* -------------------------------------------------------------- */

  public int getId() {
    return id;
  }
  
  public void createVertexShader(String source) {
    create(GLES20.GL_VERTEX_SHADER, source);
  }

  public void createFragmentShader(String source) {
    create(GLES20.GL_FRAGMENT_SHADER, source);
  }

  public void create(int type, String source) {

    if (-1 != id) {
      throw new RuntimeException("Cannot create GlShader, already created.");
    }

    id = GLES20.glCreateShader(type);
    GLES20.glShaderSource(id, source);
    GLES20.glCompileShader(id);

    int[] result = new int[] { GLES20.GL_FALSE };
    GLES20.glGetShaderiv(id, GLES20.GL_COMPILE_STATUS, result, 0);

    if (GLES20.GL_TRUE != result[0]) {
      throw new RuntimeException("Failed to compile shader. " +GLES20.glGetShaderInfoLog(id) + ", source: " + source);
    }
  };

  /* -------------------------------------------------------------- */
};
