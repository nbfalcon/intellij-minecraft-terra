package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptFileType;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptLanguage;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TerrascriptDeclarationScope;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TerrascriptFile;

public class TerrascriptFileImpl extends PsiFileBase implements TerrascriptFile {
    private TerrascriptSymbolTable lazySymbolTable = null;

    public TerrascriptFileImpl(@NotNull FileViewProvider viewProvider) {
        super(viewProvider, TerrascriptLanguage.INSTANCE);
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        lazySymbolTable = null;
    }

    @Override
    public String toString() {
        return "Terrascript File";
    }

    @Override
    public @NotNull FileType getFileType() {
        return TerrascriptFileType.INSTANCE;
    }

    @Override
    public @NotNull TerrascriptSymbolTable getSymbolTable() {
        if (lazySymbolTable == null) lazySymbolTable = TerrascriptSymbolTable.fromBlock(this);
        return lazySymbolTable;
    }

    @Override
    public @Nullable TerrascriptDeclarationScope getParentScope() {
        return null;
    }
}
