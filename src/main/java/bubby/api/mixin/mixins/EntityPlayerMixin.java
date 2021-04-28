package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public class EntityPlayerMixin
{
  @Inject(at = @At("HEAD"), method = "isPushedByWater", cancellable = true)
  private void
  isPushedByWater(CallbackInfoReturnable<Boolean> ci)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("Velocity").isToggled())
      ci.setReturnValue(false);
  }

  @ModifyConstant(method = "attackTargetEntityWithCurrentItem", constant = @Constant(doubleValue = 0.6d))
  private double
  attackTargetEntityWithCurrentItem(double oldValue)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("Velocity").isToggled())
      return 1.f;
    return oldValue;
  }
}
