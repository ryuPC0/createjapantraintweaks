package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.LimitRuleType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class AdvancedSpeedSignPacket extends BlockEntityConfigurationPacket<AdvancedSpeedSignBlockEntity> {
    AbstractLimitRule rule;
    public AdvancedSpeedSignPacket(BlockPos pos,AbstractLimitRule rule) {
        super(pos);
        this.rule = rule;
    }
    public AdvancedSpeedSignPacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        rule.write(buffer);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        LimitRuleType.read(buffer);
    }

    @Override
    protected void applySettings(AdvancedSpeedSignBlockEntity be) {
        AdvancedSpeedSign edgePoint = be.edgePoint.getEdgePoint();
        Cjptt.LOGGER.info("{},{},{}",edgePoint,rule,be.front);
        if(edgePoint != null)
            edgePoint.speed.set(be.front,rule);
    }
}
