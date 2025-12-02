package fun.wich.client;

import fun.wich.HoneySlimeEntity;
import fun.wich.HoneySlimesMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value= EnvType.CLIENT)
public class HoneySlimeEntityRenderer extends MobEntityRenderer<HoneySlimeEntity, SlimeEntityModel<HoneySlimeEntity>> {
	public static final Identifier TEXTURE = Identifier.of(HoneySlimesMod.MOD_ID, "textures/entity/slime/honey.png");
	public HoneySlimeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SlimeEntityModel<>(context.getPart(EntityModelLayers.SLIME)), 0.25f);
		this.addFeature(new OverlayFeatureRenderer(this, context.getModelLoader(), TEXTURE));
	}
	@Override protected float getShadowRadius(HoneySlimeEntity state) { return state.getSize() * 0.25f; }
	@Override
	protected void scale(HoneySlimeEntity slimeEntity, MatrixStack matrixStack, float f) {
		matrixStack.scale(0.999F, 0.999F, 0.999F);
		matrixStack.translate(0.0F, 0.001F, 0.0F);
		float g = (float)slimeEntity.getSize();
		float i = 1 / ((MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (g * 0.5f + 1)) + 1);
		matrixStack.scale(i * g, 1 / i * g, i * g);
	}
	@Override public Identifier getTexture(HoneySlimeEntity state) { return TEXTURE; }
	public static class OverlayFeatureRenderer extends FeatureRenderer<HoneySlimeEntity, SlimeEntityModel<HoneySlimeEntity>> {
		protected final SlimeEntityModel<HoneySlimeEntity> model;
		protected final Identifier texture;
		public OverlayFeatureRenderer(FeatureRendererContext<HoneySlimeEntity, SlimeEntityModel<HoneySlimeEntity>> context, EntityModelLoader loader, Identifier texture) {
			super(context);
			this.model = new SlimeEntityModel<>(loader.getModelPart(EntityModelLayers.SLIME_OUTER));
			this.texture = texture;
		}
		@Override
		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, HoneySlimeEntity livingEntity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
			MinecraftClient minecraftClient = MinecraftClient.getInstance();
			boolean bl = minecraftClient.hasOutline(livingEntity) && livingEntity.isInvisible();
			if (!livingEntity.isInvisible() || bl) {
				this.getContextModel().copyStateTo(this.model);
				this.model.animateModel(livingEntity, limbAngle, limbDistance, tickDelta);
				this.model.setAngles(livingEntity, limbAngle, limbDistance, animationProgress, headYaw, headPitch);
				this.model.render(matrixStack, vertexConsumerProvider.getBuffer(bl ? RenderLayer.getOutline(this.texture) : RenderLayer.getEntityTranslucent(this.texture)), i, LivingEntityRenderer.getOverlay(livingEntity, 0.0F));
			}
		}
	}
}
