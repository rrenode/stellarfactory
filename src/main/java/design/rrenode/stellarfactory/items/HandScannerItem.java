// src\main\java\design\rrenode\stellarfactory\items\HandScannerItem.java
package design.rrenode.stellarfactory.items;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import design.rrenode.stellarfactory.client.renderers.HandScannerRenderer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.Item;
import java.util.function.Consumer;

public final class HandScannerItem extends Item implements GeoItem {

    public HandScannerItem(Properties properties) {
        super(properties);

		// Register our item as server-side handled.
		// This enables both animation data syncing and server-side animation triggering
		SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        // No animation controllers needed
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private HandScannerRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new HandScannerRenderer();
                return this.renderer;
            }
        });
    }
}