package bubby.client.commands
import bubby.api.chat.Chat
import bubby.api.command.Command
import bubby.client.BubbyClient
import java.io.BufferedReader
import java.io.InputStreamReader

class Terminal: Command("terminal", "Run commands through the termainl/prompt, commands run from .minecraft") {
  override fun execute(command: String, args: List<String>) {
    //Runtime.getRuntime().exec(args.joinToString(" "))
    Thread{
      val builder = ProcessBuilder(args.joinToString(" "))
      builder.redirectErrorStream(true)
      builder.redirectOutput()
      val process = builder.start()
      val inputStream = process.getInputStream()
      val reader = BufferedReader(InputStreamReader(inputStream))
      var line: String? = ""
      while(line != null) {
        Chat.message(line)
        line = reader.readLine()
      }
    }.start()
  }
}
