package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import bubby.client.events.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.MoverType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public class EntityPlayerSPMixin
{
  @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;closeScreen()V"), method = "onLivingUpdate")
  public void
  closeScreen(EntityPlayerSP epSP)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("Portals").isToggled())
      return;
  }

  @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;displayGuiScreen(Lnet/minecraft/client/gui/GuiScreen;)V"), method = "onLivingUpdate")
  public void
  displayGuiScreen(Minecraft mc, GuiScreen guiScreen)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("Portals").isToggled())
      return;
  }

  @Inject(at = @At("HEAD"), method = "move")
  public void
  move(MoverType type, double x, double y, double z, CallbackInfo ci)
  {
    MoveEvent event = new MoveEvent(x, y, z);
    BubbyClient.INSTANCE.getEvents().call(event);
    if(event.isCancelled())
      ci.cancel();
  }
}
