package design.rrenode.stellarfactory.VitalisProtection;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.util.Set;
import java.util.HashSet;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
/* Below is imports for the debug information
import net.minecraft.world.level.Explosion;
*/

public class BuildProtectionHandler {
    private static final Set<ResourceLocation> ALLOWED_BLOCK_INTERACTIONS = new HashSet<>();

    static {
        // Add your allowed blocks here (use modid:blockid)
        ALLOWED_BLOCK_INTERACTIONS.add(ResourceLocation.fromNamespaceAndPath("minecraft", "crafting_table"));
    }

    private static final Map<String, RestrictedZone> ZONES = new HashMap<>();

    static {
        ZONES.put("vitalis_core_buildzone",
            RestrictedZone.of("vitalis_core_buildzone",
                new BlockPos(-75, 64, 210),
                new BlockPos(-80, 64, 208),
                "stellarworld:vitalis"
            )
        );
    }

    // Zone Manipulation and Information
    public static RestrictedZone getZoneById(String id) {
        return ZONES.get(id);
    }

    public static boolean addZone(String id, BlockPos a, BlockPos b, String dimensionId) {
        if (ZONES.containsKey(id)) return false;
        ZONES.put(id, RestrictedZone.of(id, a, b, dimensionId));
        return true;
    }    

    public static boolean removeZoneById(String id) {
        return ZONES.remove(id) != null;
    }

    public static void setZoneEnabled(String id, boolean enabled) {
        RestrictedZone zone = ZONES.get(id);
        if (zone != null) {
            ZONES.put(id, new RestrictedZone(id, zone.min(), zone.max(), zone.dimensionId(), enabled));
        }
    }

    // Flags
    public static boolean isInRestrictedZone(BlockPos pos, LevelAccessor level) {
        for (RestrictedZone zone : ZONES.values()) {
            if (zone.contains(pos, level)) return true;
        }
        return false;
    }
    
    public static boolean playerHasPermission(Entity entity) {
        return false; // Replace with real logic later
    }

    public static boolean isAllowedInteraction(BlockState state) {
        ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(state.getBlock());
        return ALLOWED_BLOCK_INTERACTIONS.contains(blockId);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        System.out.println("Break event triggered at: " + event.getPos());
        if (isInRestrictedZone(event.getPos(), event.getLevel()) && !playerHasPermission(event.getPlayer())) {
            System.out.println("Inside restricted zone!");
            event.setCanceled(true);
            if (event.getPlayer() instanceof ServerPlayer player) {
                player.sendSystemMessage(Component.literal("You can't break blocks here yet!"));
            }
        }
    }

    @SubscribeEvent
    public static void onBlockPlace(BlockEvent.EntityPlaceEvent event) {
        if (isInRestrictedZone(event.getPos(), event.getLevel()) && !playerHasPermission(event.getEntity())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockPos clicked = event.getPos();
        BlockPos placed = clicked.relative(event.getFace());
        InteractionHand hand = event.getHand();
        ItemStack item = player.getItemInHand(hand);
        Level level = event.getLevel();
        BlockState clickedBlock = level.getBlockState(clicked);

        if ((isInRestrictedZone(clicked, level) || isInRestrictedZone(placed, level)) &&
            !playerHasPermission(event.getEntity())) {

                if ((!player.isShiftKeyDown() && !item.isEmpty()) || (item.isEmpty())) {
                    // If player is not holding shift key with and item in their hand or their hand is empty

                    // Check if clickedBlock is a possible exception block
                    if (isAllowedInteraction(clickedBlock)) {
                        return; // Let the interaction go through
                    }
                }

                event.setCanceled(true);
                if (!event.getEntity().isCreative()) {
                    event.getEntity().sendSystemMessage(Component.literal("You can't build here yet!"));
                }
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        /*Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack item = player.getItemInHand(hand);

        System.out.println("== RightClickItem ==");
        System.out.println("Item used in air: " + item);
        System.out.println("Hand: " + hand);*/
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        /*Player player = event.getEntity();
        BlockPos pos = event.getPos();

        System.out.println("== LeftClickBlock ==");
        System.out.println("Block at: " + pos);
        System.out.println("Sneaking: " + player.isShiftKeyDown());*/
        if (isInRestrictedZone(event.getPos().relative(event.getFace()), event.getLevel()) &&
            !playerHasPermission(event.getEntity())) {
                
                event.setCanceled(true);
                if (!event.getEntity().isCreative()) {
                    event.getEntity().sendSystemMessage(Component.literal("You can't destroy here!"));
                }
        }
    }

    @SubscribeEvent
    public static void onExplosionDetonate(ExplosionEvent.Detonate event) {
        if (event.getLevel().isClientSide()) return;
    
        List<BlockPos> affectedBlocks = event.getAffectedBlocks();
    
        // Remove restricted blocks from the explosion's list
        affectedBlocks.removeIf(pos -> isInRestrictedZone(pos, event.getLevel()));
    }

    // Likely to not be used
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        /*Player player = event.getEntity();
        Entity target = event.getTarget();
        InteractionHand hand = event.getHand();

        System.out.println("== EntityInteract ==");
        System.out.println("Target entity: " + target.getType().getDescriptionId());
        System.out.println("Item in Hand: " + player.getItemInHand(hand));*/
    }

    // Likely to not be used
    @SubscribeEvent
    public static void onEntityInteractSpecific(PlayerInteractEvent.EntityInteractSpecific event) {
        /*Player player = event.getEntity();
        Entity target = event.getTarget();
        InteractionHand hand = event.getHand();

        System.out.println("== EntityInteractSpecific ==");
        System.out.println("Target entity (specific part): " + target.getType().getDescriptionId());
        System.out.println("Item in Hand: " + player.getItemInHand(hand));*/
    }
}