package io.github.ryuPC0.cjptt.schedule;

import com.simibubi.create.content.trains.schedule.condition.ScheduleWaitCondition;
import com.simibubi.create.content.trains.schedule.destination.ScheduleInstruction;
import io.github.ryuPC0.cjptt.Cjptt;
import net.createmod.catnip.data.Pair;

import java.util.function.Supplier;

import static com.simibubi.create.content.trains.schedule.Schedule.CONDITION_TYPES;
import static com.simibubi.create.content.trains.schedule.Schedule.INSTRUCTION_TYPES;

public class CjpttSchedule {
    static
    {
        registerCondition("Reserved",ReservedSignalBlockCondition::new);
    }
    private static void registerInstruction(String name, Supplier<? extends ScheduleInstruction> factory) {
        INSTRUCTION_TYPES.add(Pair.of(Cjptt.asResource(name), factory));
    }

    private static void registerCondition(String name, Supplier<? extends ScheduleWaitCondition> factory) {
        CONDITION_TYPES.add(Pair.of(Cjptt.asResource(name), factory));
    }
    public static void register() {}
}
