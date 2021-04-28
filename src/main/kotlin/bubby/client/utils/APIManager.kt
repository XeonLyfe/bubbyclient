package bubby.client.utils

import com.google.common.collect.Maps
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.apache.commons.io.IOUtils
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

class APIManager {
  private val uuidNameCache: MutableMap<String, String?> = Maps.newConcurrentMap()
  fun unload() {
    uuidNameCache.clear()
  }

  fun resolveName(uuidIn: String): String? {
    var uuid = uuidIn.replace("-", "")
    if (uuidNameCache.containsKey(uuid)) {
      return uuidNameCache[uuid]
    }
    val url = "https://api.mojang.com/user/profiles/$uuid/names"
    try {
      val nameJson = IOUtils.toString(URL(url))
      if (nameJson != null && nameJson.isNotEmpty()) {
        val jsonArray = JsonParser().parse(nameJson) as JsonArray
        val latestName = jsonArray[jsonArray.size() - 1] as JsonObject
        return latestName["name"].toString()
      }
    }
    catch (e: MalformedURLException) {
      e.printStackTrace()
    }
    catch (e: IOException) {
      e.printStackTrace()
    }
    return null
  }
}
