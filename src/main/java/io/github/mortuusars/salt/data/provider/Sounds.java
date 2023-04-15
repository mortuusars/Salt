package io.github.mortuusars.salt.data.provider;

import io.github.mortuusars.salt.Salt;
import io.github.mortuusars.salt.client.LangKeys;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class Sounds extends SoundDefinitionsProvider {
    public Sounds(DataGenerator generator, ExistingFileHelper helper) {
        super(generator.getPackOutput(), Salt.ID, helper);
    }

    @Override
    public void registerSounds() {
        add(Salt.Sounds.SALT_DISSOLVE.get(), definition()
                .subtitle(LangKeys.SUBTITLES_DISSOLVES)
                .with(multiple(6, "block/beehive/drip", 0.6f, 0.6f)));
        add(Salt.Sounds.MELT.get(), definition()
                .subtitle(LangKeys.SUBTITLES_MELTS)
                .with(multiple(8, "entity/boat/paddle_water", 1f, 1.25f)));
        add(Salt.Sounds.CAULDRON_EVAPORATE.get(), definition()
                .subtitle(LangKeys.SUBTITLES_CAULDRON_EVAPORATE)
                .with(sound("random/fizz").volume(0.9f).pitch(0.85f)));
        add(Salt.Sounds.BUBBLE_POP.get(), definition()
                .subtitle("subtitles.block.bubble_column.bubble_pop")
                .with(sound("block/bubble_column/bubble2").volume(0.4f).pitch(0.4f)));
        add(Salt.Sounds.SALT_CAULDRON_REMOVE_SALT.get(), definition()
                .with(multiple(5, "step/sand", 1.0f, 1.1f)));



        add(Salt.Sounds.SALT_BREAK.get(), definition()
                .subtitle("subtitles.block.generic.break")
                .with(multiple(5, "block/dripstone/break", 1.0f, 1.25f)));
        add(Salt.Sounds.SALT_PLACE.get(), definition()
                .subtitle("subtitles.block.generic.place")
                .with(multiple(5, "block/dripstone/break", 1.0f, 1.25f)));
        add(Salt.Sounds.SALT_STEP.get(), definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(multiple(6, "block/dripstone/step", 1.0f, 1.25f)));
        add(Salt.Sounds.SALT_HIT.get(), definition()
                .subtitle("subtitles.block.generic.hit")
                .with(multiple(6, "block/dripstone/step", 1.0f, 1.25f)));
        add(Salt.Sounds.SALT_FALL.get(), definition()
                .with(multiple(6, "block/dripstone/step", 1.0f, 1.25f)));

        add(Salt.Sounds.SALT_CLUSTER_BREAK.get(), definition()
                .subtitle("subtitles.block.generic.break")
                .with(multiple(4, "block/amethyst_cluster/break", 0.8f, 0.6f)));
        add(Salt.Sounds.SALT_CLUSTER_PLACE.get(), definition()
                .subtitle("subtitles.block.generic.place")
                .with(multiple(4, "block/amethyst_cluster/break", 0.8f, 0.8f)));
        add(Salt.Sounds.SALT_CLUSTER_STEP.get(), definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(multiple(14, "block/amethyst/step", 0.75f, 0.7f)));
        add(Salt.Sounds.SALT_CLUSTER_HIT.get(), definition()
                .subtitle("subtitles.block.generic.hit")
                .with(multiple(14, "block/amethyst/step", 0.75f, 0.7f)));
        add(Salt.Sounds.SALT_CLUSTER_FALL.get(), definition()
                .with(multiple(14, "block/amethyst/step", 0.75f, 0.7f)));

        add(Salt.Sounds.LARGE_SALT_BUD_BREAK.get(), definition()
                .subtitle("subtitles.block.generic.break")
                .with(multiple(4, "block/amethyst_cluster/break", 0.7f, 0.8f)));
        add(Salt.Sounds.LARGE_SALT_BUD_PLACE.get(), definition()
                .subtitle("subtitles.block.generic.place")
                .with(multiple(4, "block/amethyst_cluster/break", 0.7f, 0.9f)));
        add(Salt.Sounds.LARGE_SALT_BUD_STEP.get(), definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(multiple(14, "block/amethyst/step", 0.65f, 0.85f)));
        add(Salt.Sounds.LARGE_SALT_BUD_HIT.get(), definition()
                .subtitle("subtitles.block.generic.hit")
                .with(multiple(14, "block/amethyst/step", 0.65f, 0.85f)));
        add(Salt.Sounds.LARGE_SALT_BUD_FALL.get(), definition()
                .with(multiple(14, "block/amethyst/step", 0.65f, 0.85f)));

        add(Salt.Sounds.MEDIUM_SALT_BUD_BREAK.get(), definition()
                .subtitle("subtitles.block.generic.break")
                .with(multiple(4, "block/amethyst_cluster/break", 0.55f, 0.9f)));
        add(Salt.Sounds.MEDIUM_SALT_BUD_PLACE.get(), definition()
                .subtitle("subtitles.block.generic.place")
                .with(multiple(4, "block/amethyst_cluster/break", 0.55f, 0.95f)));
        add(Salt.Sounds.MEDIUM_SALT_BUD_STEP.get(), definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(multiple(14, "block/amethyst/step", 0.5f, 1f)));
        add(Salt.Sounds.MEDIUM_SALT_BUD_HIT.get(), definition()
                .subtitle("subtitles.block.generic.hit")
                .with(multiple(14, "block/amethyst/step", 0.5f, 1f)));
        add(Salt.Sounds.MEDIUM_SALT_BUD_FALL.get(), definition()
                .with(multiple(14, "block/amethyst/step", 0.5f, 1f)));

        add(Salt.Sounds.SMALL_SALT_BUD_BREAK.get(), definition()
                .subtitle("subtitles.block.generic.break")
                .with(multiple(4, "block/amethyst_cluster/break", 0.4f, 0.9f)));
        add(Salt.Sounds.SMALL_SALT_BUD_PLACE.get(), definition()
                .subtitle("subtitles.block.generic.place")
                .with(multiple(4, "block/amethyst_cluster/break", 0.4f, 1.05f)));
        add(Salt.Sounds.SMALL_SALT_BUD_STEP.get(), definition()
                .subtitle("subtitles.block.generic.footsteps")
                .with(multiple(14, "block/amethyst/step", 0.35f, 1.1f)));
        add(Salt.Sounds.SMALL_SALT_BUD_HIT.get(), definition()
                .subtitle("subtitles.block.generic.hit")
                .with(multiple(14, "block/amethyst/step", 0.353f, 1.1f)));
        add(Salt.Sounds.SMALL_SALT_BUD_FALL.get(), definition()
                .with(multiple(14, "block/amethyst/step", 0.35f, 1.1f)));
    }

    private SoundDefinition.Sound[] multiple(int count, String name, float volume, float pitch) {
        SoundDefinition.Sound[] sounds = new SoundDefinition.Sound[count];
        for (int i = 0; i < count; i++) {
            sounds[i] = sound(name + (i + 1)).volume(volume).pitch(pitch);
        }
        return sounds;
    }
}
