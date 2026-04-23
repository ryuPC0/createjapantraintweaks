package io.github.ryuPC0.cjptt.mixin.accessor;

import com.simibubi.create.content.trains.entity.Carriage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(value = Carriage.class,remap = false)
public interface CarriageAccessor {
    @Accessor("entities")
    Map<ResourceKey<Level>, Carriage.DimensionalCarriageEntity> getentitles();
}
