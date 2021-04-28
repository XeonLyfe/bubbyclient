package bubby.client.commands

import bubby.api.chat.Chat
import bubby.api.command.Command
import bubby.client.BubbyClient

class Panic: Command("panic", "disables all modules") {
  override fun execute(command: String, args: List<String>) {
    for(m in BubbyClient.modules.getToggledModules()) m.setToggled(false)
    Chat.info("All modules disabled")
  }
}
