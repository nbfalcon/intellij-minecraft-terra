package org.nbfalcon.intellijMCTerra.terrascript.psi;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.impl.TerrascriptSymbolTable;

import java.util.*;

public interface TerrascriptDeclarationScope extends PsiElement {
    static @Nullable PsiElement resolveCanonical(TerrascriptDeclarationScope scope, String identifier) {
        while (scope != null) {
            final PsiElement resolvedTo = scope.getSymbolTable().resolveCanonical(identifier);
            if (resolvedTo != null) {
                return resolvedTo;
            }
            scope = scope.getParentScope();
        }
        return null;
    }

    static ResolveResult @NotNull [] resolveAll(TerrascriptDeclarationScope scope, String identifier,
                                                PsiElement context) {
        while (scope != null) {
            final Collection<PsiElement> resolvedTo = scope.getSymbolTable().resolveAll(identifier);
            if (resolvedTo != null) {
                ResolveResult[] results = new ResolveResult[resolvedTo.size()];
                int wi = 0;
                for (PsiElement psiElement : resolvedTo) {
                    boolean valid = wi == 0 && psiElement.getTextOffset() < context.getTextOffset();
                    results[wi] = new PsiElementResolveResult(psiElement, valid);
                    wi++;
                }
                return results;
            }
            scope = scope.getParentScope();
        }
        return ResolveResult.EMPTY_ARRAY;
    }

    static LookupElement[] getCompletions(TerrascriptDeclarationScope scope) {
        List<LookupElement> completions = new ArrayList<>();
        Set<String> shadowed = new HashSet<>();
        while (scope != null) {
            scope.getSymbolTable().addCompletions(completions, shadowed);
            scope = scope.getParentScope();
        }
        return completions.toArray(LookupElement.EMPTY_ARRAY);
    }

    @NotNull TerrascriptSymbolTable getSymbolTable();

    default @Nullable TerrascriptDeclarationScope getParentScope() {
        return PsiTreeUtil.getParentOfType(this, TerrascriptDeclarationScope.class, true);
    }
}
