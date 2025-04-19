// src\main\java\design\rrenode\stellarfactory\client\renderers\HandScannerRenderer.java
package design.rrenode.stellarfactory.client.renderers.Items.HandScanner;

import javax.annotation.Nullable;

import design.rrenode.stellarfactory.items.HandScannerItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import net.minecraft.client.renderer.RenderType;


public class HandScannerRenderer extends GeoItemRenderer<HandScannerItem> {
    public HandScannerRenderer() {
        super(new HandScannerModel());
    }

    @Override
    public RenderType getRenderType(HandScannerItem animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(texture); // ✅ supports alpha
    }
}
