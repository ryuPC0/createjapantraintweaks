package io.github.ryuPC0.cjptt.speedSign;

import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import com.simibubi.create.foundation.block.IBE;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpeedSign extends SingleBlockEntityEdgePoint implements IBE<SpeedSignBlockEntity> {
    //@Override
    public Class<SpeedSignBlockEntity> getBlockEntityClass() {
        return SpeedSignBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends SpeedSignBlockEntity> getBlockEntityType() {
        return Cjptt.SPEEDSIGN_BLOCKENTITY.get();
    }
}
