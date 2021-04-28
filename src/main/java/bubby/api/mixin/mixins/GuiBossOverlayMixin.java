package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import net.minecraft.client.gui.GuiBossOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiBossOverlay.class)
public class GuiBossOverlayMixin 
{
  @Inject(method = "render", at = @At("HEAD"), cancellable = true)
  public void render(CallbackInfo ci)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("NoRender").isToggled()
        && (Boolean)BubbyClient.INSTANCE.getSettings().getSettingByName("NoRender BossBar").getValue())
      ci.cancel();
  }

  @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
  public void renderBossHealth(CallbackInfo ci)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("NoRender").isToggled()
        && (Boolean)BubbyClient.INSTANCE.getSettings().getSettingByName("NoRender BossBar").getValue())
      ci.cancel();
  }
}
