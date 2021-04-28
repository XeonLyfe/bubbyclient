package bubby.api.mixin.mixins;
import bubby.api.command.CommandManager;
import bubby.client.BubbyClient;
import bubby.client.events.*;
import io.netty.channel.ChannelHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.network.Packet;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.client.CPacketChatMessage;

@Mixin(NetworkManager.class)
public class NetworkManagerMixin
{
  @Inject(at = @At("HEAD"), method = "sendPacket(Lnet/minecraft/network/Packet;)V", cancellable = true)
  public void
  sendPacket(Packet<?> packet, CallbackInfo ci)
  {
    SendPacketEvent event = new SendPacketEvent(packet);
    BubbyClient.INSTANCE.getEvents().call(event);
    if(event.isCancelled())
      ci.cancel();

    if(packet instanceof CPacketChatMessage)
    {
      CPacketChatMessage pack = (CPacketChatMessage)packet;
      if(pack.getMessage().startsWith(CommandManager.prefix))
      {
        CommandManager.INSTANCE.runCommand(pack.getMessage().substring(CommandManager.prefix.length()));
        ci.cancel();
      }
    }
  }

  @Inject(at = @At("HEAD"), method = "channelRead0", cancellable = true)
  public void
  channelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo ci)
  {
    ReadPacketEvent event = new ReadPacketEvent(packet);
    BubbyClient.INSTANCE.getEvents().call(event);
    if(event.isCancelled())
      ci.cancel();
  }
}
