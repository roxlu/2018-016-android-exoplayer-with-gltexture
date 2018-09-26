package rox.lu;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.google.android.exoplayer2.*;

public class MyGlView extends GLSurfaceView {

  private MyGlRenderer renderer;
  
  public MyGlView(Context context) {
    this(context, null);
  }

  public MyGlView(Context context,  AttributeSet attrs) {
    super(context, attrs);
    setEGLContextClientVersion(2);
    renderer = new MyGlRenderer();
    setRenderer(renderer);

    Log.v("msg", "Create MyGlView");
  }

  public void setExoPlayer(SimpleExoPlayer ep) {

    if (null == renderer) {
      throw new RuntimeException("Cannot set ExoPlayer on the renderer; not created yet.");
    }
    
    renderer.setExoPlayer(ep);
  }
};
