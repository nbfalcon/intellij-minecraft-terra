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
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfBreakKwStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfForKwStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfWhileKwStmt;
import org.nbfalcon.intellijMCTerra.util.Util;

import java.util.Stack;

public class TesfBreakOutsideLoop extends LocalInspectionTool {
    @Override
    public ProblemDescriptor @Nullable [] checkFile(@NotNull PsiFile file, @NotNull InspectionManager manager, boolean isOnTheFly) {
        ProblemsHolder holder = new ProblemsHolder(manager, file, isOnTheFly);
        Stack<PsiElement> loopStack = new Stack<>();
        for (PsiElement psiElement : SyntaxTraverser.psiTraverser(file).biOrderDfsTraversal()) {
            if (psiElement instanceof TesfBreakKwStmt) {
                if (loopStack.isEmpty()) {
                    PsiElement whichEl = Util.firstNonNull(
                            ((TesfBreakKwStmt) psiElement).getBreak(),
                            ((TesfBreakKwStmt) psiElement).getContinue());
                    String which = whichEl != null ? whichEl.getText() : "break";
                    holder.registerProblem(psiElement, which + " must be inside a loop");
                }
            } else if (psiElement instanceof TesfForKwStmt || psiElement instanceof TesfWhileKwStmt) {
                // Are we traversing backwards?
                if (!loopStack.isEmpty() && psiElement == loopStack.peek()) {
                    loopStack.pop();
                } else {
                    loopStack.push(psiElement);
                }
            }
        }
        return holder.getResultsArray();
    }
}
