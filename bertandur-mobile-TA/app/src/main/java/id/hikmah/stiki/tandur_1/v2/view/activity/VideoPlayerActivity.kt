package id.hikmah.stiki.tandur_1.v2.view.activity

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import id.hikmah.stiki.tandur_1.databinding.ActivityVideoPlayerBinding
import id.hikmah.stiki.tandur_1.v2.model.TutorialDetailData
import id.hikmah.stiki.tandur_1.v2.util.KeyIntent


class VideoPlayerActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityVideoPlayerBinding.inflate(layoutInflater)
    }
    private var tutorialDetailData: TutorialDetailData? = null

    private var isFullscreen = false
    private lateinit var youTubePlayer: YouTubePlayer
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                // if the player is in fullscreen, exit fullscreen
                youTubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        tutorialDetailData = intent.getParcelableExtra(KeyIntent.KEY_DETAIL_TUTORIAL_DATA)

        if (tutorialDetailData != null) {
            initView()
        }
    }

    private fun initView() {
        binding.apply {
            onBackPressedDispatcher.addCallback(onBackPressedCallback)

            textViewJudul.text = tutorialDetailData?.TITLE_TD.toString()
            textViewDeskripsi.text = tutorialDetailData?.DESC_TD.toString()

            val iFramePlayerOptions = IFramePlayerOptions.Builder()
                .controls(1)
                .fullscreen(1) // enable full screen button
                .build()

            youtubePlayerView.enableAutomaticInitialization = false

            youtubePlayerView.addFullscreenListener(object : FullscreenListener {
                override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                    isFullscreen = true

                    // the video will continue playing in fullscreenView
                    linearLayoutContent.visibility = View.GONE
                    fullScreenViewContainer.visibility = View.VISIBLE
                    fullScreenViewContainer.addView(fullscreenView)

                    // optionally request landscape orientation
                    // requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }

                override fun onExitFullscreen() {
                    isFullscreen = false

                    // the video will continue playing in the player
                    linearLayoutContent.visibility = View.VISIBLE
                    fullScreenViewContainer.visibility = View.GONE
                    fullScreenViewContainer.removeAllViews()
                }
            })

            youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    this@VideoPlayerActivity.youTubePlayer = youTubePlayer
                    val videoId = tutorialDetailData?.URLVIDEO_TD.toString().split("watch?v=").last()
                    youTubePlayer.loadVideo(videoId, 0f)

                    materialButtonFullScreen.setOnClickListener {
                        youTubePlayer.toggleFullscreen()
                    }
                }
            }, iFramePlayerOptions)

            lifecycle.addObserver(youtubePlayerView)
//            youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    val videoId = tutorialDetailData?.URLVIDEO_TD.toString().split("watch?v=").last()
//                    youTubePlayer.loadVideo(videoId, 0f)
//                }
//            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.youtubePlayerView.release();
    }
}