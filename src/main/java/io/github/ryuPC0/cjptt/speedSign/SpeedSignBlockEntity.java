package io.github.ryuPC0.cjptt.speedSign;

import java.util.List;
import java.util.Objects;


import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.Create;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import dev.engine_room.flywheel.lib.transform.TransformStack;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SpeedSignBlockEntity extends SmartBlockEntity implements TransformableBlockEntity, IHaveGoggleInformation
{
    public TrackTargetingBehaviour<SpeedSign> edgePoint;
    public ScrollValueBehaviour throttle;
    private FilteringBehaviour filtering;
    public SpeedSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        behaviours.add(edgePoint = new TrackTargetingBehaviour<>(this, Cjptt.SPEEDSIGN));
        behaviours.add(filtering = createFilter().withCallback(this::onFilterChanged));
        //*
        throttle = new ScrollValueBehaviour(Component.nullToEmpty(null)
        , this, new ValueBoxTransform() {
            @Override
            public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
                TransformStack.of(ms)
                        .rotateXDegrees(90);
            }

            @Override
            public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
                return new Vec3(0.5, 15.5 / 16d, 0.3);
            }
        });
        throttle.between(5,100);
        throttle.setValue(100);
        throttle.withCallback((x)-> Objects.requireNonNull(edgePoint.getEdgePoint()).throttle= throttle.getValue() * 0.01);
        behaviours.add(throttle);

         //*/
    }
    private void onFilterChanged(ItemStack newFilter) {
        if (level.isClientSide())
            return;
        SpeedSign speedSign = getSpeedSign();
        if(speedSign != null)
        {
            speedSign.setFilter(level,newFilter);
        }
    }
    public FilteringBehaviour createFilter() {
        return new FilteringBehaviour(this, new ValueBoxTransform() {

            @Override
            public void rotate(LevelAccessor level, BlockPos pos, BlockState state, PoseStack ms) {
                TransformStack.of(ms)
                        .rotateXDegrees(90);
            }

            @Override
            public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
                return new Vec3(0.5, 15.5 / 16d, 0.5);
            }

        });
    }
    @Nullable
    public SpeedSign getSpeedSign() {
        return edgePoint.getEdgePoint();
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
