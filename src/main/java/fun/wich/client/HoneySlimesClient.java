package fun.wich.client;

import fun.wich.HoneySlimesMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class HoneySlimesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(HoneySlimesMod.PARTICLE_ITEM_HONEY, ModCrackParticle.MakeCrackParticle(new ItemStack(HoneySlimesMod.HONEY_CLUMP)));
		EntityRendererRegistry.register(HoneySlimesMod.HONEY_SLIME, HoneySlimeEntityRenderer::new);
	}
	private static class ModCrackParticle extends CrackParticle {
		public ModCrackParticle(ClientWorld world, double x, double y, double z, ItemStack stack) { super(world, x, y, z, stack); }
		public static ParticleFactory<SimpleParticleType> MakeCrackParticle(ItemStack stack) {
			return (simpleParticleType, world, x, y, z, velocityX, velocityY, velocityZ) -> new ModCrackParticle(world, x, y, z, stack);
		}
	}
}