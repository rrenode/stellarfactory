package design.rrenode.stellarfactory;

import net.neoforged.neoforge.energy.IEnergyStorage;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.energy.EnergyStorage;

public class ModCapabilities {
    public static final BlockCapability<IEnergyStorage, Direction> ENERGY_STORAGE = BlockCapability.createSided(
        StellarFactory.id("energy_storage"),
        IEnergyStorage.class
    );
}
