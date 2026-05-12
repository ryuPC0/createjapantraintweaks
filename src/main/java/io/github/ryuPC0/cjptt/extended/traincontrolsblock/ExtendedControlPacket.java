package io.github.ryuPC0.cjptt.extended.traincontrolsblock;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import io.github.ryuPC0.cjptt.Cjptt;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;

public class ExtendedControlPacket extends SimplePacketBase {
    Controltype controltype;
    BlockPos blockPos;
    UUID id;
    int carrigeindex;
    public ExtendedControlPacket(){
        this.controltype = Controltype.NONE;
        this.blockPos = new BlockPos(0,0,0);
        this.id = null;
        this.carrigeindex = -1;
    }
    public ExtendedControlPacket(Controltype controltype, BlockPos blockPos, UUID trainid,int carrigeindex){
        this.controltype = controltype;
        this.blockPos = blockPos;
        this.id = trainid;
        this.carrigeindex = carrigeindex;
    }
    public ExtendedControlPacket(FriendlyByteBuf buffer){
        controltype = buffer.readEnum(Controltype.class);
        blockPos = buffer.readBlockPos();
        id = buffer.readUUID();
        carrigeindex = buffer.readInt();
    }
    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeEnum(controltype);
        buffer.writeBlockPos(blockPos);
        if(id != null)
        buffer.writeUUID(id);
        else buffer.writeUUID(new UUID(0,0));
        buffer.writeInt(carrigeindex);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() ->{
            switch (controltype){
                case NONE ->{
                    Cjptt.LOGGER.info("end");
                    ExtendedControlsHandler.EndConductor();
                }
                case CONDUCTOR ->{
                    Train train = Create.RAILWAYS.trains.get(id);
                    Cjptt.LOGGER.info("{}",train);
                    if(train !=null){
                        CarriageContraptionEntity entity = train.carriages.get(carrigeindex).anyAvailableEntity();
                        if(entity != null){
                            ExtendedControlsHandler.StartConductor(entity,blockPos);
                        }
                    }
                }
            }
        });
        return true;
    }
    public enum Controltype{
        NONE,CONDUCTOR
    }
}
