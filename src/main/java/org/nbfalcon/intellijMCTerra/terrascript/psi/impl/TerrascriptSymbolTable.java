package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.icons.AllIcons;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.util.TesfPsiUtil;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
        for (Map.Entry<String, PsiElement> entry : symbols.entries()) {
            String key = entry.getKey();
            if (shadowed.add(key)) {
                LookupElementBuilder item = LookupElementBuilder.create(key);
                final TesfPsiUtil.TesfType type = TesfPsiUtil.getType((TesfVarDeclStmt) entry.getValue());
                if (type != null) {
                    switch (type) {
                        case BOOL:
                            item = item.withIcon(AllIcons.Nodes.Interface);
                            break;
                        case NUM:
                            item = item.withIcon(AllIcons.Nodes.Gvariable);
                            break;
                        case STR:
                            item = item.withIcon(AllIcons.Nodes.Field);
                            break;
                    }
                }
                target.add(item);
            }
        }
    }

    public Multimap<String, PsiElement> getSymbols() {
        return symbols;
    }
}
