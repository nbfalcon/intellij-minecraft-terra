package org.nbfalcon.intellijMCTerra.terrascript.intellisense.syntaxHighlighting;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.lang.annotation.HighlightSeverity;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.nbfalcon.intellijMCTerra.terrascript.intellisense.completion.functions.TesfBuiltinFunctions;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfFuncRefCallee;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarRefExpr;

public class TerrascriptAnnotator implements Annotator {
    @Override
    public void annotate(@NotNull PsiElement element, @NotNull AnnotationHolder holder) {
        if (element instanceof TesfFuncRefCallee) {
            final TextAttributesKey key =
                    TesfBuiltinFunctions.getInstance().getDescription(element.getText()) != null
                            ? TerrascriptSyntaxHighlighter.BUILTIN_FUNCTION
                            : TerrascriptSyntaxHighlighter.UNKNOWN_FUNCTION;
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element)
                    .textAttributes(key)
                    .create();
        } else if (element instanceof TesfVarRefExpr) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                    .range(element)
                    .textAttributes(TerrascriptSyntaxHighlighter.VAR_REF)
                    .create();
        } else if (element instanceof TesfVarDeclStmt) {
            final PsiElement name = ((TesfVarDeclStmt) element).getNameIdentifier();
            if (name != null) {
                holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(name)
                        .textAttributes(TerrascriptSyntaxHighlighter.VARIABLE_DECL)
                        .create();
            }
        }
    }
}
