package com.example.mymusic

import android.app.slice.Slice
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mymusic.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import java.sql.Time
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var mediaPlayer: MediaPlayer
    var isplayer=true
    var isuserchanging=false
    var Ismute=false
    lateinit var timer:Timer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        PreperMusic()
        binding.btnPlay.setOnClickListener{ConfigureMusic()}
        binding.btnbefore.setOnClickListener{GoBeforMusic()}
        binding.btnNext.setOnClickListener{GoNextMusic()}
        binding.voloumoffon.setOnClickListener{SetVoloumOFFon()}
        binding.slidermain.addOnChangeListener { slider, value, fromUser ->
            binding.txtLeft.text=convertmilitostring(value.toLong())
            isuserchanging=fromUser
        }
        binding.slidermain.addOnSliderTouchListener(object : Slider.OnSliderTouchListener{
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                mediaPlayer.seekTo(slider.value.toInt())
            }

        })
    }

    private fun SetVoloumOFFon() {
        val audiomanager=getSystemService(AUDIO_SERVICE) as AudioManager
        if(Ismute){
            audiomanager.adjustVolume(AudioManager.ADJUST_UNMUTE,AudioManager.FLAG_SHOW_UI)
            binding.voloumoffon.setImageResource(R.drawable.ic_volume_on)
            Ismute=false

        }else{
            audiomanager.adjustVolume(AudioManager.ADJUST_MUTE,AudioManager.FLAG_SHOW_UI)
            binding.voloumoffon.setImageResource(R.drawable.ic_volume_off)
            Ismute=true


        }


    }

    private fun GoNextMusic() {
        val now=mediaPlayer.currentPosition
        val newvalue=now+15000
        mediaPlayer.seekTo(newvalue)
    }

    private fun GoBeforMusic() {
        val now=mediaPlayer.currentPosition
        val newvalue=now-15000
        mediaPlayer.seekTo(newvalue)
    }

    private fun ConfigureMusic() {
        if(isplayer){
            mediaPlayer.pause()
            binding.btnPlay.setImageResource(R.drawable.ic_play)
            isplayer=false

        }else{
            mediaPlayer.start()
            binding.btnPlay.setImageResource(R.drawable.ic_pause)
            isplayer=true


        }
    }

    private fun PreperMusic() {
       mediaPlayer=MediaPlayer.create(this,R.raw.music)
        mediaPlayer.start()
        isplayer=true
        binding.btnPlay.setImageResource(R.drawable.ic_pause)
        binding.slidermain.valueTo= mediaPlayer.duration.toFloat()
        binding.txtRight.text=convertmilitostring(mediaPlayer.duration.toLong())
        timer= Timer()
        timer.schedule(object : TimerTask(){
            override fun run() {
                runOnUiThread{
                    if(!isuserchanging) {
                        binding.slidermain.value = mediaPlayer.currentPosition.toFloat()
                    }
                    ///binding.txtLeft.text=convertmilitostring(mediaPlayer.currentPosition.toLong())

                }
            }

        },1000,1000)


    }
    private fun convertmilitostring(duration: Long):String{
        val seconde=duration/1000%60
        val minute=duration/(1000*60)%60
        return java.lang.String.format(Locale.US,"%02d:%02d",minute,seconde)


    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.isLooping=true
        timer.cancel()
        mediaPlayer.release()
    }
}