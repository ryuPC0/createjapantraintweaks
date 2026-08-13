package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.content.trains.graph.*;
import com.simibubi.create.content.trains.signal.SingleBlockEntityEdgePoint;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.registry.CjpttPackets;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.LimitRuleType;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.Limitrem;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.nbt.NBTHelper;
import net.createmod.catnip.nbt.NBTProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.PacketDistributor;

import java.util.*;

@SuppressWarnings({"DataFlowIssue", "ConstantValue"})
public class AdvancedSpeedSign extends TrackEdgePoint {
    public Couple<Set<BlockPos>> blockEntities;
    public Couple<AbstractLimitRule> speed;
    public AdvancedSpeedSign(){
        blockEntities = Couple.create(HashSet::new);
        speed = Couple.create(null,null);
    }
    @Override
    public boolean canMerge() {
        return true;
    }

    @Override
    public void invalidate(LevelAccessor level) {
        blockEntities.forEach((s) -> s.forEach((b) -> invalidateAt(level, b)));
    }

    @Override
    public boolean canCoexistWith(EdgePointType<?> otherType, boolean front) {
        return otherType == getType();
    }

    @Override
    public void blockEntityAdded(BlockEntity blockEntity, boolean front) {
        if (blockEntities.get(front).isEmpty()) {
            speed.set(front, LimitRuleType.LIMITREM.create());
            if(!blockEntities.both(Set::isEmpty)) {
                if(!blockEntity.getLevel().isClientSide){
                    CjpttPackets.getChannel().send(PacketDistributor.ALL.noArg(), new AdvancedSpeedSignedgePacket(this,front,blockEntity.getLevel()));
                }
            }
        }
        blockEntities.get(front).add(blockEntity.getBlockPos());
        if(blockEntity instanceof AdvancedSpeedSignBlockEntity ass){
            ass.front = front;
        }
    }
    @Override
    public void blockEntityRemoved(BlockPos blockEntityPos, boolean front) {
        blockEntities.get(front).remove(blockEntityPos);
        if(blockEntities.get(front).isEmpty())
            speed.set(front,null);
        if (blockEntities.getFirst().isEmpty() && blockEntities.getSecond().isEmpty())
            removeFromAllGraphs();
    }

    @Override
    public void read(CompoundTag nbt, boolean migration, DimensionPalette dimensions) {
        super.read(nbt, migration, dimensions);
        if(nbt.contains("frontrule"))
            speed.setFirst(LimitRuleType.read(nbt.getCompound("frontrule")));
        if(nbt.contains("backrule"))
            speed.setSecond(LimitRuleType.read(nbt.getCompound("backrule")));
    }

    @Override
    public void read(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        super.read(buffer, dimensions);
        if(buffer.readBoolean())
            speed.setFirst(LimitRuleType.read(buffer));
        if(buffer.readBoolean())
            speed.setSecond(LimitRuleType.read(buffer));
    }

    @Override
    public void write(CompoundTag nbt, DimensionPalette dimensions) {
        super.write(nbt, dimensions);
        if(speed.getFirst() != null)
            nbt.put("frontrule",speed.getFirst().write());
        if(speed.getSecond() != null)
            nbt.put("backrule",speed.getSecond().write());
    }

    @Override
    public void write(FriendlyByteBuf buffer, DimensionPalette dimensions) {
        super.write(buffer, dimensions);
        if(speed.getFirst() != null) {
            buffer.writeBoolean(true);
            speed.getFirst().write(buffer);
        }
        else{
            buffer.writeBoolean(false);
        }

        if(speed.getSecond() != null) {
            buffer.writeBoolean(true);
            speed.getSecond().write(buffer);
        }
        else {
            buffer.writeBoolean(false);
        }
    }
}
