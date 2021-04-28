package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import bubby.client.events.*;
import me.rina.turok.render.opengl.TurokRenderGL;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.renderer.EntityRenderer;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin
{
  @Inject(at = @At(value = "INVOKE_STRING", target = "net/minecraft/profiler/Profiler.endStartSection(Ljava/lang/String;)V", args = {"ldc=hand"}), method = "renderWorldPass")
  private void
  hand(int pass, float partialTicks, long finishTimeNano, CallbackInfo ci)
  {
    BubbyClient.INSTANCE.getEvents().call(new Render3DEvent());
  }

  @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/client/gui/GuiIngame.renderGameOverlay(F)V"), method = "updateCameraAndRender")
  private void
  updateCameraAndRender$renderGameOverlay(GuiIngame guiIngame, float partialTicks)
  {
    guiIngame.renderGameOverlay(partialTicks);

    BubbyClient.INSTANCE.getComponents().onRenderComponentList();
    BubbyClient.INSTANCE.getComponents().onCornerDetectorComponentList(partialTicks);

    // why is it like this rina
    TurokRenderGL.prepareOverlay();
    TurokRenderGL.releaseOverlay();
  }

  @Inject(at = @At("HEAD"), method = "hurtCameraEffect", cancellable = true)
  public void
  hurtCameraEffect(float partialTicks, CallbackInfo ci)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("NoRender").isToggled()
        && (Boolean)BubbyClient.INSTANCE.getSettings().getSettingByName("NoRender HurtCam").getValue())
      ci.cancel();
  }
}
