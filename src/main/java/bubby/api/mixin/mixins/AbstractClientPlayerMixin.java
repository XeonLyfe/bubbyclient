package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {
  @Shadow @Nullable protected abstract NetworkPlayerInfo getPlayerInfo();

  @Inject(method = "getLocationCape", at = @At("HEAD"), cancellable = true)
  public void getLocationCape(CallbackInfoReturnable<ResourceLocation> cir){
    UUID uuid = getPlayerInfo().getGameProfile().getId();

    if(BubbyClient.INSTANCE.getModules().getModuleByName("Capes").isToggled()
        && BubbyClient.INSTANCE.getCapeUtils().hasCape(uuid)) {
      cir.setReturnValue(BubbyClient.INSTANCE.getCapeUtils().getCape());
    }
  }
}
