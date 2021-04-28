package bubby.client.commands

import bubby.api.command.Command
import bubby.client.BubbyClient

class Shrug: Command("shrug", "\u00af\\_(\u30b7)_/\u00af") {
  override fun execute(command: String, args: List<String>) {
    BubbyClient.MC.player.sendChatMessage("\u00af\\_(\u30b7)_/\u00af")
  }
}
