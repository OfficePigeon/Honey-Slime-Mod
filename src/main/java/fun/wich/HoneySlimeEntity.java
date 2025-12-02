package fun.wich;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AttackGoal;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;

public class HoneySlimeEntity extends SlimeEntity {
	public HoneySlimeEntity(EntityType<? extends SlimeEntity> entityType, World world) { super(entityType, world); }
	@Override
	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(2, new HoneySlimeAttackGoal());
	}
	@Override public boolean canTarget(LivingEntity entity) { return isPoisoned(entity) && super.canTarget(entity); }
	@Override protected ParticleEffect getParticles() { return HoneySlimesMod.PARTICLE_ITEM_HONEY; }
	protected class HoneySlimeAttackGoal extends AttackGoal {
		public HoneySlimeAttackGoal() { super(HoneySlimeEntity.this); }
		@Override public boolean canStart() { return super.canStart() && isPoisoned(HoneySlimeEntity.this.getTarget()); }
		@Override public boolean shouldContinue() { return super.shouldContinue() && isPoisoned(HoneySlimeEntity.this.getTarget()); }
	}
	public static boolean isPoisoned(LivingEntity entity) { return entity != null && entity.hasStatusEffect(StatusEffects.POISON); }
}
