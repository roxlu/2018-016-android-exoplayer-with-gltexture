package rox.lu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.net.Uri;
import android.util.Log;
import com.google.android.exoplayer2.*;
import com.google.android.exoplayer2.trackselection.*;
import com.google.android.exoplayer2.source.*;
import com.google.android.exoplayer2.upstream.*;
import com.google.android.exoplayer2.ui.*;
import com.google.android.exoplayer2.source.dash.*;
import com.google.android.exoplayer2.util.*;

public class MainActivity extends AppCompatActivity {

  /* ---------------------------------------------- */

  private SimpleExoPlayer player;
  private PlayerView player_view;
  private DefaultBandwidthMeter bandwidth_meter;
  private DefaultTrackSelector track_selector;
  private MyGlView gl_view;
  
  /* ---------------------------------------------- */
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    /* Access the MyGlView */
    gl_view = (rox.lu.MyGlView) findViewById(R.id.my_gl_view);
    if (null == gl_view) {
      throw new RuntimeException("Failed to access the gl view.");
    }

    /* Creat player and bandwidth meter. */
    bandwidth_meter = new DefaultBandwidthMeter();
    TrackSelection.Factory track_sel_fac = new AdaptiveTrackSelection.Factory(bandwidth_meter);
    track_selector = new DefaultTrackSelector(track_sel_fac);
    player = ExoPlayerFactory.newSimpleInstance(this, track_selector);

    /* Attach the player to the view */
    player_view = findViewById(R.id.player_view);
    player_view.requestFocus();
    player_view.setPlayer(player);

    /* Load a video */
    DataSource.Factory data_source_fac = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "roxlu"), bandwidth_meter);
    Uri video_uri = Uri.parse("http://demo.unified-streaming.com/video/ateam/ateam.ism/ateam.mpd");
    MediaSource video_media_source = new ExtractorMediaSource.Factory(data_source_fac).createMediaSource(video_uri);
    Handler main_handler = new Handler();
    DashMediaSource dash_media_source = new DashMediaSource(video_uri, data_source_fac, new DefaultDashChunkSource.Factory(data_source_fac), null, null);

    /* Prepare for playback */
    player.prepare(dash_media_source);
    player.setPlayWhenReady(true);

    /* Pass along the ExoPlayer so the the GlView/Renderer can setup a texture surface. */
    gl_view.setExoPlayer(player);
  }

  /* ---------------------------------------------- */
}
