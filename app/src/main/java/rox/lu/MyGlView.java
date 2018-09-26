package rox.lu;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.video.*;

public class MyGlView extends GLSurfaceView implements VideoListener {
  
  /* ---------------------------------------------- */
  
  private MyGlRenderer renderer;
  private SimpleExoPlayer player;

  /* ---------------------------------------------- */
  
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
    player = ep;
    player.addVideoListener(this);
    renderer.setExoPlayer(ep);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int measuredWidth = getMeasuredWidth();
    int measuredHeight = getMeasuredHeight();

    setMeasuredDimension(measuredWidth, 100);

    Log.v("msg", "onMeasure: " +measuredWidth +", " +measuredHeight);
  }

  /* VideoListener                                  */
  /* ---------------------------------------------- */
  
  @Override
  public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
    Log.v("msg", "------------------------------------------");
    Log.v("msg", "width = " + width + " height = " + height + " unappliedRotationDegrees = " + unappliedRotationDegrees + " pixelWidthHeightRatio = " + pixelWidthHeightRatio);
    //videoAspect = ((float) width / height) * pixelWidthHeightRatio;
    // Log.d(TAG, "videoAspect = " + videoAspect);
    //requestLayout();
  }

  @Override
  public void onRenderedFirstFrame() {
  }
};
