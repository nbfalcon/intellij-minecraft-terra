package org.nbfalcon.intellijMCTerra.terrascript.intellisense.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarRefExpr;
import org.nbfalcon.intellijMCTerra.terrascript.psi.util.TesfPsiUtil;

import java.util.HashSet;
import java.util.Set;

public class TesfUnusedVariableInspection extends LocalInspectionTool {
    @Override
    public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);
        Set<PsiElement> unusedDecls = new HashSet<>();
        for (PsiElement element : SyntaxTraverser.psiTraverser(file).preOrderDfsTraversal()) {
            if (element instanceof TesfVarDeclStmt) {
                unusedDecls.add(element);
            } else if (element instanceof TesfVarRefExpr) {
                unusedDecls.remove(((TesfVarRefExpr) element).resolve());
            }
        }
        for (PsiElement unusedDecl : unusedDecls) {
            final TesfVarDeclStmt decl = (TesfVarDeclStmt) unusedDecl;
            final PsiElement id = decl.getNameIdentifier();
            holder.registerProblem(id != null ? id : decl,
                    "Unused variable: " + "'" + TesfPsiUtil.getNameSafe(decl) + "'" );
        }
        return holder.getResultsArray();
    }
}
