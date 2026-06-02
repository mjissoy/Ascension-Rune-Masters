package net.zic.runic_ascension.content.runes;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface IRunicRune {

    ResourceLocation getId();

    Component getName();

    RunicRuneType getType();

    RunicRuneDepth getDepth();

    int getMinimumRunicRealmToObserve();

    int getMinimumRunicRealmToUse();
}