package bubby.api.mixin.mixins;
import bubby.api.mixin.interfaces.IRenderGlobal;
import bubby.client.BubbyClient;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.shader.ShaderLinkHelper;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderGlobal.class)
public class RenderGlobalMixin implements IRenderGlobal {
  boolean isPogShader = false;
  public void setPogShader(boolean a) {
    isPogShader = a;
  }

  public boolean isPogShader() {
    return isPogShader;
  }

  @Shadow
  private ShaderGroup entityOutlineShader;

  @Shadow
  private Framebuffer entityOutlineFramebuffer;

  @Inject(at = @At("RETURN"), method = "makeEntityOutlineShader")
  public void makeEntityOutlineShader(CallbackInfo ci) {
    isPogShader = false;
  }

  // 99% of this is from the normal code. Mainly just changed the resourcelocation
  public void setGameingShader() {
    if (OpenGlHelper.shadersSupported) {
      if (ShaderLinkHelper.getStaticShaderLinkHelper() == null) {
        ShaderLinkHelper.setNewStaticShaderLinkHelper();
      }

      ResourceLocation resourcelocation = new ResourceLocation("minecraft", "pogOutline.json");

      try {
        this.entityOutlineShader = new ShaderGroup(BubbyClient.INSTANCE.getMC().getTextureManager(), BubbyClient.INSTANCE.getMC().getResourceManager(), BubbyClient.INSTANCE.getMC().getFramebuffer(), resourcelocation);
        this.entityOutlineShader.createBindFramebuffers(BubbyClient.INSTANCE.getMC().displayWidth, BubbyClient.INSTANCE.getMC().displayHeight);
        this.entityOutlineFramebuffer = this.entityOutlineShader.getFramebufferRaw("final");
      } catch (IOException ioexception) {
        this.entityOutlineShader = null;
        this.entityOutlineFramebuffer = null;
      } catch (JsonSyntaxException jsonsyntaxexception) {
        this.entityOutlineShader = null;
        this.entityOutlineFramebuffer = null;
      }
    } else {
      this.entityOutlineShader = null;
      this.entityOutlineFramebuffer = null;
    }
    isPogShader = true;
  }
}
