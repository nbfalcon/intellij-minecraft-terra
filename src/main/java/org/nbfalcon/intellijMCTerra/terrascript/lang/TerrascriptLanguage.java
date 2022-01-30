package org.nbfalcon.intellijMCTerra.terrascript.lang;

import com.intellij.lang.Language;

public class TerrascriptLanguage extends Language {
    public static final TerrascriptLanguage INSTANCE = new TerrascriptLanguage();

    private TerrascriptLanguage() {
        super("Minecraft.Terrascript");
    }
}
