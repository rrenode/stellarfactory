// src\main\java\design\rrenode\stellarfactory\StellarFactory.java
package design.rrenode.stellarfactory;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import design.rrenode.stellarfactory.VitalisProtection.BuildProtectionHandler;
import design.rrenode.stellarfactory.VitalisProtection.ZoneCommands;
import design.rrenode.stellarfactory.client.ClientInit;
import design.rrenode.stellarfactory.client.renderers.Blocks.AtmosphericCondenser.AtmosphericCondenserRenderer;
import design.rrenode.stellarfactory.entities.AtmosphericCondenserEntity;
import design.rrenode.stellarfactory.registry.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(StellarFactory.MODID)
public class StellarFactory
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "stellarfactory";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public StellarFactory(IEventBus modEventBus, ModContainer modContainer) {
        // 1. Register your mod's content (blocks, items, block entities, etc.)
        design.rrenode.stellarfactory.registry.ModRegistry.register(modEventBus);
    
        // 2. Register lifecycle listeners
        modEventBus.addListener(this::commonSetup);
        
        // 3. Register this mod class for global event bus (if using @SubscribeEvent methods)
        NeoForge.EVENT_BUS.register(this);
        NeoForge.EVENT_BUS.register(BuildProtectionHandler.class);
        modEventBus.addListener(ClientInit::onClientSetup);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event) {
        ZoneCommands.register(event.getDispatcher());
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
            BlockEntityRenderers.register(
                (BlockEntityType<AtmosphericCondenserEntity>) ModRegistry.ATMOSPHERIC_CONDENSER_ENTITY.get(),
                ctx -> new AtmosphericCondenserRenderer()
            );
        }
    }
}
