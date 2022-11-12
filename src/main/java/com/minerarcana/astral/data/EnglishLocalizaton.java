package com.minerarcana.astral.data;

import com.minerarcana.astral.Astral;
import com.minerarcana.astral.items.AstralItems;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class EnglishLocalizaton extends LanguageProvider {

    public EnglishLocalizaton(DataGenerator gen) {
        super(gen, Astral.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(AstralItems.SNOWBERRIES.get(), "Snowberries");
        add(AstralItems.FEVERWEED_ITEM.get(), "Feverweed");
    }
}
