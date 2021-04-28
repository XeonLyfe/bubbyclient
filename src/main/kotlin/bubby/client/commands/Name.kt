package bubby.client.commands

import bubby.api.command.Command
import bubby.client.BubbyClient
import org.lwjgl.opengl.Display

class Name: Command("Name", "Displays all commands") {
  override fun execute(command: String, args: List<String>) {
    BubbyClient.name = args.joinToString(" ")
    Display.setTitle(BubbyClient.name + " " + BubbyClient.version);
  }
}



//bubbyroosh more like meanie