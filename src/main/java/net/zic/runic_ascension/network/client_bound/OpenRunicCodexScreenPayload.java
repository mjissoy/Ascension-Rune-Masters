package net.zic.runic_ascension.network.client_bound;

import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.thejadeproject.ascension.refactor_packages.util.ByteBufUtil;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.client.gui.screens.RunicCodexScreen;
import net.zic.runic_ascension.content.RunicPathHelper;
import net.zic.runic_ascension.content.RunicPlayerData;
import net.zic.runic_ascension.content.casting.RunicDiscoveredFormula;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public record OpenRunicCodexScreenPayload(
        List<ResourceLocation> knownRunes,
        List<ResourceLocation> discoveredSequences,
        List<RunicDiscoveredFormula> discoveredFormulas,
        List<String> discoveredScraps,
        String focusedScrap
) implements CustomPacketPayload {

    public static final Type<OpenRunicCodexScreenPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "open_runic_codex_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OpenRunicCodexScreenPayload> STREAM_CODEC =
            StreamCodec.of(OpenRunicCodexScreenPayload::encode, OpenRunicCodexScreenPayload::decode);

    public OpenRunicCodexScreenPayload {
        knownRunes = knownRunes == null ? List.of() : List.copyOf(knownRunes);
        discoveredSequences = discoveredSequences == null ? List.of() : List.copyOf(discoveredSequences);
        discoveredFormulas = discoveredFormulas == null ? List.of() : List.copyOf(discoveredFormulas);
        discoveredScraps = discoveredScraps == null ? List.of() : List.copyOf(discoveredScraps);
        focusedScrap = focusedScrap == null ? "" : focusedScrap;
    }

    public OpenRunicCodexScreenPayload(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas
    ) {
        this(knownRunes, discoveredSequences, discoveredFormulas, List.of(), "");
    }

    public static void sendTo(ServerPlayer player) {
        sendTo(player, "");
    }

    public static void sendTo(ServerPlayer player, String focusedScrap) {
        if (player == null) {
            return;
        }

        RunicPlayerData data = RunicPathHelper.getRunicData(player);

        List<ResourceLocation> knownRunes = data.getKnownRunes().stream()
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        List<ResourceLocation> discoveredSequences = data.getDiscoveredSequences().stream()
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .toList();

        List<RunicDiscoveredFormula> discoveredFormulas = data.getDiscoveredFormulas().stream()
                .sorted(Comparator.comparing(formula -> formula.formulaId().toString()))
                .toList();

        List<String> discoveredScraps = data.getDiscoveredScraps().stream()
                .sorted()
                .toList();

        PacketDistributor.sendToPlayer(player, new OpenRunicCodexScreenPayload(
                knownRunes,
                discoveredSequences,
                discoveredFormulas,
                discoveredScraps,
                focusedScrap
        ));
    }

    public static void encode(RegistryFriendlyByteBuf buf, OpenRunicCodexScreenPayload packet) {
        writeResourceLocationList(buf, packet.knownRunes);
        writeResourceLocationList(buf, packet.discoveredSequences);
        writeFormulaList(buf, packet.discoveredFormulas);
        writeStringList(buf, packet.discoveredScraps);
        buf.writeUtf(packet.focusedScrap);
    }

    public static OpenRunicCodexScreenPayload decode(RegistryFriendlyByteBuf buf) {
        List<ResourceLocation> knownRunes = readResourceLocationList(buf);
        List<ResourceLocation> discoveredSequences = readResourceLocationList(buf);
        List<RunicDiscoveredFormula> discoveredFormulas = readFormulaList(buf);
        List<String> discoveredScraps = readStringList(buf);
        String focusedScrap = buf.readUtf();

        return new OpenRunicCodexScreenPayload(
                knownRunes,
                discoveredSequences,
                discoveredFormulas,
                discoveredScraps,
                focusedScrap
        );
    }

    private static void writeResourceLocationList(RegistryFriendlyByteBuf buf, List<ResourceLocation> ids) {
        buf.writeInt(ids.size());

        for (ResourceLocation id : ids) {
            ByteBufUtil.encodeString(buf, id.toString());
        }
    }

    private static List<ResourceLocation> readResourceLocationList(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        List<ResourceLocation> ids = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            ids.add(ByteBufUtil.readResourceLocation(buf));
        }

        return ids;
    }

    private static void writeFormulaList(RegistryFriendlyByteBuf buf, List<RunicDiscoveredFormula> formulas) {
        buf.writeInt(formulas.size());

        for (RunicDiscoveredFormula formula : formulas) {
            RunicDiscoveredFormula.encode(buf, formula);
        }
    }

    private static List<RunicDiscoveredFormula> readFormulaList(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        List<RunicDiscoveredFormula> formulas = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            formulas.add(RunicDiscoveredFormula.decode(buf));
        }

        return formulas;
    }

    private static void writeStringList(RegistryFriendlyByteBuf buf, List<String> values) {
        buf.writeInt(values.size());

        for (String value : values) {
            buf.writeUtf(value);
        }
    }

    private static List<String> readStringList(RegistryFriendlyByteBuf buf) {
        int size = buf.readInt();
        List<String> values = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            values.add(buf.readUtf());
        }

        return values;
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handlePayload(OpenRunicCodexScreenPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                open(
                        payload.knownRunes,
                        payload.discoveredSequences,
                        payload.discoveredFormulas,
                        payload.discoveredScraps,
                        payload.focusedScrap
                );
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static void open(
            List<ResourceLocation> knownRunes,
            List<ResourceLocation> discoveredSequences,
            List<RunicDiscoveredFormula> discoveredFormulas,
            List<String> discoveredScraps,
            String focusedScrap
    ) {
        Minecraft.getInstance().setScreen(new RunicCodexScreen(
                knownRunes,
                discoveredSequences,
                discoveredFormulas,
                discoveredScraps,
                focusedScrap
        ));
    }
}
