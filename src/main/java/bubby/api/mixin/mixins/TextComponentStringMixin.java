package bubby.api.mixin.mixins;


import bubby.api.mixin.interfaces.ITextComponentString;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TextComponentString.class)
public abstract class TextComponentStringMixin implements ITextComponentString {

  @Accessor
  @Override
  public abstract void setText(String text);
}