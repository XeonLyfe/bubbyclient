package bubby.client.commands

import bubby.api.command.Command
import bubby.client.BubbyClient

class Toggle: Command("toggle", "toggles a module through a command") {
  override fun execute(command: String, args: List<String>) {
    BubbyClient.modules.getModuleByName(args[0]).toggle()
  }
}
