package org.nbfalcon.intellijMCTerra.terrascript.intellisense.xref;

import com.intellij.find.findUsages.FindUsagesHandler;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.searches.ReferencesSearch;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptElementTypes;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;

import java.util.Objects;

public class TerrascriptFindUsagesHandlerFactory extends FindUsagesHandlerFactory {
    @Nullable
    public static PsiElement extractDefinition(@NotNull PsiElement element) {
        final ASTNode node = element.getNode();
        if (node == null || node.getElementType() != TerrascriptElementTypes.ID) return null;

        final PsiReference reference = element.getReference();
        if (reference != null) {
            return reference.resolve();
        } else {
            final PsiElement parent = element.getParent();
            if (parent instanceof TesfVarDeclStmt) {
                return parent;
            }
        }
        return null;
    }

    @Override
    public boolean canFindUsages(@NotNull PsiElement element) {
        return extractDefinition(element) != null;
    }

    @Override
    public @Nullable FindUsagesHandler createFindUsagesHandler(@NotNull PsiElement element, boolean forHighlightUsages) {
        final PsiElement elementF = extractDefinition(element);
        if (elementF == null) return null;
        return new FindUsagesHandler(elementF) {
            @Override
            public PsiElement @NotNull [] getSecondaryElements() {
                return ReferencesSearch.search(elementF, GlobalSearchScope.fileScope(elementF.getContainingFile()))
                        .findAll().stream()
                        .map(PsiReference::resolve).filter(Objects::nonNull)
                        .toArray(PsiElement[]::new);
            }
        };
    }
}
