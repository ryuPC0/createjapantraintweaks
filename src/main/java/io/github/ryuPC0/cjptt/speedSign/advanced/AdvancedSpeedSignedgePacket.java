package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.Create;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.trains.graph.DimensionPalette;
import com.simibubi.create.content.trains.graph.TrackGraph;
import com.simibubi.create.content.trains.graph.TrackNodeLocation;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.ryuPC0.cjptt.registry.CjpttEdgePointType;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.LimitRuleType;
import net.createmod.catnip.math.VecHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

@SuppressWarnings("DataFlowIssue")
public class AdvancedSpeedSignedgePacket extends SimplePacketBase {
    UUID graphid;
    UUID signid;
    AbstractLimitRule rule;
    Boolean front;
    public AdvancedSpeedSignedgePacket(AdvancedSpeedSign sign, boolean front,Level level){
        graphid = Create.RAILWAYS.getGraph(level,sign.edgeLocation.getFirst()).id;
        signid = sign.id;
        rule = sign.speed.get(front);
        this.front = front;
    }
    public AdvancedSpeedSignedgePacket(FriendlyByteBuf buffer) {
        graphid = buffer.readUUID();
        signid = buffer.readUUID();
        front = buffer.readBoolean();
        if(buffer.readBoolean())
            rule = LimitRuleType.read(buffer);

    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(graphid);
        buffer.writeUUID(signid);
        buffer.writeBoolean(front);
        if(rule == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        rule.write(buffer);
    }


    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(()->{
            TrackGraph graph = CreateClient.RAILWAYS.trackNetworks.get(graphid);
            if(graph != null) {
                AdvancedSpeedSign sign = graph.getPoint(CjpttEdgePointType.ADVANCED_SPEEDSIGN, signid);
                if(sign != null){
                    sign.speed.set(front,rule == null ? null : rule);
                }
            }
        });
        return true;
    }
}
