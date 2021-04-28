package bubby.client.utils

import bubby.client.BubbyClient
import java.io.*
import java.util.*
import java.util.function.Consumer

object FileManager {
  var BC = File(BubbyClient.MC.gameDir.toString() + File.separator + "BubbyClient" + File.separator + "1.12")
  var BCM = File(BC, "Modules")

  init {
    if(!BC.exists())
      BC.mkdirs()
    if(!BCM.exists())
      BCM.mkdirs()
  }

  fun writeToFile(f: File?, s: ArrayList<String>, name: String?) {
    try {
      val file = File(f!!.absolutePath, name)
      val out = BufferedWriter(FileWriter(file))
      s.forEach(Consumer { line: String ->
        try {
          out.write("""$line""".trimIndent())
        }
        catch(e: IOException) {
        }
      })
      out.close()
    }
    catch(e: Exception) {
    }
  }

  private fun readFromFile(f: File?, name: String?): ArrayList<String> {
    val out = ArrayList<String>()
    try {
      val file = File(f!!.absolutePath, name)
      if(!file.exists()) {
        val cum = ArrayList<String>()
        cum.add("")
        writeToFile(f, cum, name)
      }
      val fstream = FileInputStream(file.absolutePath)
      val `in` = DataInputStream(fstream)
      val br = BufferedReader(InputStreamReader(`in`))
      var line: String
      while(br.readLine().also { line = it } != null) {
        out.add(line)
      }
      br.close()
      return out
    }
    catch(e: Exception) {
    }
    return out
  }

  fun saveFriends() {
    writeToFile(BC, BubbyClient.friends.friends, "Friends.txt")
  }

  fun loadFriends() {
    readFromFile(BC, "Friends.txt").forEach(Consumer { line: String? -> BubbyClient.friends.addFriendNoSave(line!!) })
  }


  fun readFileAsString(f: File, name: String): String = File(f, name).readText(Charsets.UTF_8)
}
