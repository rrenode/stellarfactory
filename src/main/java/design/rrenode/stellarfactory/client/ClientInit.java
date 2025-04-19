package design.rrenode.stellarfactory.client;

import design.rrenode.stellarfactory.registry.ModRegistry;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;


public class ClientInit {
    public static void onClientSetup(FMLClientSetupEvent event) {
        // You can put client setup logic here (if needed)
        System.out.println("Client Logic for StellarFactory.");
        event.enqueueWork(() -> {
            ModRegistry.HAND_SCANNER.get(); // Triggers the item to be initialized
        });
    }
}

