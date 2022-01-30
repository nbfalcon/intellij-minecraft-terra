package org.nbfalcon.intellijMCTerra.terrascript.intellisense.inspections;

import com.intellij.codeInspection.InspectionManager;
import com.intellij.codeInspection.LocalInspectionTool;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.psi.PsiFile;
import com.intellij.psi.SyntaxTraverser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarRefExpr;

public class TesfUnknownVariableInspection extends LocalInspectionTool {
    @Override
    public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);
        for (TesfVarRefExpr ref :
                SyntaxTraverser.psiTraverser(file).bfsTraversal().filter(TesfVarRefExpr.class)) {
            if (ref.resolve() == null) {
                holder.registerProblem(ref, "Cannot resolve variable '" + ref.getCanonicalText() + "'" );
            }
        }
        return holder.getResultsArray();
    }
}
