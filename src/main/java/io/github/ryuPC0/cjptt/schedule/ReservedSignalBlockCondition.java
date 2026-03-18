package io.github.ryuPC0.cjptt.schedule;

import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.condition.ScheduleWaitCondition;
import io.github.ryuPC0.cjptt.Cjptt;
import net.createmod.catnip.data.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ReservedSignalBlockCondition extends ScheduleWaitCondition {
    @Override
    public boolean tickCompletion(Level level, Train train, CompoundTag context) {

        return false;
    }

    @Override
    public MutableComponent getWaitingStatus(Level level, Train train, CompoundTag compoundTag) {
        return Component.translatable("cjptt.schedule.condition.loaded.reserved");
    }

    @Override
    public Pair<ItemStack, Component> getSummary() {
        return Pair.of(ItemStack.EMPTY, Component.translatable("cjptt.schedule.condition.reserved"));
    }

    @Override
    public ResourceLocation getId() {
        return Cjptt.asResource("reserved");
    }
}
