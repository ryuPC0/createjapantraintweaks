package io.github.ryuPC0.cjptt.registry;

import com.simibubi.create.content.trains.graph.EdgePointType;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSign;
import io.github.ryuPC0.cjptt.speedSign.simple.SpeedSign;
import net.minecraft.resources.ResourceLocation;

import static io.github.ryuPC0.cjptt.Cjptt.MODID;

@SuppressWarnings("removal")
public class CjpttEdgePointType
{

    public static final EdgePointType<SpeedSign> SPEEDSIGN = EdgePointType.register(new ResourceLocation(MODID,"speedsign"),SpeedSign::new);
    public static final EdgePointType<AdvancedSpeedSign> ADVANCED_SPEEDSIGN = EdgePointType.register(Cjptt.asResource("advancedspeedsign"),AdvancedSpeedSign::new);
}
