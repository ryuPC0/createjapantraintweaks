package io.github.ryuPC0.cjptt.extended.traincontrolsblock;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import net.minecraft.core.BlockPos;

import java.lang.ref.WeakReference;

public class ExtendedControlsHandler {
    public static WeakReference<AbstractContraptionEntity> entityref = new WeakReference(null);
    public static BlockPos controlsPos;
    public static void StartConductor(AbstractContraptionEntity entity, BlockPos controllerLocalPos){
        entityref = new WeakReference<AbstractContraptionEntity>(entity);
        controlsPos = controllerLocalPos;
    }
    public static void EndConductor(){
        entityref = new WeakReference(null);
        controlsPos = null;
    }
}
