package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.client.LangKeys;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class Languages extends LanguageProvider {
    private final String locale;

    public Languages(DataGenerator gen, String locale) {
        super(gen, Salt.ID, locale);
        this.locale = locale;
    }

    @Override
    protected void addTranslations() {
        if (locale.equals("en_us"))
            en_us();
        else if (locale.equals("uk_ua"))
            uk_ua();
    }

    protected void en_us() {
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
        add(Salt.Blocks.SALT_LAMP.get(), "Salt Lamp");

        add(LangKeys.GUI_TOOLTIP_SALTED, "Salted");
        add(LangKeys.GUI_TOOLTIP_SALTED_EXPANDED_PART, ": %s nutrition");

        add(LangKeys.ADVANCEMENT_TASTE_EXPLOSION_TITLE, "Taste Explosion");
        add(LangKeys.ADVANCEMENT_TASTE_EXPLOSION_DESCRIPTION, "Consume a salted food, gaining additional nutrition");
        add(LangKeys.ADVANCEMENT_BOIL_OFF_TITLE, "Boil Off");
        add(LangKeys.ADVANCEMENT_BOIL_OFF_DESCRIPTION, "Scrape off the Salt that is formed by boiling water in a cauldron");
        add(LangKeys.ADVANCEMENT_CRYSTAL_GARDEN_TITLE, "Crystal Garden");
        add(LangKeys.ADVANCEMENT_CRYSTAL_GARDEN_DESCRIPTION, "Harvest fully grown Salt Cluster that has been formed by dripping water");

        add(LangKeys.JEI_CATEGORY_SALT_CRYSTAL_GROWING, "Growing Salt Crystals");
        add(LangKeys.JEI_CATEGORY_SALT_EVAPORATION, "Water Evaporation");
        add(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP, "Heat Source");
        add(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP_2, "Any ");

        add(LangKeys.SUBTITLES_DISSOLVES, "Block dissolves");
        add(LangKeys.SUBTITLES_MELTS, "Block melts");
        add(LangKeys.SUBTITLES_CAULDRON_EVAPORATE, "Water evaporates");
    }

    protected void uk_ua() {
        add(Salt.Items.SALT.get(), "Сіль");
        add(Salt.Items.RAW_ROCK_SALT.get(), "Необроблена кам’яна сіль");

        add(Salt.Blocks.SALT_BLOCK.get(), "Блок солі");
        add(Salt.Blocks.ROCK_SALT_ORE.get(), "Кам’яна сіль");
        add(Salt.Blocks.DEEPSLATE_ROCK_SALT_ORE.get(), "Глибосланцева кам’яна сіль");
        add(Salt.Blocks.RAW_ROCK_SALT_BLOCK.get(), "Блок необробленої кам’яної солі");
        add(Salt.Blocks.SALT_CLUSTER.get(), "Соляна друза");
        add(Salt.Blocks.LARGE_SALT_BUD.get(), "Великий зародок солі");
        add(Salt.Blocks.MEDIUM_SALT_BUD.get(), "Середній зародок солі");
        add(Salt.Blocks.SMALL_SALT_BUD.get(), "Малий зародок солі");
        add(Salt.Blocks.SALT_CAULDRON.get(), "Казан із сіллю");
        add(Salt.Blocks.SALT_LAMP.get(), "Соляна лампа");

        add(LangKeys.GUI_TOOLTIP_SALTED, "Солоне");
        add(LangKeys.GUI_TOOLTIP_SALTED_EXPANDED_PART, ": %s до поживної цінності");

        add(LangKeys.ADVANCEMENT_TASTE_EXPLOSION_TITLE, "А пахне як!...");
        add(LangKeys.ADVANCEMENT_TASTE_EXPLOSION_DESCRIPTION, "Отримайте додаткову поживну цінність, з’ївши посолену їжу");
        add(LangKeys.ADVANCEMENT_BOIL_OFF_TITLE, "Соляна пара");
        add(LangKeys.ADVANCEMENT_BOIL_OFF_DESCRIPTION, "Вишкребіть сіль, яка залишилась від википівшої води у казані");
        add(LangKeys.ADVANCEMENT_CRYSTAL_GARDEN_TITLE, "Рости, сілька, велика і маленька");
        add(LangKeys.ADVANCEMENT_CRYSTAL_GARDEN_DESCRIPTION, "Зберіть Соляну друзу, яка виросла від крапаючої води");

        add(LangKeys.JEI_CATEGORY_SALT_CRYSTAL_GROWING, "Вирощування кристалів солі");
        add(LangKeys.JEI_CATEGORY_SALT_EVAPORATION, "Випаровування води");
        add(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP, "Джерело тепла");
        add(LangKeys.JEI_CATEGORY_SALT_EVAPORATION_HEAT_SOURCE_TOOLTIP_2, "Будь-який з ");

        add(LangKeys.SUBTITLES_DISSOLVES, "Блок розчиняється");
        add(LangKeys.SUBTITLES_MELTS, "Блок плавиться");
        add(LangKeys.SUBTITLES_CAULDRON_EVAPORATE, "Вода випаровується");
    }
}
