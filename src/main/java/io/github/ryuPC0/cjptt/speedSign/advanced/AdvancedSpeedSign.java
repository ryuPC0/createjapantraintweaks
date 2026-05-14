package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import net.createmod.catnip.data.Couple;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdvancedSpeedSign extends SingleBlockEntityEdgePoint {
    public Map<BlockPos,Integer> blockEntities;
    public Map<Integer,Double> speed;
    public boolean front = true;
    public AdvancedSpeedSign(){
        blockEntities = new HashMap<>();
        speed = new HashMap<>();
    }
    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void invalidate(LevelAccessor level) {
        blockEntities.forEach((s,i) -> invalidateAt(level, s));
    }

    @Override
    public boolean canCoexistWith(EdgePointType<?> otherType, boolean front) {
        return otherType == getType() && this.front == front;
    }

    @Override
    public void blockEntityAdded(BlockEntity blockEntity, boolean front) {
        if (blockEntities.isEmpty())
            ;
        blockEntities.put(blockEntity.getBlockPos(), 8);
    }
    @Override
    public void blockEntityRemoved(BlockPos blockEntityPos, boolean front) {
        blockEntities.remove(blockEntityPos);
        if (blockEntities.isEmpty())
            removeFromAllGraphs();
    }
}
