package io.github.ryuPC0.cjptt.speedSign.advanced.limitrule;

import io.github.ryuPC0.cjptt.Cjptt;
import net.createmod.catnip.nbt.NBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LimitRuleType<T extends AbstractLimitRule> {
    public static final Map<ResourceLocation, LimitRuleType<?>> TYPES = new HashMap<>();
    private ResourceLocation id;
    private Supplier<T> factory;

    public static LimitRuleType<Limitrem> LIMITREM = register(Cjptt.asResource("limitrem"),Limitrem::new);
    public static LimitRuleType<SimpleLimit> SIMPLELIMIT = register(Cjptt.asResource("simplelimit"),SimpleLimit::new);

    public static <T extends AbstractLimitRule> LimitRuleType<T> register(ResourceLocation id, Supplier<T> factory) {
        LimitRuleType<T> type = new LimitRuleType<>(id, factory);
        TYPES.put(id, type);
        return type;
    }

    public LimitRuleType(ResourceLocation id, Supplier<T> factory) {
        this.id = id;
        this.factory = factory;
    }

    public T create() {
        T t = factory.get();
        t.setType(this);
        return t;
    }

    public ResourceLocation getId() {
        return id;
    }

    public static AbstractLimitRule read(FriendlyByteBuf buffer) {
        ResourceLocation type = buffer.readResourceLocation();
        LimitRuleType<?> ruleType = TYPES.get(type);
        if(ruleType == null) return null;
        AbstractLimitRule rule = ruleType.create();
        rule.read(buffer);
        return rule;
    }
    public static AbstractLimitRule read(CompoundTag tag){
        ResourceLocation type = NBTHelper.readResourceLocation(tag,"limittype");
        LimitRuleType<?> ruleType = TYPES.get(type);
        if(ruleType == null) return null;
        AbstractLimitRule rule = ruleType.create();
        rule.read(tag);
        return rule;
    }
}
