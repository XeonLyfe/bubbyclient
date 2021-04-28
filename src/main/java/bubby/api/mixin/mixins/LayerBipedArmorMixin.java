package bubby.api.mixin.mixins;
import bubby.client.BubbyClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;

@Mixin(LayerBipedArmor.class)
public class LayerBipedArmorMixin
{
  @Inject(at = @At("HEAD"), method = "setModelSlotVisible", cancellable = true)
  protected void
  setModelSlotVisible(ModelBiped modelBiped, EntityEquipmentSlot entityEquipmentSlot, CallbackInfo ci)
  {
    if(BubbyClient.INSTANCE.getModules().getModuleByName("NoRender").isToggled() 
        && (Boolean)BubbyClient.INSTANCE.getSettings().getSettingByName("NoRender Armour").getValue())
    {
      ci.cancel();
      switch(entityEquipmentSlot)
      {
        case HEAD:
          modelBiped.bipedHead.showModel = false;
          modelBiped.bipedHeadwear.showModel = false;
        case CHEST:
          modelBiped.bipedBody.showModel = false;
          modelBiped.bipedLeftArm.showModel = false;
          modelBiped.bipedRightArm.showModel = false;
        case LEGS:
          modelBiped.bipedBody.showModel = false;
          modelBiped.bipedLeftLeg.showModel = false;
          modelBiped.bipedRightLeg.showModel = false;
        case FEET:
          modelBiped.bipedRightLeg.showModel = false;
          modelBiped.bipedLeftLeg.showModel = false;
          break;
      }
    }
  }
}
