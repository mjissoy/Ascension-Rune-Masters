package net.zic.runic_ascension.common.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.thejadeproject.ascension.common.items.ModItems;
import net.zic.runic_ascension.RunicAscension;
import net.zic.runic_ascension.core.items.RunicItems;

import java.util.concurrent.CompletableFuture;

public class RunicRecipeProvider extends RecipeProvider {

    public RunicRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        runicCodex(recipeOutput);
        runicBrushes(recipeOutput);
        runicInscriptionSeal(recipeOutput);
    }

    private void runicCodex(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RunicItems.RUNIC_CODEX.get())
                .pattern("IPI")
                .pattern("PBP")
                .pattern(" A ")
                .define('I', Items.INK_SAC)
                .define('P', Items.PAPER)
                .define('B', Items.BOOK)
                .define('A', ModItems.SPIRITUAL_STONE)
                .unlockedBy("has_spiritual_stone", has(ModItems.SPIRITUAL_STONE))
                .save(recipeOutput, RunicAscension.MOD_ID + ":shaped/runic_codex");
    }

    private void runicBrushes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RunicItems.BASIC_RUNIC_BRUSH.get())
                .pattern(" F ")
                .pattern(" I ")
                .pattern(" S ")
                .define('F', Items.FEATHER)
                .define('I', Items.INK_SAC)
                .define('S', Items.STICK)
                .unlockedBy("has_ink_sac", has(Items.INK_SAC))
                .save(recipeOutput, RunicAscension.MOD_ID + ":shaped/basic_runic_brush");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RunicItems.EARTH_RUNIC_BRUSH.get())
                .pattern("DED")
                .pattern("EBE")
                .pattern("DID")
                .define('B', RunicItems.BASIC_RUNIC_BRUSH.get())
                .define('D', Items.DEEPSLATE)
                .define('E', ModItems.EARTH_CORE.get())
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_basic_runic_brush", has(RunicItems.BASIC_RUNIC_BRUSH.get()))
                .save(recipeOutput, RunicAscension.MOD_ID + ":shaped/earth_runic_brush");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RunicItems.HEAVEN_RUNIC_BRUSH.get())
                .pattern("GSG")
                .pattern("ABA")
                .pattern("GFG")
                .define('B', RunicItems.BASIC_RUNIC_BRUSH.get())
                .define('G', Items.GLOWSTONE_DUST)
                .define('S', ModItems.SPIRITUAL_STONE.get())
                .define('A', Items.AMETHYST_SHARD)
                .define('F', Items.FEATHER)
                .unlockedBy("has_basic_runic_brush", has(RunicItems.BASIC_RUNIC_BRUSH.get()))
                .save(recipeOutput, RunicAscension.MOD_ID + ":shaped/heaven_runic_brush");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RunicItems.HELL_RUNIC_BRUSH.get())
                .pattern("PFP")
                .pattern("CBC")
                .pattern("PMP")
                .define('B', RunicItems.BASIC_RUNIC_BRUSH.get())
                .define('P', Items.BLAZE_POWDER)
                .define('F', ModItems.FIRE_CORE.get())
                .define('C', Items.COAL)
                .define('M', Items.MAGMA_CREAM)
                .unlockedBy("has_basic_runic_brush", has(RunicItems.BASIC_RUNIC_BRUSH.get()))
                .save(recipeOutput, RunicAscension.MOD_ID + ":shaped/hell_runic_brush");


    }

    private void runicInscriptionSeal(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, RunicItems.RUNIC_INSCRIPTION_SEAL.get())
                .pattern(" A ")
                .pattern("CIC")
                .pattern(" A ")
                .define('A', ModItems.SPIRITUAL_STONE)
                .define('C', ModItems.BLACK_IRON_INGOT)
                .define('I', Items.IRON_NUGGET)
                .unlockedBy("has_iron_nugget", has(Items.IRON_NUGGET))
                .save(recipeOutput, RunicAscension.MOD_ID + ":shaped/runic_inscription_seal");
    }
}