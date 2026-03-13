package io.github.ryuPC0.cjptt.speedSign;

import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import com.simibubi.create.foundation.block.IBE;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpeedSign extends SingleBlockEntityEdgePoint implements IBE<SpeedSignBlockEntity> {
    boolean front = true;
    double throttle = 1;
    @Override
    public void blockEntityAdded(BlockEntity blockEntity, boolean front)
    {
        super.blockEntityAdded(blockEntity,front);
        this.front = front;
    }
    //@Override
    public Class<SpeedSignBlockEntity> getBlockEntityClass() {
        return SpeedSignBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends SpeedSignBlockEntity> getBlockEntityType() {
        return Cjptt.SPEEDSIGN_BLOCKENTITY.get();
    }
    public double Getthrottle()
    {
        return throttle;
    }
}
