package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.renderer.ItemRenderer;

@Mixin(ItemRenderer.class)
public class ItemRendererMixin
{
  @Inject(at = @At("INVOKE"), method = "renderFireInFirstPerson", cancellable = true)
  private void
  renderFireInFirstPerson(CallbackInfo ci)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("NoRender").isToggled() &&
            (Boolean)BubbyClient.INSTANCE.getSettings().getSettingByName("NoRender Fire").getValue())
      ci.cancel();

  }
}
