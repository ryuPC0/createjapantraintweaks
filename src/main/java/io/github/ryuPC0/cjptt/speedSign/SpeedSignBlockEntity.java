package io.github.ryuPC0.cjptt.speedSign;

import java.util.List;


import com.simibubi.create.content.trains.graph.EdgePointType;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.simibubi.create.api.contraption.transformable.TransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.api.equipment.goggles.IHaveGoggleInformation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class SpeedSignBlockEntity extends SmartBlockEntity implements TransformableBlockEntity, IHaveGoggleInformation
{
    public TrackTargetingBehaviour<SpeedSign> edgePoint;

    public SpeedSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(edgePoint = new TrackTargetingBehaviour<>(this, Cjptt.SPEEDSIGN));
    }
    @Override
    public void transform(BlockEntity be, StructureTransform transform) {
        edgePoint.transform(be, transform);
    }
    @Override
    protected AABB createRenderBoundingBox() {
        return new AABB(worldPosition, edgePoint.getGlobalPosition()).inflate(2);
    }
}
