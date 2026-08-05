package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector2i;
import org.joml.Vector3d;

public abstract class AbstractLimitRule {
    private LimitRuleType<?> type;
    public void setType(LimitRuleType<?> type) {
        this.type = type;
    }

    public LimitRuleType<?> getType() {
        return type;
    }
    public abstract float GetSpeed();
    public abstract void RenderSign(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, Vector2i posa,Vector2i posb, int light, int overlay, ResourceLocation resrifimage);
    protected Vector3d GetDrawOffSet(AdvancedSpeedSignBlockEntity be, double degree, boolean mode){
        double radian = Math.toRadians(-(degree+90));
        Vector3d pos = new Vector3d(0.5,0.5,0.5);
        Vector3d pos2 = new Vector3d(Math.cos(radian),0,Math.sin(radian)).mul(mode ? -7.0/16 + 0.005:1.0/16 + 0.005);
        return pos.add(pos2);
    }
    protected Double Getdegree(int dir){
        return -(dir > 15 ? (dir - 16) * 90 : (dir * 22.5));
    }
    public abstract void read(CompoundTag nbt);
    public abstract void read(FriendlyByteBuf buffer);
    public CompoundTag write(){
        CompoundTag nbt = new CompoundTag();
        NBTHelper.writeResourceLocation(nbt,"limittype",type.getId());
        return nbt;
    }
    public void write(FriendlyByteBuf buffer){
        buffer.writeResourceLocation(type.getId());
    }
}
