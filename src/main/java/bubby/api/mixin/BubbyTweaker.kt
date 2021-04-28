package bubby.api.mixin

import io.github.impactdevelopment.simpletweaker.SimpleTweaker
import net.minecraft.launchwrapper.LaunchClassLoader
import org.spongepowered.asm.launch.MixinBootstrap
import org.spongepowered.asm.mixin.Mixins

open class BubbyTweaker:
SimpleTweaker()
{
  override fun injectIntoClassLoader(launchClassLoader: LaunchClassLoader)
  {
    super.injectIntoClassLoader(launchClassLoader)
    MixinBootstrap.init()
    Mixins.addConfiguration("mixins.bubby.json")
  }
  fun getModContainerClass(): String? = null
}