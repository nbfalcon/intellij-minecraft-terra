package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class TerrascriptSymbolTable {
    private final Multimap<String, PsiElement> symbols = MultimapBuilder.linkedHashKeys().arrayListValues(1).build();

    public static TerrascriptSymbolTable fromBlock(PsiElement block) {
        TerrascriptSymbolTable table = new TerrascriptSymbolTable();
        for (TesfVarDeclStmt decl : PsiTreeUtil.getChildrenOfTypeAsList(block, TesfVarDeclStmt.class)) {
            final String name = decl.getName();
            if (name != null) {
                table.symbols.put(name, decl);
            }
        }
        return table;
    }

    public @Nullable Collection<PsiElement> resolveAll(String identifier) {
        final Collection<PsiElement> resolvedTo = symbols.get(identifier);
        return resolvedTo.isEmpty() ? null : resolvedTo;
    }

    public @Nullable PsiElement resolveCanonical(String identifier) {
        final Collection<PsiElement> resolvedTo = symbols.get(identifier);
        return resolvedTo.isEmpty() ? null : resolvedTo.iterator().next();
    }

    public void addCompletions(List<LookupElement> target, Set<String> shadowed) {
        for (String key : symbols.keys()) {
            if (!shadowed.contains(key)) {
                target.add(new LookupElement() {
                    @Override
                    public @NotNull String getLookupString() {
                        return key;
                    }
                });
                shadowed.add(key);
            }
        }
    }
}
