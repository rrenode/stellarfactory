// src\main\java\design\rrenode\stellarfactory\registry\ModRegistry.java
package design.rrenode.stellarfactory.registry;

import design.rrenode.stellarfactory.StellarFactory;
import design.rrenode.stellarfactory.blocks.AtmosphericCondenserBlock;
import design.rrenode.stellarfactory.entities.AtmosphericCondenserEntity;
import design.rrenode.stellarfactory.registry.ModRegistry;
import design.rrenode.stellarfactory.items.HandScannerItem;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModRegistry {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(BuiltInRegistries.BLOCK, StellarFactory.MODID);

    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(BuiltInRegistries.ITEM, StellarFactory.MODID);

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, StellarFactory.MODID);

    public static final DeferredHolder<Block, Block> ATMOSPHERIC_CONDENSER_BLOCK =
        BLOCKS.register("atmospheric_condenser", () ->
            new AtmosphericCondenserBlock(BlockBehaviour.Properties.of().strength(2.0F).noOcclusion()));

    public static final DeferredHolder<Item, BlockItem> ATMOSPHERIC_CONDENSER_BLOCK_ITEM =
        ITEMS.register("atmospheric_condenser", () -> new BlockItem(ATMOSPHERIC_CONDENSER_BLOCK.get(), new Item.Properties()));

    @SuppressWarnings({ "DataFlowIssue", "null" })
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AtmosphericCondenserEntity>> ATMOSPHERIC_CONDENSER_ENTITY =
        BLOCK_ENTITIES.register("atmospheric_condenser", () ->
            BlockEntityType.Builder.of(AtmosphericCondenserEntity::new, ATMOSPHERIC_CONDENSER_BLOCK.get()).build(null));
    
    public static final DeferredHolder<Item, HandScannerItem> HAND_SCANNER =
        ITEMS.register("hand_scanner", () ->
            new HandScannerItem(new Item.Properties()));

    public static void register(net.neoforged.bus.api.IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
    }
}
