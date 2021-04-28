package bubby.client.modules
import bubby.api.module.*
import net.minecraft.client.entity.EntityOtherPlayerMP
import com.mojang.authlib.GameProfile
import java.util.UUID;

class FakePlayer: Module("FakePlayer", "creates a fake player", -1, Category.Misc, true) {
  private lateinit var fakePlayer: EntityOtherPlayerMP

  override fun onEnable() {
    try {
      fakePlayer = EntityOtherPlayerMP(mc.world, GameProfile(UUID.fromString("fdee323e-7f0c-4c15-8d1c-0f277442342a"), "fit"));
      fakePlayer.copyLocationAndAnglesFrom(mc.player);
      fakePlayer.rotationYawHead = mc.player.rotationYawHead;
      mc.world.addEntityToWorld(-100, fakePlayer);
    }
    catch(e: Exception) {

    }
  }

  override fun onDisable() {
    try {
      mc.world.removeEntity(fakePlayer);
    }
    catch(e: Exception) {

    }
  }
}
