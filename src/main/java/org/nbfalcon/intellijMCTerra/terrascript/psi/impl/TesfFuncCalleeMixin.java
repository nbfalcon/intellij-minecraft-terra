package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.intellisense.completion.functions.TesfBuiltinFunctions;

// Is a reference only for the sake of getVariants currently
public class TesfFuncCalleeMixin extends ASTWrapperPsiElement implements PsiReference {
    public TesfFuncCalleeMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @Override
    public @NotNull PsiElement getElement() {
        return this;
    }

    @Override
    public @NotNull TextRange getRangeInElement() {
        return new TextRange(0, getTextLength());
    }

    @Override
    public @Nullable PsiElement resolve() {
        return null;
    }

    @Override
    public @NotNull @NlsSafe String getCanonicalText() {
        return getText();
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        return null;
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        return false;
    }

    @Override
    public Object @NotNull [] getVariants() {
        return TesfBuiltinFunctions.getInstance().getCompletions();
    }

    @Override
    public boolean isSoft() {
        return false;
    }
}
