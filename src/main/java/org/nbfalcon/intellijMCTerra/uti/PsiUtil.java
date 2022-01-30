package org.nbfalcon.intellijMCTerra.uti;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.util.Consumer;
import org.jetbrains.annotations.NotNull;

public class PsiUtil {
    public static void visitChildrenRecursively(PsiElement visitMe, Consumer<PsiElement> onElement) {
        visitMe.acceptChildren(new PsiElementVisitor() {
            @Override
            public void visitElement(@NotNull PsiElement element) {
                super.visitElement(element);
                onElement.consume(element);
                element.acceptChildren(this);
            }
        });
    }
}
