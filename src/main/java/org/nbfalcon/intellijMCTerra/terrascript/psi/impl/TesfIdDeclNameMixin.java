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

public class TesfIdDeclNameMixin extends ASTWrapperPsiElement implements PsiNamedElement, PsiNameIdentifierOwner {
    public TesfIdDeclNameMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return findChildByType(TerrascriptElementTypes.VAR_DECL_STMT);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        return null; // FIXME
    }
}
