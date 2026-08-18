package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlock;
import io.github.ryuPC0.cjptt.speedSign.advanced.AdvancedSpeedSignBlockEntity;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector2i;
import org.joml.Vector3d;

import java.util.Collection;
import java.util.List;

public abstract class AbstractLimitRule {
    private LimitRuleType<?> type;
    public void setType(LimitRuleType<?> type) {
        this.type = type;
    }

    public LimitRuleType<?> getType() {
        return type;
    }
    public abstract float GetSpeed();
    @OnlyIn(Dist.CLIENT)
    public abstract void RenderSign(AdvancedSpeedSignBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, Vector2i posa,Vector2i posb, int light, int overlay, ResourceLocation resrifimage);
    public abstract void RenderAdditionalSettings(Collection<AbstractSimiWidget> wigetlist);
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
    protected double GetDegree(AdvancedSpeedSignBlockEntity be){
        int dir = be.getBlockState().getValue(AdvancedSpeedSignBlock.ROTATION);
        return be.Getdegree(dir);
    }
    protected Vector3d GetDrawOffset(AdvancedSpeedSignBlockEntity be,double degree){
        int dir = be.getBlockState().getValue(AdvancedSpeedSignBlock.ROTATION);
        return be.GetDrawOffSet(be,degree,dir > 15);
    }
}
