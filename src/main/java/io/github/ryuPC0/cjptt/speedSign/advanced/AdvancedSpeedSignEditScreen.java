package io.github.ryuPC0.cjptt.speedSign.advanced;

import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;
import io.github.ryuPC0.cjptt.registry.CjpttPackets;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.AbstractLimitRule;
import io.github.ryuPC0.cjptt.speedSign.advanced.limitrule.LimitRuleType;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.widget.AbstractSimiWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.*;

public class AdvancedSpeedSignEditScreen extends AbstractSimiScreen {
    private SelectionScrollInput type;
    private Label typelabel;
    private Map<LimitRuleType<?>,Component> typeoptions;
    protected AdvancedSpeedSignBlockEntity be;
    protected AdvancedSpeedSign sign;
    private AbstractLimitRule buffer;
    private Collection<AbstractSimiWidget> additionaloption;
    public AdvancedSpeedSignEditScreen(AdvancedSpeedSignBlockEntity be,AdvancedSpeedSign sign){
        super();
        this.be = be;
        this.sign = sign;
        additionaloption = new ArrayList<>();
    }
    @Override
    public void tick() {
        super.tick();
    }

    @Override
    protected void init() {
        super.init();
        typeoptions = new HashMap<>();
        typeoptions.put(LimitRuleType.LIMITREM,Component.translatable("cjptt.Limitrem"));
        typeoptions.put(LimitRuleType.SIMPLELIMIT,Component.literal("通常/分岐機制限"));
        type = new SelectionScrollInput(guiLeft,guiTop,100,14);
        type.withStepFunction( ctx -> type.standardStep()
                .apply(ctx));
        buffer = sign.speed.get(be.front);
        if(buffer == null)
            buffer = LimitRuleType.LIMITREM.create();
        type.calling((c)-> {
            LimitRuleType<?> type = typeoptions.keySet().stream().toList().get(c);
            if(buffer.getType() != type) {
                buffer = type.create();
                OptionUpdate();
            }
        });
        type.forOptions(typeoptions.values().stream().toList());
        type.setState(typeoptions.keySet().stream().toList().indexOf(buffer.getType()));
        typelabel = new Label(guiLeft,guiTop,Component.literal(""));
        type.writingTo(typelabel);
        type.setActive(true).visible = true;
        typelabel.setActive(true).visible = true;
        addRenderableWidget(type);
        addRenderableWidget(typelabel);
        OptionUpdate();
    }

    void OptionUpdate(){
        removeWidgets(additionaloption);
        additionaloption.clear();
        buffer.RenderAdditionalSettings(additionaloption);
        if(!additionaloption.isEmpty())
            addRenderableWidgets(additionaloption);
    }

    @Override
    public void removed() {
        CjpttPackets.getChannel().sendToServer(new AdvancedSpeedSignbePacket(be.getBlockPos(),buffer));
        super.removed();
    }

    @Override
    protected void renderWindow(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {

    }
}
