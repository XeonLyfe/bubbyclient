package bubby.client.utils

import java.io.File

import javax.sound.sampled.*

class SoundManager(var file: File, var loop: Boolean): LineListener {

  private var shouldStop = false
  private var kill = false

  fun start() {
    shouldStop = false
    kill = false
    Thread {
      val audioStream = AudioSystem.getAudioInputStream(file)
      val format = audioStream.format
      val info = DataLine.Info(Clip::class.java, format)
      var audioClip = AudioSystem.getLine(info) as Clip
      audioClip.open(audioStream)
      audioClip.addLineListener(this)
      audioClip.start()
      while(!shouldStop) {
        try {
          Thread.sleep(1000)
        } catch(ignored: InterruptedException) {
          break
        }
      }
      audioClip.stop()
    }.start()
  }

  fun stop() {
    shouldStop = true
    kill = true
  }

  override fun update(event: LineEvent) {
    val type = event.type

    if(type == LineEvent.Type.STOP && loop && !kill) {
      start()
    }
  }
}
