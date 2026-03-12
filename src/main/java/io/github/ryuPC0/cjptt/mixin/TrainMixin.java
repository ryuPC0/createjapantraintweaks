package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.entity.TravellingPoint;
import com.simibubi.create.content.trains.graph.TrackNode;
import com.simibubi.create.content.trains.signal.TrackEdgePoint;
import io.github.ryuPC0.cjptt.speedSign.SpeedSign;
import net.createmod.catnip.data.Couple;
import net.createmod.catnip.data.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = Train.class,remap = false)
public class TrainMixin {
    @Shadow public double throttle;

    @Inject(method = "lambda$frontSignalListener$6",at = @At(value = "INVOKE", target = "Lnet/createmod/catnip/data/Pair;getFirst()Ljava/lang/Object;",ordinal = 3),cancellable = true)
    void Cjptt$frontsignallistenerThrottle(CallbackInfoReturnable<TravellingPoint.IEdgePointListener> cir, @Local Pair<TrackEdgePoint, Couple<TrackNode>> couple)
    {
        Object patt13732$temp = couple.getFirst();
        if (patt13732$temp instanceof SpeedSign speedSign) {
            this.throttle = speedSign.Getthrottle();
            cir.cancel();
        }
    }
}
