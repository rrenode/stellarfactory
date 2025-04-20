// src\main\java\design\rrenode\stellarfactory\items\HandScannerItem.java
package design.rrenode.stellarfactory.items;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager.ControllerRegistrar;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.network.chat.Component;

import design.rrenode.stellarfactory.client.renderers.Items.HandScanner.HandScannerRenderer;

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

    
    @Override
    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.BOW; // or BOW, or NONE depending on your feel
    }

    @Override
    public @Nonnull InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player, @Nonnull InteractionHand hand) {
        //System.out.println("Started using item");
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(player.getItemInHand(hand));
    }

    private final Map<UUID, Object> lastScanTarget = new HashMap<>();


    //BUG: doesn't scan if you hold right click while changing target and then stop
    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level,@Nonnull Entity entity, int slot, boolean selected) {
        boolean SCOPED_DEBUG = false;
        if (!level.isClientSide && entity instanceof Player player && player.isUsingItem() && player.getUseItem() == stack) {
            
            UUID id = player.getUUID();

            // Determine current target (entity or block)
            Object currentTarget;
            EntityHitResult entityHit = getEntityHit(level, player, 5.0D);
            HitResult blockHit = player.pick(5.0D, 0.0F, true);
            if (entityHit != null) {
                currentTarget = entityHit.getEntity().getId(); // entity id is unique per world
            } else {
                if (blockHit.getType() == HitResult.Type.BLOCK) {
                    currentTarget = ((BlockHitResult) blockHit).getBlockPos();
                } else {
                    currentTarget = "MISS";
                }
            }

            // Compare to previous
            Object previousTarget = lastScanTarget.get(id);
            if (!currentTarget.equals(previousTarget)) {
                // Target changed → restart
                lastScanTarget.put(id, currentTarget);
                player.startUsingItem(player.getUsedItemHand());
                return;
            }

            int useTime = getUseDuration(stack, player) - player.getUseItemRemainingTicks();

            if (useTime % 20 == 0) {
                if (SCOPED_DEBUG) {
                    System.out.println("Server: Holding for " + (useTime / 20) + "s...");
                }
            }
            
            if (useTime == 80) {
                String scan_result = "";
            
                if (entityHit != null) {
                    Entity target = entityHit.getEntity();
                    scan_result = "Scanned Entity: " + target.getName().getString();
                } else {
                    switch (blockHit.getType()) {
                        case BLOCK -> {
                            BlockHitResult bhr = (BlockHitResult) blockHit;
                            BlockPos pos = bhr.getBlockPos();
                            BlockState state = level.getBlockState(pos);
            
                            if (!state.getFluidState().isEmpty()) {
                                scan_result = "Scanned Fluid: " + state.getFluidState().getType().toString();
                            } else {
                                scan_result = "Scanned Block: " + state.getBlock().getName().getString();
                            }
                        }
            
                        case MISS -> {
                            scan_result = "Scan found nothing.";
                        }
            
                        case ENTITY -> {
                            // Shouldn’t happen, since entity scanning is handled separately
                        }
            
                        default -> {
                            scan_result = "Unhandled scan type: " + blockHit.getType();
                        }
                    }
                }
            
                player.sendSystemMessage(Component.literal(scan_result));
                lastScanTarget.remove(id); // Clear scan state after scan
            }
        }
    }

    @Nullable
    private EntityHitResult getEntityHit(Level level, Player player, double range) {
        Vec3 eyePos = player.getEyePosition();
        Vec3 lookVec = player.getViewVector(1.0F); // partialTicks = 1.0F
        Vec3 reachVec = eyePos.add(lookVec.scale(range));

        AABB aabb = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0D);
        return ProjectileUtil.getEntityHitResult(level, player, eyePos, reachVec, aabb,
                e -> !e.isSpectator() && e.isPickable() && e != player);
    }

    @Override
    public void releaseUsing(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity, int timeLeft) {
        if (!level.isClientSide && entity instanceof Player player) {
            lastScanTarget.remove(player.getUUID());
        }
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack, @Nonnull LivingEntity entity) {
        return 72000;
    }
}