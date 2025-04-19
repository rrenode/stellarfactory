// src/main/java/design/rrenode/stellarfactory/client/renderers/AtmosphericCondenserRenderer.java
package design.rrenode.stellarfactory.client.renderers;

import design.rrenode.stellarfactory.entities.AtmosphericCondenserEntity;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AtmosphericCondenserRenderer extends GeoBlockRenderer<AtmosphericCondenserEntity> {
    public AtmosphericCondenserRenderer() {
        super(new AtmosphericCondenserModel());
    }
}