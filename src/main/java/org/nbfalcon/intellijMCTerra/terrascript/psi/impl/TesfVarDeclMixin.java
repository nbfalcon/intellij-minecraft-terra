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
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfElementFactory;

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
    public int getTextOffset() {
        final PsiElement id = getNameIdentifier();
        if (id != null) {
            return id.getTextOffset();
        }
        return super.getTextOffset();
    }

    @Override
    public @Nullable PsiElement getNameIdentifier() {
        return findChildByType(TerrascriptElementTypes.ID);
    }

    @Override
    public PsiElement setName(@NlsSafe @NotNull String name) throws IncorrectOperationException {
        final PsiElement id = getNameIdentifier();
        if (id != null) {
            id.replace(TesfElementFactory.getInstance(getProject()).createLeaf(name));
        }
        return this;
    }
}
