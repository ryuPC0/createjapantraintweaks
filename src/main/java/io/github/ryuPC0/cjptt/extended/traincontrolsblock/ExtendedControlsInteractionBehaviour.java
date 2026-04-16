package io.github.ryuPC0.cjptt.extended.traincontrolsblock;

import com.simibubi.create.api.behaviour.interaction.ConductorBlockInteractionBehavior;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class ExtendedControlsInteractionBehaviour extends ConductorBlockInteractionBehavior {

    @Override
    public boolean isValidConductor(BlockState blockState) {
        return true;
    }
}
