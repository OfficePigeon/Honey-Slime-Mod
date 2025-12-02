package fun.wich;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.block.*;
import net.minecraft.block.enums.NoteBlockInstrument;
import net.minecraft.entity.*;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;

import java.util.function.Function;

public class HoneySlimesMod implements ModInitializer {
	public static final String MOD_ID = "wich";
	public static final SimpleParticleType PARTICLE_ITEM_HONEY = FabricParticleTypes.simple(false);

	public static final SoundEvent ENTITY_PARROT_IMITATE_HONEY_SLIME = register("entity.parrot.imitate.honey_slime");
	public static SoundEvent register(String path) {
		Identifier id = Identifier.of(MOD_ID, path);
		return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
	}
	public static final TagKey<Biome> TAG_SPAWNS_HONEY_SLIMES = TagKey.of(RegistryKeys.BIOME, Identifier.of(MOD_ID, "spawns_honey_slimes"));
	public static final EntityType<HoneySlimeEntity> HONEY_SLIME = register(
			"honey_slime",
			EntityType.Builder.create(HoneySlimeEntity::new, SpawnGroup.MONSTER)
					.dimensions(0.52F, 0.52F)
					.eyeHeight(0.325F)
					.spawnBoxScale(4.0F)
					.maxTrackingRange(10)
	);
	public static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> type) {
		Identifier id = Identifier.of(MOD_ID, name);
		return Registry.register(Registries.ENTITY_TYPE, id, type.build(id.toString()));
	}
	public static final Block HONEY_CLUMP_BLOCK = register("honey_clump_block", Block::new, Block.Settings.create().mapColor(MapColor.ORANGE).sounds(BlockSoundGroup.HONEY).instrument(NoteBlockInstrument.BASEDRUM));
	public static final Block CRYSTALLIZED_HONEY_BRICKS = register("crystallized_honey_bricks", Block::new, Block.Settings.create().mapColor(MapColor.ORANGE).sounds(BlockSoundGroup.CORAL).instrument(NoteBlockInstrument.BASEDRUM).requiresTool().strength(1.5f, 6));
	public static final Block CRYSTALLIZED_HONEY_BRICK_STAIRS = register("crystallized_honey_brick_stairs", settings -> new StairsBlock(CRYSTALLIZED_HONEY_BRICKS.getDefaultState(), settings), Block.Settings.copy(CRYSTALLIZED_HONEY_BRICKS));
	public static final Block CRYSTALLIZED_HONEY_BRICK_SLAB = register("crystallized_honey_brick_slab", SlabBlock::new, Block.Settings.copy(CRYSTALLIZED_HONEY_BRICKS));
	public static final Block CRYSTALLIZED_HONEY_BRICK_WALL = register("crystallized_honey_brick_wall", WallBlock::new, Block.Settings.copy(CRYSTALLIZED_HONEY_BRICKS));
	public static final Block CHISELED_CRYSTALLIZED_HONEY_BRICKS = register("chiseled_crystallized_honey_bricks", Block::new, Block.Settings.copy(CRYSTALLIZED_HONEY_BRICKS));
	public static Block register(String name, Function<Block.Settings, Block> blockFactory, Block.Settings settings) {
		return Registry.register(Registries.BLOCK, RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(MOD_ID, name)), blockFactory.apply(settings));
	}
	public static final Item HONEY_SLIME_SPAWN_EGG = register("honey_slime_spawn_egg", settings -> new SpawnEggItem(HONEY_SLIME, 0xffffff, 0xffffff, settings), new Item.Settings());
	public static final Item HONEY_CLUMP = register("honey_clump", Item::new, new Item.Settings());
	public static final Item HONEY_CLUMP_BLOCK_ITEM = register("honey_clump_block", settings -> new BlockItem(HONEY_CLUMP_BLOCK, settings), new Item.Settings());
	public static final Item CRYSTALLIZED_HONEY_BRICK = register("crystallized_honey_brick", Item::new, new Item.Settings());
	public static final Item CRYSTALLIZED_HONEY_BRICKS_ITEM = register("crystallized_honey_bricks", settings -> new BlockItem(CRYSTALLIZED_HONEY_BRICKS, settings), new Item.Settings());
	public static final Item CRYSTALLIZED_HONEY_BRICK_STAIRS_ITEM = register("crystallized_honey_brick_stairs", settings -> new BlockItem(CRYSTALLIZED_HONEY_BRICK_STAIRS, settings), new Item.Settings());
	public static final Item CRYSTALLIZED_HONEY_BRICK_SLAB_ITEM = register("crystallized_honey_brick_slab", settings -> new BlockItem(CRYSTALLIZED_HONEY_BRICK_SLAB, settings), new Item.Settings());
	public static final Item CRYSTALLIZED_HONEY_BRICK_WALL_ITEM = register("crystallized_honey_brick_wall", settings -> new BlockItem(CRYSTALLIZED_HONEY_BRICK_WALL, settings), new Item.Settings());
	public static final Item CHISELED_CRYSTALLIZED_HONEY_BRICKS_ITEM = register("chiseled_crystallized_honey_bricks", settings -> new BlockItem(CHISELED_CRYSTALLIZED_HONEY_BRICKS, settings), new Item.Settings());
	public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
		return Registry.register(Registries.ITEM, RegistryKey.of(RegistryKeys.ITEM, Identifier.of(MOD_ID, name)), itemFactory.apply(settings));
	}
	@Override
	public void onInitialize() {
		Registry.register(Registries.PARTICLE_TYPE, Identifier.of(MOD_ID, "item_honey"), PARTICLE_ITEM_HONEY);
		//Attributes
		FabricDefaultAttributeRegistry.register(HONEY_SLIME, HostileEntity.createHostileAttributes());
		//Spawning
		SpawnRestriction.register(HONEY_SLIME, SpawnLocationTypes.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HoneySlimeEntity::canMobSpawn);
		BiomeModifications.addSpawn(BiomeSelectors.tag(TAG_SPAWNS_HONEY_SLIMES),
				SpawnGroup.MONSTER, HONEY_SLIME, 1, 1, 1);
		//Items
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.SPAWN_EGGS).register(itemGroup -> itemGroup.add(HONEY_SLIME_SPAWN_EGG));
		//Crystallized Honey
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(itemGroup -> itemGroup.add(HONEY_CLUMP));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> itemGroup.add(HONEY_CLUMP_BLOCK_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(itemGroup -> itemGroup.add(CRYSTALLIZED_HONEY_BRICK));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> itemGroup.add(CRYSTALLIZED_HONEY_BRICKS_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> itemGroup.add(CRYSTALLIZED_HONEY_BRICK_STAIRS_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> itemGroup.add(CRYSTALLIZED_HONEY_BRICK_SLAB_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> itemGroup.add(CRYSTALLIZED_HONEY_BRICK_WALL_ITEM));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register(itemGroup -> itemGroup.add(CHISELED_CRYSTALLIZED_HONEY_BRICKS_ITEM));
	}
}