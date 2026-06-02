package net.zic.runic_ascension.content.casting;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record RunicDiscoveredFormula(
        ResourceLocation formulaId,
        List<ResourceLocation> runes,
        int castCount
) {
    private static final String KEY_ID = "id";
    private static final String KEY_RUNES = "runes";
    private static final String KEY_CAST_COUNT = "cast_count";

    public RunicDiscoveredFormula {
        runes = runes == null ? List.of() : List.copyOf(runes);
        castCount = Math.max(0, castCount);
    }

    public RunicFormulaMasteryGrade getMasteryGrade() {
        return RunicFormulaMasteryGrade.fromCastCount(castCount);
    }

    public RunicDiscoveredFormula withAdditionalCast(List<ResourceLocation> latestRunes) {
        List<ResourceLocation> storedRunes = latestRunes == null || latestRunes.isEmpty()
                ? runes
                : List.copyOf(latestRunes);

        return new RunicDiscoveredFormula(formulaId, storedRunes, castCount + 1);
    }

    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putString(KEY_ID, formulaId.toString());
        tag.put(KEY_RUNES, writeRuneList(runes));
        tag.putInt(KEY_CAST_COUNT, castCount);
        return tag;
    }

    public static RunicDiscoveredFormula deserializeNBT(CompoundTag tag) {
        ResourceLocation formulaId = ResourceLocation.tryParse(tag.getString(KEY_ID));

        if (formulaId == null) {
            return null;
        }

        List<ResourceLocation> runes = readRuneList(tag.getList(KEY_RUNES, net.minecraft.nbt.Tag.TAG_STRING));
        int castCount = tag.getInt(KEY_CAST_COUNT);

        return new RunicDiscoveredFormula(formulaId, runes, castCount);
    }

    public static void encode(RegistryFriendlyByteBuf buf, RunicDiscoveredFormula formula) {
        buf.writeUtf(formula.formulaId().toString());
        buf.writeInt(formula.runes().size());

        for (ResourceLocation runeId : formula.runes()) {
            buf.writeUtf(runeId.toString());
        }

        buf.writeInt(formula.castCount());
    }

    public static RunicDiscoveredFormula decode(RegistryFriendlyByteBuf buf) {
        ResourceLocation formulaId = ResourceLocation.tryParse(buf.readUtf());
        int size = buf.readInt();
        List<ResourceLocation> runes = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ResourceLocation runeId = ResourceLocation.tryParse(buf.readUtf());

            if (runeId != null) {
                runes.add(runeId);
            }
        }

        int castCount = buf.readInt();

        if (formulaId == null) {
            formulaId = createFallbackFormulaId(runes);
        }

        return new RunicDiscoveredFormula(formulaId, runes, castCount);
    }

    private static ListTag writeRuneList(List<ResourceLocation> runes) {
        ListTag list = new ListTag();

        for (ResourceLocation runeId : runes) {
            list.add(StringTag.valueOf(runeId.toString()));
        }

        return list;
    }

    private static List<ResourceLocation> readRuneList(ListTag list) {
        List<ResourceLocation> runes = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            ResourceLocation runeId = ResourceLocation.tryParse(list.getString(i));

            if (runeId != null) {
                runes.add(runeId);
            }
        }

        return List.copyOf(runes);
    }

    private static ResourceLocation createFallbackFormulaId(List<ResourceLocation> runes) {
        String joined = runes.stream()
                .map(id -> id.getNamespace() + "." + id.getPath())
                .reduce((a, b) -> a + "_" + b)
                .orElse("empty");

        return ResourceLocation.fromNamespaceAndPath("runic_ascension", "formula/" + joined);
    }
}
