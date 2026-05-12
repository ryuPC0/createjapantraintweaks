package io.github.ryuPC0.cjptt.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import io.github.ryuPC0.cjptt.Cjptt;
import io.github.ryuPC0.cjptt.extended.traincontrolsblock.ExtendedControlPacket;
import io.github.ryuPC0.cjptt.mixininterface.CarriageContraptionMixinInterface;
import io.github.ryuPC0.cjptt.registry.CjpttPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.*;

@Mixin(value = CarriageContraptionEntity.class,remap = false)
public class CarriageContraptionEntityMixin extends OrientedContraptionEntity {
    @Shadow public UUID trainId;
    @Shadow public int carriageIndex;
    @Unique
    public Map<UUID,Boolean> cjptt$extconlist;

    public CarriageContraptionEntityMixin(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "<init>",at = @At(value = "TAIL"))
    void cjptt$initInitializationvariable(EntityType<?> type, Level world, CallbackInfo ci){
        cjptt$extconlist = new HashMap<>();
    }
    @Inject(method = "tick",at = @At(value = "INVOKE", target = "Lcom/simibubi/create/content/trains/entity/CarriageContraptionEntity;getPassengers()Ljava/util/List;"))
    void cjptt$tickextconductorcheck(CallbackInfo ci, @Local CarriageContraption cc){
            Set<UUID> checkedextplayers = new HashSet<>(cjptt$extconlist.keySet());
            for (Entity entity : getPassengers()) {
                if (entity instanceof Player player) {
                    if (!checkedextplayers.remove(player.getUUID()))
                        if (cc instanceof CarriageContraptionMixinInterface ccit) {
                            BlockPos seatOf = cc.getSeatOf(entity.getUUID());
                            Direction conductordir = ccit.cjptt$getextendedconductorSeats().getOrDefault(seatOf, null);
                            if (conductordir != null) {
                                cjptt$extconlist.put(player.getUUID(), false);
                                if (player instanceof ServerPlayer serverPlayer)
                                    CjpttPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> serverPlayer), new ExtendedControlPacket(ExtendedControlPacket.Controltype.CONDUCTOR, seatOf.relative(conductordir), trainId, carriageIndex));
                            }
                        }

                }
            }
            if (!checkedextplayers.isEmpty()) {
                MinecraftServer server = level().getServer();

                for (UUID uuid : checkedextplayers) {
                    cjptt$extconlist.remove(uuid);
                    if (server != null) {
                        ServerPlayer player = server.getPlayerList().getPlayer(uuid);
                        if (player != null)
                            CjpttPackets.getChannel().send(PacketDistributor.PLAYER.with(() -> player), new ExtendedControlPacket());
                    }
                }
            }

    }
}
