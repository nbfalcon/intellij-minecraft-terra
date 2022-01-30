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
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfExpr;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.util.TesfPsiUtil;

public class TesfTypeCheckerInspection extends LocalInspectionTool {
    @Override
    public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);
        final TesfPsiUtil.TypeErrorConsumer onTypeError = (element, expected, actual) ->
                holder.registerProblem(element, "Expected '" + expected + "'" + ", but got: '" + actual + "'");
        for (PsiElement child : SyntaxTraverser.psiTraverser(file).bfsTraversal()) {
            if (child instanceof TesfExpr) {
                TesfPsiUtil.checkTypes1((TesfExpr) child, onTypeError);
            } else if (child instanceof TesfStmt) {
                TesfPsiUtil.checkTypes1((TesfStmt) child, onTypeError);
            }
        }
        return holder.getResultsArray();
    }
}
