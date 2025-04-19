package design.rrenode.stellarfactory.VitalisProtection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;

public class ZoneCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("zone")
            .then(Commands.literal("enable")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> setZoneEnabled(ctx.getSource(), StringArgumentType.getString(ctx, "id"), true))))
            .then(Commands.literal("disable")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> setZoneEnabled(ctx.getSource(), StringArgumentType.getString(ctx, "id"), false))))
            .then(Commands.literal("remove")
                .then(Commands.argument("id", StringArgumentType.word())
                    .executes(ctx -> removeZone(ctx.getSource(), StringArgumentType.getString(ctx, "id")))))
            .then(literal("add")
                    .then(argument("id", StringArgumentType.word())
                        .then(argument("pos1", BlockPosArgument.blockPos())
                            .then(argument("pos2", BlockPosArgument.blockPos())
                                .executes(ctx -> addZone(ctx))
                            )
                        )
                    )
                )
        );
    }

    private static int setZoneEnabled(CommandSourceStack source, String id, boolean enabled) {
        RestrictedZone zone = BuildProtectionHandler.getZoneById(id);
        if (zone == null) {
            source.sendFailure(Component.literal("Zone '" + id + "' not found."));
            return 0;
        }

        BuildProtectionHandler.setZoneEnabled(id, enabled);
        String status = enabled ? "enabled" : "disabled";
        source.sendSuccess(() -> Component.literal("Zone '" + id + "' " + status + "."), true);
        return 1;
    }

    private static int removeZone(CommandSourceStack source, String id) {
        boolean removed = BuildProtectionHandler.removeZoneById(id);
        if (removed) {
            source.sendSuccess(() -> Component.literal("Zone '" + id + "' removed."), true);
            return 1;
        } else {
            source.sendFailure(Component.literal("Zone with ID '" + id + "' not found."));
            return 0;
        }
    }

    private static int addZone(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String id = StringArgumentType.getString(ctx, "id");
        BlockPos pos1 = BlockPosArgument.getLoadedBlockPos(ctx, "pos1");
        BlockPos pos2 = BlockPosArgument.getLoadedBlockPos(ctx, "pos2");
        String dimension = ctx.getSource().getLevel().dimension().location().toString();

        boolean success = BuildProtectionHandler.addZone(id, pos1, pos2, dimension);
        if (success) {
            ctx.getSource().sendSuccess(() -> Component.literal("Zone '" + id + "' added."), true);
            return 1;
        } else {
            ctx.getSource().sendFailure(Component.literal("Zone '" + id + "' already exists."));
            return 0;
        }
    }
}

