package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfDeclarationScope;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfElementFactory;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;

import java.util.Objects;

public class TesfVarRefMixin extends ASTWrapperPsiElement implements PsiPolyVariantReference {
    public TesfVarRefMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public ResolveResult @NotNull [] multiResolve(boolean incompleteCode) {
        final TesfDeclarationScope scope = getScope();
        if (scope == null) return ResolveResult.EMPTY_ARRAY;
        return TesfDeclarationScope.resolveAll(scope, getName(), this);
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
        final TesfDeclarationScope scope = getScope();
        if (scope == null) return null;
        return TesfDeclarationScope.resolveCanonical(scope, getName());
    }

    @Override
    public Object @NotNull [] getVariants() {
        final TesfDeclarationScope scope = getScope();
        return TesfDeclarationScope.getCompletions(scope);
    }

    @Nullable
    private TesfDeclarationScope getScope() {
        return PsiTreeUtil.getParentOfType(this, TesfDeclarationScope.class);
    }

    @Override
    public PsiReference getReference() {
        return this;
    }

    @Override
    public @NotNull @NlsSafe String getCanonicalText() {
        return getElement().getText();
    }

    @Override
    public PsiElement handleElementRename(@NotNull String newElementName) throws IncorrectOperationException {
        return replace(TesfElementFactory.getInstance(getProject()).createVarref(newElementName));
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement element) throws IncorrectOperationException {
        final String name = ((PsiNamedElement) element).getName();
        return name == null ? null : handleElementRename(name);
    }

    @Override
    public boolean isReferenceTo(@NotNull PsiElement element) {
        final PsiElement resolved = resolve();
        if (resolved == null) return false;
        return Objects.equals(
                PsiTreeUtil.getParentOfType(element, TesfVarDeclStmt.class),
                PsiTreeUtil.getParentOfType(resolved, TesfVarDeclStmt.class));
    }

    @Override
    public boolean isSoft() {
        return false;
    }

    @Override
    public String getName() {
        return getCanonicalText();
    }
}
