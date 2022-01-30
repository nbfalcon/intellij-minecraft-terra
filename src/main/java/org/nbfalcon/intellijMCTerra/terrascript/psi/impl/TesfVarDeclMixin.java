package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.PsiNamedElement;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptElementTypes;

public class TesfVarDeclMixin extends ASTWrapperPsiElement implements PsiNamedElement, PsiNameIdentifierOwner {
    public TesfVarDeclMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public String getName() {
        final PsiElement id = getNameIdentifier();
        return id == null ? null : id.getText();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return findChildByType(TerrascriptElementTypes.ID);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null; // FIXME
    }
}
