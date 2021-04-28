package bubby.api.mixin.mixins;
import bubby.api.mixin.interfaces.*;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin implements IEntity
{
  @Shadow
  protected boolean isInWeb;

  @Override
  public boolean
  getInWeb()
  {
    return this.isInWeb;
  }

  @Override
  public void
  forceWeb(boolean web)
  {
    this.isInWeb = web;
  }
}
