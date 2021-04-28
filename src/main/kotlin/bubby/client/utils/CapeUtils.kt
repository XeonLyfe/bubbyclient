package bubby.client.utils

import bubby.client.BubbyClient
import net.minecraft.util.ResourceLocation
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.*

class CapeUtils {
  var uuids: MutableList<UUID> = ArrayList()

  fun hasCape(id: UUID?): Boolean {
    return uuids.contains(id)
  }

  fun getCape(): ResourceLocation {
    when(BubbyClient.settings.getSettingByName("Capes Cape").value) {
      "BlueGrey" -> return ResourceLocation("minecraft", "bg.png")
      "Black" -> return ResourceLocation("minecraft", "nw.png")
    }
    return ResourceLocation("minecraft", "tc.png")
  }

  init {
    try {
      val pastebin = URL("https://pastebin.com/raw/BPTp6AX0")
      val i = BufferedReader(InputStreamReader(pastebin.openStream()))
      var inputLine: String?
      while(i.readLine().also { inputLine = it } != null) {
        uuids.add(UUID.fromString(inputLine))
      }
    }
    catch(ignored: Exception) {
    }
  }
}
