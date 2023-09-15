package nickwrecks.demonicvessel.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import nickwrecks.demonicvessel.item.ModItems;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        nineBlockStorageRecipes(pWriter,RecipeCategory.MISC,ModItems.ABBADONIUM_INGOT.get(),RecipeCategory.MISC,ModItems.ABBADONIUM_BLOCK.get());
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ModItems.EMPTY_SOUL_SYRINGE.get())
                .pattern("IGI")
                .pattern("IGI")
                .pattern(" G ")
                .define('I', Items.IRON_INGOT)
                .define('G', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.ABBADONIUM_GEAR.get(),4)
                .pattern(" A ")
                .pattern("A A")
                .pattern(" A ")
                .define('A', ModItems.ABBADONIUM_INGOT.get())
                .unlockedBy("has_abbadonium_ingot", has(ModItems.ABBADONIUM_INGOT.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC,ModItems.ABBADONIUM_JACKET.get())
                .pattern("AOA")
                .pattern("A A")
                .pattern("AOA")
                .define('A', ModItems.ABBADONIUM_INGOT.get())
                .define('O',ModItems.ABBADONIUM_GEAR.get())
                .unlockedBy("has_abbadonium_gear", has(ModItems.ABBADONIUM_GEAR.get()))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE,ModItems.BATTERY_BLOCK_ITEM.get())
                .pattern(" G ")
                .pattern("GRG")
                .pattern(" J ")
                .define('G',Items.GOLD_INGOT)
                .define('R',Items.REDSTONE_BLOCK)
                .define('J',ModItems.ABBADONIUM_JACKET.get())
                .unlockedBy("has_abbadonium_jacket", has(ModItems.ABBADONIUM_JACKET.get()))
                .save(pWriter);
    }
}