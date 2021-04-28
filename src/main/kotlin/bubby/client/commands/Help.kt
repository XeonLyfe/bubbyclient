package bubby.client.commands

import bubby.api.chat.Chat
import bubby.api.command.Command
import bubby.api.command.CommandManager

class Help: Command("help", "Displays all commands") {
  override fun execute(command: String, args: List<String>) {
    CommandManager.commands.forEach {
      Chat.message(it.getName() + " | " + it.getDesc())
    }
  }
}
