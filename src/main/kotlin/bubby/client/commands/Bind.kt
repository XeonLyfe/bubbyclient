package bubby.client.commands

import bubby.api.chat.Chat
import bubby.api.command.Command
import bubby.client.BubbyClient
import org.lwjgl.input.Keyboard

class Bind: Command("bind", "Binds modules through a command") {
  override fun execute(command: String, args: List<String>) {
    BubbyClient.modules.getModuleByName(args[0]).bind = Keyboard.getKeyIndex(args[1].toUpperCase())
    BubbyClient.modules.saveModule(BubbyClient.modules.getModuleByName(args[0]))
    Chat.info(args[0] + " bound to " + args[1])
  }
}
