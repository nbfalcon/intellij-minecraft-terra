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
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfFuncallExpr;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.util.TesfPsiUtil;
import org.nbfalcon.intellijMCTerra.util.Util;

import java.util.List;

public class TesfTypeCheckerInspection extends LocalInspectionTool {
    @Override
    public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);
        final TesfPsiUtil.TypeErrorConsumer onTypeError = new TesfPsiUtil.TypeErrorConsumer() {
            @Override
            public void onTypeError(TesfExpr element, TesfPsiUtil.TesfType expected, TesfPsiUtil.TesfType actual) {
                holder.registerProblem(element, "Expected '" + expected + "'" + ", but got: '" + actual + "'");
            }

            @Override
            public void onUnexpectedArgument(TesfExpr argument) {
                holder.registerProblem(argument, "Unexpected argument");
            }

            @Override
            public void onMissingArguments(TesfFuncallExpr expr, List<String> arguments) {
                final PsiElement where = Util.firstNonNull(expr.getRpar(), expr);
                assert where != null;
                holder.registerProblem(where, "Missing arguments: " + String.join(", ", arguments));
            }
        };
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
