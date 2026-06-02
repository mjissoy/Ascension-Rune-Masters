package net.zic.runic_ascension.content;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.content.runes.IRunicRune;
import net.zic.runic_ascension.content.sequences.IRunicSequence;

@EventBusSubscriber(
        modid = RunicAscension.MOD_ID,
        bus = EventBusSubscriber.Bus.MOD
)
public final class RunicAscensionRegistries {

    private RunicAscensionRegistries() {
    }

    @SubscribeEvent
    public static void registerRegistries(NewRegistryEvent event) {
        event.register(RunicRunes.RUNIC_RUNES_REGISTRY);
        event.register(RunicSequences.RUNIC_SEQUENCES_REGISTRY);
    }

    public static final class RunicRunes {
        public static final ResourceKey<Registry<IRunicRune>> RUNIC_RUNES_REGISTRY_KEY =
                ResourceKey.createRegistryKey(
                        ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "runic_runes")
                );

        public static final Registry<IRunicRune> RUNIC_RUNES_REGISTRY =
                new RegistryBuilder<>(RUNIC_RUNES_REGISTRY_KEY).create();

        private RunicRunes() {
        }
    }

    public static final class RunicSequences {
        public static final ResourceKey<Registry<IRunicSequence>> RUNIC_SEQUENCES_REGISTRY_KEY =
                ResourceKey.createRegistryKey(
                        ResourceLocation.fromNamespaceAndPath(RunicAscension.MOD_ID, "runic_sequences")
                );

        public static final Registry<IRunicSequence> RUNIC_SEQUENCES_REGISTRY =
                new RegistryBuilder<>(RUNIC_SEQUENCES_REGISTRY_KEY).create();

        private RunicSequences() {
        }
    }
}