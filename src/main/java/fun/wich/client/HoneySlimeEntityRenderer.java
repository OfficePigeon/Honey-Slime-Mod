package fun.wich.client;

import fun.wich.HoneySlimesMod;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.LoadedEntityModels;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.render.entity.state.SlimeEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

@Environment(value= EnvType.CLIENT)
public class HoneySlimeEntityRenderer extends MobEntityRenderer<SlimeEntity, SlimeEntityRenderState, SlimeEntityModel> {
	public static final Identifier TEXTURE = Identifier.of(HoneySlimesMod.MOD_ID, "textures/entity/slime/honey.png");
	public HoneySlimeEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new SlimeEntityModel(context.getPart(EntityModelLayers.SLIME)), 0.25f);
		this.addFeature(new OverlayFeatureRenderer(this, context.getEntityModels(), TEXTURE));
	}
	protected float getShadowRadius(SlimeEntityRenderState state) { return state.size * 0.25f; }
	protected void scale(SlimeEntityRenderState state, MatrixStack matrixStack) {
		matrixStack.scale(0.999f, 0.999f, 0.999f);
		matrixStack.translate(0., 0.001f, 0);
		float g = (float)state.size;
		float i = 1 / ((state.stretch / (g * 0.5f + 1)) + 1);
		matrixStack.scale(i * g, 1 / i * g, i * g);
	}
	public Identifier getTexture(SlimeEntityRenderState slimeEntityRenderState) { return TEXTURE; }
	public SlimeEntityRenderState createRenderState() { return new SlimeEntityRenderState(); }
	public void updateRenderState(SlimeEntity slimeEntity, SlimeEntityRenderState slimeEntityRenderState, float f) {
		super.updateRenderState(slimeEntity, slimeEntityRenderState, f);
		slimeEntityRenderState.stretch = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch);
		slimeEntityRenderState.size = slimeEntity.getSize();
	}
	public static class OverlayFeatureRenderer extends FeatureRenderer<SlimeEntityRenderState, SlimeEntityModel> {
		protected final SlimeEntityModel model;
		protected final Identifier texture;
		public OverlayFeatureRenderer(FeatureRendererContext<SlimeEntityRenderState, SlimeEntityModel> context, LoadedEntityModels loader, Identifier texture) {
			super(context);
			this.model = new SlimeEntityModel(loader.getModelPart(EntityModelLayers.SLIME_OUTER));
			this.texture = texture;
		}
		public void render(MatrixStack matrixStack, OrderedRenderCommandQueue orderedRenderCommandQueue, int i, SlimeEntityRenderState slimeEntityRenderState, float f, float g) {
			boolean bl = slimeEntityRenderState.hasOutline() && slimeEntityRenderState.invisible;
			if (!slimeEntityRenderState.invisible || bl) {
				orderedRenderCommandQueue.getBatchingQueue(1).submitModel(this.model, slimeEntityRenderState, matrixStack,
						bl ? RenderLayer.getOutline(this.texture) : RenderLayer.getEntityTranslucent(this.texture), i,
						LivingEntityRenderer.getOverlay(slimeEntityRenderState, 0),
						-1, null, slimeEntityRenderState.outlineColor, null);
			}
		}
	}
}
