package rox.lu;

import android.opengl.GLES20;

public class GlProgram {

  /* -------------------------------------------------------------- */

  private int id = -1;

  /* -------------------------------------------------------------- */

  public void create() {
    
    if (-1 != id) {
      throw new RuntimeException("GlProgram already created.");
    }

    id = GLES20.glCreateProgram();
  }

  public void attachShader(int shaderId) {

    if (shaderId <= 0) {
      throw new RuntimeException("Invalid shader id, cannot attach: " +shaderId);
    }

    if (-1 == id) {
      throw new RuntimeException("Cannot attach shader, program not created yet. Call `create()` first.");
    }

    GLES20.glAttachShader(id, shaderId);
  }

  public void link() {

    int[] result = new int[] { GLES20.GL_FALSE };
    
    if (-1 == id) {
      throw new RuntimeException("Cannot link shader because it's not created yet. Call `create()` first.");
    }

    GLES20.glLinkProgram(id);
    GLES20.glGetProgramiv(id, GLES20.GL_LINK_STATUS, result, 0);

    if (GLES20.GL_TRUE != result[0]) {
      throw new RuntimeException("Failed to link the shader.");
    }
  }

  public void use() {
    GLES20.glUseProgram(id);
  }

  public int getId() {
    return id;
  }

  public int getAttribLocation(String name) {
    return GLES20.glGetAttribLocation(id, name);
  }

  public void enableAttrib(String name) {
    int loc = getAttribLocation(name);
    GLES20.glEnableVertexAttribArray(loc);
  }

  public void enableAttrib(int loc) {
    GLES20.glEnableVertexAttribArray(loc);
  }

  public void bindAttribLocation(String name, int loc) {
    
    if (-1 == id) {
      throw new RuntimeException("Cannot bind attribute location for `" +name +"` because the program is not created yet. Call `create()` first.");
    }
    
    GLES20.glBindAttribLocation(id, loc, name);
  }
    
  /* -------------------------------------------------------------- */
  
};
