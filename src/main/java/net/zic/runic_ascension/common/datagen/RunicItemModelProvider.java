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
        tomeItem(RunicItems.RUNIC_TOME_EMBER_BOLT.get());
        tomeItem(RunicItems.RUNIC_TOME_STONE_WARD.get());
        tomeItem(RunicItems.RUNIC_TOME_GENTLE_RENEWAL.get());
        tomeItem(RunicItems.RUNIC_TOME_WIND_STEP.get());
        tomeItem(RunicItems.RUNIC_TOME_FROST_BIND.get());
        tomeItem(RunicItems.RUNIC_TOME_STORM_PIERCE.get());
        scrapItem(RunicItems.RUNIC_SCRAP_ORIGIN.get());
        scrapItem(RunicItems.RUNIC_SCRAP_INTENT.get());
        scrapItem(RunicItems.RUNIC_SCRAP_FORMS.get());
        scrapItem(RunicItems.RUNIC_SCRAP_MODIFIERS.get());
        scrapItem(RunicItems.RUNIC_SCRAP_INSTABILITY.get());
        scrapItem(RunicItems.RUNIC_SCRAP_SUPPRESSION.get());
        scrapItem(RunicItems.RUNIC_SCRAP_FIRST_FORMULA.get());
        scrapItem(RunicItems.RUNIC_SCRAP_FREE_CASTING.get());
        brushItem(RunicItems.BASIC_RUNIC_BRUSH.get());
        brushItem(RunicItems.EARTH_RUNIC_BRUSH.get());
        brushItem(RunicItems.HEAVEN_RUNIC_BRUSH.get());
        brushItem(RunicItems.HELL_RUNIC_BRUSH.get());

        basicItem(RunicItems.RUNIC_INSCRIPTION_SEAL.get());


    }


    private ItemModelBuilder scrapItem(Item item) {
        return withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/runic_scrap"));
    }

    private ItemModelBuilder tomeItem(Item item) {
        return withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/runic_tome"));
    }

    private ItemModelBuilder brushItem(Item item) {
        return withExistingParent(name(item), mcLoc("item/generated"))
                .texture("layer0", modLoc("item/basic_runic_brush"));
    }

    private String name(Item item) {
        return item.builtInRegistryHolder().key().location().getPath();
    }
}
