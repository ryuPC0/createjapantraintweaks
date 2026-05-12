package io.github.ryuPC0.cjptt.mixin;

import com.simibubi.create.api.behaviour.interaction.ConductorBlockInteractionBehavior;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import io.github.ryuPC0.cjptt.mixininterface.ConductorBlockInteractionBehaviorMixinInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = ConductorBlockInteractionBehavior.class,remap = false)
public abstract class ConductorBlockInteractionBehaviorMixin implements ConductorBlockInteractionBehaviorMixinInterface {
    @Inject(method = "handlePlayerInteraction",at = @At("HEAD"), cancellable = true)
    void Cjptt$handlePlayerInteractionOverrideable(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity, CallbackInfoReturnable<Boolean> cir){
        Boolean flag = cjptt$overrideablehandlePlayerInteraction(player,activeHand,localPos,contraptionEntity);
        if(flag != null) cir.setReturnValue(flag);
    }
}
