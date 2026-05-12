package io.github.ryuPC0.cjptt.extended.traincontrolsblock;

import com.google.common.base.Objects;
import com.simibubi.create.AllItems;
import com.simibubi.create.Create;
import com.simibubi.create.api.behaviour.interaction.ConductorBlockInteractionBehavior;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.actors.trainControls.ControlsHandler;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import io.github.ryuPC0.cjptt.mixininterface.ConductorBlockInteractionBehaviorMixinInterface;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.UUID;

public class ExtendedControlsInteractionBehaviour extends ConductorBlockInteractionBehavior implements ConductorBlockInteractionBehaviorMixinInterface {

    @Override
    public boolean isValidConductor(BlockState blockState) {
        return true;
    }

    @Override
    public Boolean cjptt$overrideablehandlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos,
                                           AbstractContraptionEntity contraptionEntity) {
        ItemStack item = player.getItemInHand(activeHand);
        if (AllItems.WRENCH.isIn(item))
            return false;
        else if(AllItems.SCHEDULE.isIn(item)){
            if(contraptionEntity instanceof CarriageContraptionEntity carriageContraptionEntity){
                Train train = Create.RAILWAYS.trains.get(carriageContraptionEntity.trainId);
                if(train != null){
                    if(train.runtime.getSchedule() == null){
                        return null;
                    }
                }
            }
        }else if(item.isEmpty()){
            if(contraptionEntity instanceof CarriageContraptionEntity carriageContraptionEntity){
                Train train = Create.RAILWAYS.trains.get(carriageContraptionEntity.trainId);
                if(train != null){
                    if(train.runtime.getSchedule() != null){
                        return null;
                    }
                }
            }
        }

        UUID currentlyControlling = contraptionEntity.getControllingPlayer()
                .orElse(null);

        if (currentlyControlling != null) {
            contraptionEntity.stopControlling(localPos);
            //将来的に設定を開くよう記述？
            if (Objects.equal(currentlyControlling, player.getUUID()))
                return true;
        }

        if (!contraptionEntity.startControlling(localPos, player))
            return false;

        contraptionEntity.setControllingPlayer(player.getUUID());
        if (player.level().isClientSide)
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                    () -> () -> ControlsHandler.startControlling(contraptionEntity, localPos));
        return true;
    }
}
