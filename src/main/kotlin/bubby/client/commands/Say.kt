package bubby.client.commands

import bubby.api.command.Command
import bubby.client.BubbyClient

class Say: Command("say", "says the arguments in chat") {
  override fun execute(command: String, args: List<String>) {
    BubbyClient.MC.player.sendChatMessage(args.joinToString(" "))
  }
}
