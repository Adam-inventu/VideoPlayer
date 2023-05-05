package com.silverorange.videoplayer

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.silverorange.videoplayer.data.model.Video
import com.silverorange.videoplayer.data.viewModel.VideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val mTAG = "MainActivity"

    private lateinit var playerView: PlayerView
    private lateinit var titleText: TextView

    private lateinit var player: ExoPlayer
    private lateinit var playPauseButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var previousButton: ImageButton
    private lateinit var markwon: Markwon
    private lateinit var videoDetailsText: TextView
    private lateinit var videoTitleText: TextView
    private lateinit var videoAuthorText: TextView

    private val videoViewModel: VideoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playerView = findViewById(R.id.player_view)
        titleText = findViewById(R.id.toolbar_title)

        playPauseButton = findViewById(R.id.button_play_pause)
        nextButton = findViewById(R.id.button_next)
        previousButton = findViewById(R.id.button_previous)
        videoDetailsText = findViewById(R.id.video_details_text)
        videoTitleText = findViewById(R.id.video_title_text)
        videoAuthorText = findViewById(R.id.video_author_text)

        titleText.text = getString(R.string.string_app_name)

        // Instantiate the player.
        player = ExoPlayer.Builder(applicationContext).build()
        playerView.player = player

        markwon = Markwon.create(applicationContext)


        lifecycleScope.launchWhenStarted {
            videoViewModel.videos.collectLatest { videos ->
                videoViewModel.currentVideo.collectLatest {
                    if (it != null) {
                        playVideo(it)
                        setDetailsText(it)
                        previousButton.isClickable = it != videos.first()
                        nextButton.isClickable = it != videos.last()
                    } else {
                        nextButton.isClickable = false
                        previousButton.isClickable = false
                    }
                }
            }

        }
        videoViewModel.refreshVideos()


        playPauseButton.setOnClickListener {
            if (player.isPlaying) {
                player.pause()
                playPauseButton.setImageResource(R.drawable.ic_play)
            } else {
                player.play()
                playPauseButton.setImageResource(R.drawable.ic_pause)
            }
        }

        nextButton.setOnClickListener {
            videoViewModel.getNextVideo()
        }

        previousButton.setOnClickListener {
            videoViewModel.getPreviousVideo()
        }
    }

    private fun playVideo(video: Video) {
        try {
            val videoUri = Uri.parse(video.fullURL)
            val mediaItem = MediaItem.fromUri(videoUri)
            player.setMediaItem(mediaItem)
            player.prepare()
            player.pause()
            playPauseButton.setImageResource(R.drawable.ic_play)
        } catch (error: Error) {
            Log.d(mTAG, error.toString())
        }
    }

    private fun setDetailsText(video: Video) {
        videoAuthorText.text = video.author.name
        videoTitleText.text = video.title
        markwon.setMarkdown(videoDetailsText, video.description)
    }

}