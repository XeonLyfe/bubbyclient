package bubby.client.modules

import bubby.api.chat.Chat
import bubby.api.module.*
import bubby.api.setting.Setting

import bubby.client.BubbyClient
import bubby.client.utils.FileManager

import java.io.File

import javax.sound.sampled.*

class PlaySound: Module("PlaySound", "Plays music.wav in BubbyClient/1.12.2/", -1, Category.Misc, false), LineListener {

  val loop = Setting<Boolean>("PlaySound Loop", this).withValue(false).add()

  val file = File(FileManager.BC.absolutePath + File.separator + "music.wav")

  var shouldStop = false

  override fun onEnable() {
    shouldStop = false
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

  override fun onDisable() {
    shouldStop = true
  }

  override fun update(event: LineEvent) {
    val type = event.type

    if(type == LineEvent.Type.STOP && loop.value && isToggled()) {
      onEnable()
    }
  }
}
