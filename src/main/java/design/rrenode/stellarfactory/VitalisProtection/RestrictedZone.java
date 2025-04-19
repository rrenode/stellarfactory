package design.rrenode.stellarfactory.VitalisProtection;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public record RestrictedZone(
    String id,
    BlockPos min,
    BlockPos max,
    String dimensionId,
    boolean enabled
) {

    public boolean contains(BlockPos pos, LevelAccessor level) {
        if (!enabled) return false;
        if (!(level instanceof net.minecraft.world.level.Level realLevel)) return false;
        if (!realLevel.dimension().location().toString().equals(dimensionId)) return false;

        return pos.getX() >= min.getX() && pos.getX() <= max.getX()
            && pos.getY() >= min.getY() && pos.getY() <= max.getY()
            && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    // Use a factory method to normalize input
    public static RestrictedZone of(String id, BlockPos a, BlockPos b, String dimensionId) {
        return of(id, a, b, dimensionId, true);
    }

    public static RestrictedZone of(String id, BlockPos a, BlockPos b, String dimensionId, boolean enabled) {
        BlockPos min = new BlockPos(
            Math.min(a.getX(), b.getX()),
            Math.min(a.getY(), b.getY()),
            Math.min(a.getZ(), b.getZ())
        );
        BlockPos max = new BlockPos(
            Math.max(a.getX(), b.getX()),
            Math.max(a.getY(), b.getY()),
            Math.max(a.getZ(), b.getZ())
        );
        return new RestrictedZone(id, min, max, dimensionId, enabled);
    }
}