package io.github.ryuPC0.cjptt.extended.train;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.ryuPC0.cjptt.mixininterface.TrainMixinInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class ExtendedTrainPacket extends SimplePacketBase {
    UUID trainid;
    boolean extendedtrain;
    public ExtendedTrainPacket(Train train){
        TrainMixinInterface exttrain = (TrainMixinInterface) train;
        extendedtrain = exttrain.cjptt$getisextendedtrain();
        trainid = train.id;
    }
    public ExtendedTrainPacket(FriendlyByteBuf buffer){
        trainid = buffer.readUUID();
        extendedtrain = buffer.readBoolean();
    }
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeUUID(trainid);
        buffer.writeBoolean(extendedtrain);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() ->{
            Train train = Create.RAILWAYS.trains.get(trainid);
            if(train == null)
                return;
            ((TrainMixinInterface)train).cjptt$setisextendedtrain(extendedtrain);
        });
        return true;
    }
}
