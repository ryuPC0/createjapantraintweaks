package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.foundation.networking.BlockEntityConfigurationPacket;
import io.github.ryuPC0.cjptt.registry.CjpttPackets;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.LimitRuleType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.PacketDistributor;

public class AdvancedSpeedSignbePacket extends BlockEntityConfigurationPacket<AdvancedSpeedSignBlockEntity> {
    AbstractLimitRule rule;
    public AdvancedSpeedSignbePacket(BlockPos pos, AbstractLimitRule rule) {
        super(pos);
        this.rule = rule;
    }
    public AdvancedSpeedSignbePacket(FriendlyByteBuf buffer) {
        super(buffer);
    }

    @Override
    protected void writeSettings(FriendlyByteBuf buffer) {
        rule.write(buffer);
    }

    @Override
    protected void readSettings(FriendlyByteBuf buffer) {
        rule = LimitRuleType.read(buffer);
    }

    @Override
    protected void applySettings(AdvancedSpeedSignBlockEntity be) {
        AdvancedSpeedSign edgePoint = be.getedgePoint();
        if(edgePoint != null) {
            edgePoint.speed.set(be.front, rule);
            CjpttPackets.getChannel().send(PacketDistributor.ALL.noArg(), new AdvancedSpeedSignedgePacket(edgePoint,be.front,be.getLevel()));
        }
    }
}
