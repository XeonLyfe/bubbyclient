package bubby.client.modules

import bubby.api.module.*
import bubby.api.setting.*
import bubby.client.utils.*

import java.io.File

class Music: Module("Music", "Plays BubbyClient/1.12.2/music.wav", -1, Category.Misc) {

  val file = File(FileManager.BC.absolutePath + File.separator + "music.wav")

  val loop = Setting<Boolean>("Music Loop", this).withValue(false).add()

  val soundManager = SoundManager(file, false)

  override fun onEnable() {
    soundManager.loop = loop.value
    soundManager.start()
  }

  override fun onDisable() {
    soundManager.stop()
  }
}
