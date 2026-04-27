package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.station.StationBlockEntity;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.extended.train.ExtendedTrainPacket;
import io.github.ryuPC0.cjptt.mixin.accessor.CarriageAccessor;
import io.github.ryuPC0.cjptt.mixininterface.CarriageContraptionMixinInterface;
import io.github.ryuPC0.cjptt.mixininterface.TrainMixinInterface;
import io.github.ryuPC0.cjptt.registry.CjpttPackets;
import net.minecraftforge.network.PacketDistributor;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.UUID;

@Mixin(value = StationBlockEntity.class,remap = false)
public class StationBlockEntityMixin {
    @Inject(method = "assemble",at = @At(value = "INVOKE", target = "Ljava/util/List;size()I",ordinal = 10))
    void Cjptt$assembleextseatcheck(UUID playerUUID, CallbackInfo ci, @Local(ordinal = 0) Train train, @Local(name = "contraptions")List<CarriageContraption> contraptions){
        contraptions.forEach((c -> {
            Cjptt.LOGGER.info("mixin,foreachok");
            if(!((CarriageContraptionMixinInterface)c).cjptt$getextendedconductorSeats().isEmpty()) {
                ((TrainMixinInterface) train).cjptt$setisextendedtrain(true);
                Cjptt.LOGGER.info("exttrain");
            }
        }));
    }
    @Inject(method = "assemble",at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/simple/SimpleChannel;send(Lnet/minecraftforge/network/PacketDistributor$PacketTarget;Ljava/lang/Object;)V",shift = At.Shift.AFTER))
    void Cjptt$assembleadditionalpacket(UUID playerUUID, CallbackInfo ci,@Local(ordinal = 0) Train train){
        CjpttPackets.getChannel().send(PacketDistributor.ALL.noArg(),new ExtendedTrainPacket(train));
    }
}
