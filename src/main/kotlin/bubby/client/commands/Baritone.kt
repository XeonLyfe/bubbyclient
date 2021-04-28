package bubby.client.commands
//import baritone.api.BaritoneAPI;
import bubby.api.chat.Chat
import bubby.api.command.Command

class Baritone: Command("b", "Baritone API again pog") {
  override fun execute(command: String, args: List<String>) {
    Chat.info("Baritone not yet implemented")
    //BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(args.joinToString(" "));
  }
}
