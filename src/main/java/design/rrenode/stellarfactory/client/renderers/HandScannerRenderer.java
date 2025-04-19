// src\main\java\design\rrenode\stellarfactory\client\renderers\HandScannerRenderer.java
package design.rrenode.stellarfactory.client.renderers;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import design.rrenode.stellarfactory.items.HandScannerItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;


public class HandScannerRenderer extends GeoItemRenderer<HandScannerItem> {
    private static final Logger LOGGER = LogUtils.getLogger();

    public HandScannerRenderer() {
        super(new HandScannerModel());
        LOGGER.info("HandScannerRenderer: attached"); // debug
    }
}
