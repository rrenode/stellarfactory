// src\main\java\design\rrenode\stellarfactory\blocks\AtmosphericCondenserBlock.java
package design.rrenode.stellarfactory.blocks;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import design.rrenode.stellarfactory.entities.AtmosphericCondenserEntity;;

public class AtmosphericCondenserBlock extends Block implements EntityBlock {
    public AtmosphericCondenserBlock(Properties props) {
        super(props);
    }

    @Override
    @Nonnull
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new AtmosphericCondenserEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
    }
