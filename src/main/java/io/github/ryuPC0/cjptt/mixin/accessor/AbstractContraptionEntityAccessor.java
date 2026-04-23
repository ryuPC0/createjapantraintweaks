package io.github.ryuPC0.cjptt.mixin.accessor;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = AbstractContraptionEntity.class,remap = false)
public interface AbstractContraptionEntityAccessor {
    @Accessor("contraption")
    Contraption getcontraption();
}
