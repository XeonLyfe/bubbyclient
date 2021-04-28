package bubby.api.command

import bubby.api.chat.Chat
import bubby.client.commands.*

object CommandManager {
  const val prefix = "."
  val commands = listOf(
    Baritone(),
    Bind(),
    Coords(),
    Friend(),
    Help(),
    Panic(),
    Say(),
    Shrug(),
    Terminal(),
    Toggle(),
    Name()
  )

  fun runCommand(input: String) {
    val argss = input.split(" ")
    val command = argss[0]
    val args = input.substring(command.length).trim()

    commands.forEach {
      if(it.getName().equals(command, true)) try {
        it.execute(command, args.split(" "))
      }
      catch(e: Exception) {
        Chat.error("Invalid Syntax")
      }
    }
  }
}
