package net.zic.runic_ascension.common.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.items.RunicItems;

public class RunicItemModelProvider extends ItemModelProvider {

    public RunicItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, RunicAscension.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(RunicItems.RUNIC_CODEX.get());
        tomeItem(RunicItems.RUNIC_TOME.get());
        tomeItem(RunicItems.RUNIC_TOME_BASE_ELEMENTS.get());
        tomeItem(RunicItems.RUNIC_TOME_DEEP_ELEMENTS.get());
        tomeItem(RunicItems.RUNIC_TOME_INTENTS.get());
        tomeItem(RunicItems.RUNIC_TOME_FORMS.get());
        tomeItem(RunicItems.RUNIC_TOME_MODIFIERS.get());
        basicItem(RunicItems.BASIC_RUNIC_BRUSH.get());
    }

    private ItemModelBuilder tomeItem(Item item) {
        return withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/runic_tome"));
    }

    private String name(Item item) {
        return item.builtInRegistryHolder().key().location().getPath();
    }
}
