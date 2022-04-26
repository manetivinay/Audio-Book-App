package com.vinaymaneti.audiobookapp.player

import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.vinaymaneti.audiobookapp.AudioBooksModel
import com.vinaymaneti.audiobookapp.R
import com.vinaymaneti.audiobookapp.utils.AudioBookUtils
import java.util.concurrent.TimeUnit


const val TAG = "PlayerActivity"

class PlayerActivity : AppCompatActivity() {

    private var rid: Int = 0
    lateinit var runnable: Runnable
    private var handler = Handler()
    private var audioPath = ""
    private lateinit var playPauseBtn: ImageView
    private lateinit var seekBar: SeekBar
    private lateinit var startTimer: TextView
    private lateinit var endTimer: TextView
    private lateinit var bookTitle: TextView
    private lateinit var albumTitle: TextView
    private lateinit var authorTitle: TextView
    private lateinit var imageCover: ImageView
    private lateinit var previousLeftBtn: ImageView
    private lateinit var nextRightBtn: ImageView
    private var index: Int = 0
    private lateinit var model: AudioBooksModel

    private lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        playPauseBtn = findViewById(R.id.play_pause_button)
        seekBar = findViewById(R.id.seek_bar)
        startTimer = findViewById(R.id.start_timer)
        endTimer = findViewById(R.id.end_timer)
        bookTitle = findViewById(R.id.book_title)
        albumTitle = findViewById(R.id.album_title)
        authorTitle = findViewById(R.id.author_title)
        imageCover = findViewById(R.id.image_cover)
        previousLeftBtn = findViewById(R.id.previous_left_button)
        nextRightBtn = findViewById(R.id.next_right_button)

        seekBar.progress = 0

        val bundle = intent
        bundle?.let {
            model = intent.getParcelableExtra("audioBookModel")!!
            index = intent.getIntExtra("position", 0)
            setUpViews()
        }

        handleMediaPlayer()

        previousLeftBtn.setOnClickListener {
//            if reached to 0 need to assign values to list size
            Log.d(TAG, "before ::$index")
            index -= 1
            Log.d(TAG, "before ::$index")
            if(index >=0) {
                getModelItem()
                setUpViews()
                mediaPlayer.stop()
                playPauseBtn.setImageResource(R.drawable.play)
                handleMediaPlayer()
            } else {
                Toast.makeText(
                    this@PlayerActivity,
                    "you have reached to first time, go with next item",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        nextRightBtn.setOnClickListener {
//            if reached to maxleght need to assign values to initial ist
            Log.d(TAG, "before ::$index")
            index += 1
            Log.d(TAG, "before ::$index")
            if(index <= 5) {
                getModelItem()
                setUpViews()
                mediaPlayer.stop()
                playPauseBtn.setImageResource(R.drawable.play)
                handleMediaPlayer()
            } else {
                Toast.makeText(
                    this@PlayerActivity,
                    "you have reached to last time, go with previous item",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun handleMediaPlayer() {
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC) //set streaming according to ur needs

            rid = resources.getIdentifier("$packageName:raw/$audioPath", null, null)
            mediaPlayer.setDataSource(
                this@PlayerActivity,
                Uri.parse(("android.resource://$packageName/raw/$rid"))
            )
            mediaPlayer.isLooping = true
            mediaPlayer.setOnPreparedListener { mp ->
                if (mp == mediaPlayer) {
                    runnable = Runnable {
                        seekBar.progress = mediaPlayer.currentPosition
                        val getCurrent = mediaPlayer?.currentPosition
                        startTimer?.setText(
                            String.format(
                                "%02d:%02d ",
                                TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong() as Long),
                                TimeUnit.MILLISECONDS.toSeconds(getCurrent?.toLong()) -
                                        TimeUnit.MINUTES.toSeconds(
                                            TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong())
                                        )
                            )
                        )
                        endTimer?.text = getAudioFileLength()
                        handler.postDelayed(runnable, 1000)
                    }
                    handler.postDelayed(runnable, 1000)
                }

            }
            mediaPlayer.prepare()

            // add the maximum value of our seekbar the duration of the album
            seekBar.max = mediaPlayer.duration

            playPauseBtn.setOnClickListener {
                if (mediaPlayer.isPlaying) {
                    mediaPlayer.pause();
                    playPauseBtn.setImageResource(R.drawable.play)
                } else {
                    mediaPlayer.start()
                    playPauseBtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24)
                }
            }
        } catch (error: Exception) {
            Log.d(TAG, error.message.toString())
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, position: Int, changed: Boolean) {
                if (changed) {
                    mediaPlayer.seekTo(position)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })




        mediaPlayer.setOnCompletionListener {
            playPauseBtn.setImageResource(R.drawable.play)
            seekBar.progress = 0
        }
    }

    private fun setUpViews() {
        audioPath = model!!.audioPath
        bookTitle.text = model.bookTitle
        albumTitle.text = model.bookTitle
        authorTitle.text = model.authorName
        imageCover.setImageBitmap(model.coverPhoto)
    }

    private fun getModelItem() {
        val audiList = AudioBookUtils.addItemsFromJSON()
        for ((indexItem, value) in audiList.withIndex()) {
            println("the element at $indexItem is $value")
            if (index == indexItem) {
                model = audiList[index]
                return
            }
        }
    }

    private fun getAudioFileLength(): String? {
        val stringBuilder = StringBuilder()
        try {
            val uri = Uri.parse(("android.resource://$packageName/raw/$rid"))
            val mmr = MediaMetadataRetriever()
            mmr.setDataSource(this@PlayerActivity, uri)
            val duration = mmr.extractMetadata(METADATA_KEY_DURATION)
            val millSecond = duration!!.toInt()
            if (millSecond < 0) return 0.toString()
//            if (!true) return millSecond.toString()
            val hours: Int
            val minutes: Int
            var seconds = millSecond / 1000
            hours = seconds / 3600
            minutes = seconds / 60 % 60
            seconds %= 60
            if (hours in 1..9) stringBuilder.append("0").append(hours)
                .append(":") else if (hours > 0) stringBuilder.append(hours).append(":")
            if (minutes < 10) stringBuilder.append("0").append(minutes)
                .append(":") else stringBuilder.append(minutes).append(":")
            if (seconds < 10) stringBuilder.append("0").append(seconds) else stringBuilder.append(
                seconds
            )
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }



    override fun onPause() {
        super.onPause()
        mediaPlayer.let {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause();
                playPauseBtn.setImageResource(R.drawable.play)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.let {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.stop()
                mediaPlayer.release()
            }
        }
    }

}