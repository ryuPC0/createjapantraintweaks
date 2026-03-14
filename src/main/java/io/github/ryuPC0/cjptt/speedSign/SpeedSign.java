package io.github.ryuPC0.cjptt.speedSign;

import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.EdgePointType;
import com.simibubi.create.content.trains.graph.TrackEdge;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.signal.SignalPropagator;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SpeedSign extends SingleBlockEntityEdgePoint implements IBE<SpeedSignBlockEntity> {
    boolean front = true;
    double throttle = 1;
    private FilterItemStack filter;
    @Override
    public void blockEntityAdded(BlockEntity blockEntity, boolean front)
    {
        super.blockEntityAdded(blockEntity,front);
        this.front = front;
        FilteringBehaviour filteringBehaviour = BlockEntityBehaviour.get(blockEntity, FilteringBehaviour.TYPE);
        if (filteringBehaviour != null)
            setFilterAndNotify(blockEntity.getLevel(), filteringBehaviour.getFilter());
    }
    public void setFilterAndNotify(Level level, ItemStack filter) {
        this.filter = FilterItemStack.of(filter.copy());
        notifyTrains(level);
    }

    private void notifyTrains(Level level) {
        TrackGraph graph = Create.RAILWAYS.sided(level)
                .getGraph(level, edgeLocation.getFirst());
        if (graph == null)
            return;
        TrackEdge edge = graph.getConnection(edgeLocation.map(graph::locateNode));
        if (edge == null)
            return;
        SignalPropagator.notifyTrains(graph, edge);
    }
    //@Override
    public Class<SpeedSignBlockEntity> getBlockEntityClass() {
        return SpeedSignBlockEntity.class;
    }
    @Override
    public BlockEntityType<? extends SpeedSignBlockEntity> getBlockEntityType() {
        return Cjptt.SPEEDSIGN_BLOCKENTITY.get();
    }
    public FilterItemStack getFilter() {
        return filter;
    }
    public double Getthrottle()
    {
        return throttle;
    }
    @Override
    public void read(CompoundTag nbt, boolean migration, DimensionPalette dimensions)
    {
        super.read(nbt, migration, dimensions);
        throttle = nbt.getDouble("throttle");
        filter = FilterItemStack.of(nbt.getCompound("Filter"));
    }
    @Override
    public void write(CompoundTag nbt, DimensionPalette dimensions)
    {
        super.write(nbt,dimensions);
        nbt.putDouble("throttle",throttle);
        nbt.put("Filter", filter.serializeNBT());
    }
}
