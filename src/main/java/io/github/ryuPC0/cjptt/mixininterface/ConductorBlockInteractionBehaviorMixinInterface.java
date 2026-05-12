package io.github.ryuPC0.cjptt.mixininterface;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public interface ConductorBlockInteractionBehaviorMixinInterface {
    default Boolean cjptt$overrideablehandlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos,
                                                                     AbstractContraptionEntity contraptionEntity){
        return null;
    }
}
