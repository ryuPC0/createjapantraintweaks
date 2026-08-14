package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.api.contraption.transformable.TransformableBlockEntity;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.trains.track.TrackTargetingBehaviour;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.registry.CjpttEdgePointType;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSign;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector2i;
import org.joml.Vector3d;

import java.util.List;

public class AdvancedSpeedSignBlockEntity extends SmartBlockEntity implements TransformableBlockEntity {
    private TrackTargetingBehaviour<AdvancedSpeedSign> edgePoint;
    public boolean front;
    public AdvancedSpeedSignBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void transform(BlockEntity blockEntity, StructureTransform structureTransform) {
        edgePoint.transform(blockEntity,structureTransform);
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
        list.add(edgePoint = new TrackTargetingBehaviour<>(this, CjpttEdgePointType.ADVANCED_SPEEDSIGN));
    }
    public AdvancedSpeedSign getedgePoint(){
        return edgePoint.getEdgePoint();
    }

    @Override
    public void tick(){
        super.tick();
    }

    @Override
    protected void read(CompoundTag tag, boolean clientPacket) {
        super.read(tag, clientPacket);
        front = tag.getBoolean("front");
    }

    @Override
    protected void write(CompoundTag tag, boolean clientPacket) {
        super.write(tag, clientPacket);
        tag.putBoolean("front",front);
    }

    public Double Getdegree(int dir){
        return -(dir > 15 ? (dir - 16) * 90 : (dir * 22.5));
    }
    public Vector3d GetDrawOffSet(AdvancedSpeedSignBlockEntity be, double degree, boolean mode){
        double radian = Math.toRadians(-(degree+90));
        Vector3d pos = new Vector3d(0.5,0.5,0.5);
        Vector3d pos2 = new Vector3d(Math.cos(radian),0,Math.sin(radian)).mul(mode ? -7.0/16 + 0.005:1.0/16 + 0.005);
        return pos.add(pos2);
    }
}
