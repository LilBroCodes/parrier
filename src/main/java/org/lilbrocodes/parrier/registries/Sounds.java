package org.lilbrocodes.parrier.registries;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.lilbrocodes.parrier.Parrier;

public class Sounds {
    public static final SoundEvent PARRY = Registry.register(Registries.SOUND_EVENT, Identifier.of(Parrier.MOD_ID, "parry"), SoundEvent.of(Identifier.of(Parrier.MOD_ID, "parry")));

    public static void register() {

    }
}
