package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jetbrains.annotations.NotNull;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfDeclarationScope;

public class TesfDeclarationScopeMixin extends ASTWrapperPsiElement implements TesfDeclarationScope {
    private TerrascriptSymbolTable lazySymbolTable;

    public TesfDeclarationScopeMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull TerrascriptSymbolTable getSymbolTable() {
        if (lazySymbolTable == null) lazySymbolTable = TerrascriptSymbolTable.fromBlock(this);
        return lazySymbolTable;
    }

    @Override
    public void subtreeChanged() {
        super.subtreeChanged();
        lazySymbolTable = null;
    }
}
