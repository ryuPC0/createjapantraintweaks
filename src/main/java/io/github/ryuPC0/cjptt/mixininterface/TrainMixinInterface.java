package io.github.ryuPC0.cjptt.mixininterface;

import net.createmod.catnip.data.Couple;

import java.util.List;

public interface TrainMixinInterface {
    boolean cjptt$getisextendedtrain();
    void cjptt$setisextendedtrain(boolean value);
    List<Couple<Double>> cjptt$getspeedlimit();
}
