package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.client.LangKeys;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class Lang extends LanguageProvider {
    private final String locale;

    public Lang(DataGenerator gen, String locale) {
        super(gen, Salt.ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if (locale.equals("en_us"))
            addEN_US();
    }

    protected void addEN_US() {
        add(Salt.Items.SALT.get(), "Salt");
        add(Salt.Items.RAW_ROCK_SALT.get(), "Raw Rock Salt");

        add(Salt.Blocks.SALT_BLOCK.get(), "Salt Block");
        add(Salt.Blocks.ROCK_SALT_ORE.get(), "Rock Salt");
        add(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get(), "Deepslate Rock Salt");
        add(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get(), "Block of Raw Rock Salt");
        add(Salt.Blocks.SALT_CLUSTER.get(), "Salt Cluster");
        add(Salt.Blocks.LARGE_SALT_BUD.get(), "Large Salt Bud");
        add(Salt.Blocks.MEDIUM_SALT_BUD.get(), "Medium Salt Bud");
        add(Salt.Blocks.SMALL_SALT_BUD.get(), "Small Salt Bud");
        add(Salt.Blocks.SALT_CAULDRON.get(), "Salt Cauldron");

        add(LangKeys.GUI_TOOLTIP_SALTED, "Salted");
        add(LangKeys.GUI_TOOLTIP_SALTED_EXPANDED_PART, ": %s nutrition");

        add(LangKeys.SUBTITLES_DISSOLVES, "Block dissolves");
        add(LangKeys.SUBTITLES_MELTS, "Block melts");
        add(LangKeys.SUBTITLES_CAULDRON_EVAPORATE, "Water evaporates");
    }
}
