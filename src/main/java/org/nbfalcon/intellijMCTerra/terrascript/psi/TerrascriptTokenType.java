package org.nbfalcon.intellijMCTerra.terrascript.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptLanguage;

public class TerrascriptTokenType extends IElementType {
    public TerrascriptTokenType(@NotNull @NonNls String debugName) {
        super(debugName, TerrascriptLanguage.INSTANCE);
    }
}