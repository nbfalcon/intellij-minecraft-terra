package org.nbfalcon.intellijMCTerra.terrascript.intellisense.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiNameIdentifierOwner;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfDeclarationScope;

import java.util.HashMap;
import java.util.Map;

public class TesfDuplicateVariableInspection extends LocalInspectionTool {
    @Override
    public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);
        Map<String, PsiElement> knownAtThisScope = new HashMap<>();
        for (TesfDeclarationScope scope :
                SyntaxTraverser.psiTraverser(file).bfsTraversal().filter(TesfDeclarationScope.class)) {
            for (Map.Entry<String, PsiElement> entry : scope.getSymbolTable().getSymbols().entries()) {
                final String name = entry.getKey();
                final PsiElement decl = entry.getValue();
                if (knownAtThisScope.putIfAbsent(name, decl) != null) {
                    final PsiElement id = ((PsiNameIdentifierOwner) decl).getIdentifyingElement();
                    holder.registerProblem(
                            id == null ? decl : id,
                            "Variable '" + name + "' is already defined" );
                }
            }
            knownAtThisScope.clear();
        }
        return holder.getResultsArray();
    }
}
