// src\main\java\design\rrenode\stellarfactory\client\renderers\AtmosphericCondenserModel.java
package design.rrenode.stellarfactory.client.renderers.Blocks.AtmosphericCondenser;

import design.rrenode.stellarfactory.StellarFactory;
import design.rrenode.stellarfactory.entities.AtmosphericCondenserEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class AtmosphericCondenserModel extends DefaultedBlockGeoModel<AtmosphericCondenserEntity> {
    public AtmosphericCondenserModel() {
        super(StellarFactory.id("models/atmospheric_condenser"));
    }
}
