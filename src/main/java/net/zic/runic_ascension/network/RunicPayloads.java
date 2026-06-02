package net.zic.runic_ascension.network;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.network.client_bound.OpenRunicCastingScreenPayload;
import net.zic.runic_ascension.network.client_bound.OpenRunicCodexScreenPayload;
import net.zic.runic_ascension.network.server_bound.CastRunicSequencePayload;

public final class RunicPayloads {

    private RunicPayloads() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(RunicPayloads::registerPayloads);
    }

    private static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(RunicAscension.MOD_ID)
                .versioned("1.0.0");

        registrar.playToClient(
                OpenRunicCastingScreenPayload.TYPE,
                OpenRunicCastingScreenPayload.STREAM_CODEC,
                OpenRunicCastingScreenPayload::handlePayload
        );

        registrar.playToClient(
                OpenRunicCodexScreenPayload.TYPE,
                OpenRunicCodexScreenPayload.STREAM_CODEC,
                OpenRunicCodexScreenPayload::handlePayload
        );

        registrar.playToServer(
                CastRunicSequencePayload.TYPE,
                CastRunicSequencePayload.STREAM_CODEC,
                CastRunicSequencePayload::handlePayload
        );
    }
}