package fun.wich.client;

import fun.wich.HoneySlimesMod;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CrackParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.entity.EntityRendererFactories;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.util.math.random.Random;

@Environment(EnvType.CLIENT)
public class HoneySlimesClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ParticleFactoryRegistry.getInstance().register(HoneySlimesMod.PARTICLE_ITEM_HONEY, ModCrackParticle.MakeCrackParticle(new ItemStack(HoneySlimesMod.HONEY_CLUMP)));
		EntityRendererFactories.register(HoneySlimesMod.HONEY_SLIME, HoneySlimeEntityRenderer::new);
	}
	private static class ModCrackParticle extends CrackParticle {
		public ModCrackParticle(ClientWorld world, double x, double y, double z, Sprite sprite) { super(world, x, y, z, sprite); }
		public static Factory<SimpleParticleType> MakeCrackParticle(ItemStack stack) {
			return new Factory<>() {
				@Override
				public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, Random random) {
					return new ModCrackParticle(world, x, y, z, this.getSprite(stack, world, random));
				}
			};
		}
	}
}