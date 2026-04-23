package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.trains.entity.Carriage;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TrainPacket;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.mixin.accessor.AbstractContraptionEntityAccessor;
import io.github.ryuPC0.cjptt.mixin.accessor.CarriageAccessor;
import io.github.ryuPC0.cjptt.mixininterface.CarriageContraptionMixinInterface;
import io.github.ryuPC0.cjptt.mixininterface.TrainMixinInterface;
import net.minecraft.network.FriendlyByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("InjectIntoConstructor")
@Mixin(value = TrainPacket.class,remap = false)
public class TrainPacketMixin {
    @Shadow
    Train train;
    @Inject(method = "<init>(Lnet/minecraft/network/FriendlyByteBuf;)V",at = @At(value = "TAIL"))
    void Cjptt$initextseatcheck(FriendlyByteBuf buffer, CallbackInfo ci, @Local(name = "carriages") List<Carriage> carriages){
        boolean flag = false;
        for(Carriage carriage : train.carriages){
            if(flag)break;
            for(Carriage.DimensionalCarriageEntity dimensionalCarriageEntity :((CarriageAccessor)carriage).getentitles().values()){
                Cjptt.LOGGER.info("{}",((CarriageContraptionMixinInterface)dimensionalCarriageEntity.entity.get()).cjptt$getextendedconductorSeats());
                if(!((CarriageContraptionMixinInterface)dimensionalCarriageEntity.entity.get()).cjptt$getextendedconductorSeats().isEmpty()) {
                    flag = true;
                    break;
                }
            }
        }
        ((TrainMixinInterface)train).cjptt$setisextendedtrain(flag);
    }
}
