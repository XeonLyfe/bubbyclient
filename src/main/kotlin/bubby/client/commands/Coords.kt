package bubby.client.commands

import bubby.api.chat.Chat
import bubby.api.command.Command
import bubby.client.BubbyClient
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class Coords: Command("coords", "copies your coords to your clipboard") {
  override fun execute(command: String, args: List<String>) {
    val coords = "X: ${BubbyClient.MC.player.posX.toInt()} Y: ${BubbyClient.MC.player.posY.toInt()} Z: ${BubbyClient.MC.player.posZ.toInt()}"
    Toolkit.getDefaultToolkit().systemClipboard.setContents(StringSelection(coords), null)
    Chat.info("Coords copied to clipboard")
  }
}
