package bubby.client.commands

import bubby.api.chat.Chat
import bubby.api.command.Command
import bubby.client.BubbyClient

class Friend: Command("friend", "add/remove friends") {
  override fun execute(command: String, args: List<String>) {
    if(args[0].equals("add", true)) {
      BubbyClient.friends.addFriend(args[1])
      Chat.info(args[1] + " added to friends")
    }
    else if(args[0].equals("remove", true)) {
      BubbyClient.friends.removeFriend(args[1])
      Chat.info(args[1] + " removed from friends")
    }
    else if(args[0].equals("list", true)) {
      for(s in BubbyClient.friends.friends) Chat.info(s)
    }
  }
}
