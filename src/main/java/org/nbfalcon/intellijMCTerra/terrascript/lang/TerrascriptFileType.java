package org.nbfalcon.intellijMCTerra.terrascript.lang;

import com.intellij.lang.Language;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsContexts;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.tree.IFileElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class TerrascriptFileType extends LanguageFileType {
    public static final TerrascriptFileType INSTANCE = new TerrascriptFileType();

    private TerrascriptFileType() {
        super(TerrascriptLanguage.INSTANCE);
    }

    @Override
    public @NonNls @NotNull String getName() {
        return "Terrascript File";
    }

    @Override
    public @NlsContexts.Label @NotNull String getDescription() {
        return "Minecraft Terra world generation scripting file";
    }

    @Override
    public @NlsSafe @NotNull String getDefaultExtension() {
        return ".tesf";
    }

    @Override
    public @Nullable Icon getIcon() {
        // FIXME
        return null;
    }
}
