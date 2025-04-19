// src\main\java\design\rrenode\stellarfactory\client\renderers\HandScannerModel.java
package design.rrenode.stellarfactory.client.renderers.Items.HandScanner;

import design.rrenode.stellarfactory.StellarFactory;
import design.rrenode.stellarfactory.items.HandScannerItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public class HandScannerModel extends DefaultedItemGeoModel<HandScannerItem> {
    public HandScannerModel() {
        super(StellarFactory.id("models/hand_scanner"));
    }
}