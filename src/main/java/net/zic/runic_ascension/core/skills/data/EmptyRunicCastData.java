package net.zic.runic_ascension.core.skills.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.thejadeproject.ascension.refactor_packages.skills.castable.ICastData;

public class EmptyRunicCastData implements ICastData {

    @Override
    public CompoundTag write() {
        return new CompoundTag();
    }

    @Override
    public void encode(RegistryFriendlyByteBuf buf) {
    }
}