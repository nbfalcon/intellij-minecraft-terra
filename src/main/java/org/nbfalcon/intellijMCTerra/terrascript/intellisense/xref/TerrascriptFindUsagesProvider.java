package org.nbfalcon.intellijMCTerra.terrascript.intellisense.xref;

import com.intellij.lang.cacheBuilder.DefaultWordsScanner;
import com.intellij.lang.cacheBuilder.WordsScanner;
import com.intellij.lang.findUsages.FindUsagesProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptParserDefinition;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;

import java.util.Objects;

public class TerrascriptFindUsagesProvider implements FindUsagesProvider {
    @Override
    public boolean canFindUsagesFor(@NotNull PsiElement psiElement) {
        return psiElement instanceof PsiNamedElement;
    }

    @Override
    public @Nullable WordsScanner getWordsScanner() {
        return new DefaultWordsScanner(TerrascriptParserDefinition.getLexer(),
                TerrascriptParserDefinition.IDENTIFIER_TOKENS,
                TerrascriptParserDefinition.COMMENT_TOKENS,
                TerrascriptParserDefinition.STRING_TOKENS);
    }

    @Override
    public @Nullable @NonNls String getHelpId(@NotNull PsiElement psiElement) {
        return null; // FIXME
    }

    @Override
    public @Nls @NotNull String getType(@NotNull PsiElement element) {
        return "variable";
    }

    @Override
    public @Nls @NotNull String getDescriptiveName(@NotNull PsiElement element) {
        if (element instanceof TesfVarDeclStmt) element = ((TesfVarDeclStmt) element).getId();
        return Objects.requireNonNull(element).getText();
    }

    @Override
    public @Nls @NotNull String getNodeText(@NotNull PsiElement element, boolean useFullName) {
        return element.getText().replaceAll("\\s+", " ");
    }
}
