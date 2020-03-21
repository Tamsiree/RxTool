package com.tamsiree.rxkit

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import java.io.IOException

/**
 *
 * @author Tamsiree
 * @date 2017/10/11
 * 提示音工具
 */
object RxBeepTool {
    private const val BEEP_VOLUME = 0.50f
    private const val VIBRATE_DURATION = 200
    private var playBeep = false
    private var mediaPlayer: MediaPlayer? = null

    @JvmStatic
    fun playBeep(mContext: Activity, vibrate: Boolean) {
        playBeep = true
        val audioService = mContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (audioService.ringerMode != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false
        }
        if (playBeep && mediaPlayer != null) {
            mediaPlayer!!.start()
        } else {
            mContext.volumeControlStream = AudioManager.STREAM_MUSIC
            mediaPlayer = MediaPlayer()
            mediaPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
            mediaPlayer!!.setOnCompletionListener { mediaPlayer -> mediaPlayer.seekTo(0) }
            val file = mContext.resources.openRawResourceFd(R.raw.beep)
            try {
                mediaPlayer!!.setDataSource(file.fileDescriptor, file.startOffset, file.length)
                file.close()
                mediaPlayer!!.setVolume(BEEP_VOLUME, BEEP_VOLUME)
                mediaPlayer!!.prepare()
            } catch (e: IOException) {
                mediaPlayer = null
            }
        }
        if (vibrate) {
            RxVibrateTool.vibrateOnce(mContext, VIBRATE_DURATION)
        }
    }
}