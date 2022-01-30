package org.nbfalcon.intellijMCTerra.terrascript.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptLanguage;

public class TesfElementFactory {
    private final Project myProject;

    public TesfElementFactory(Project project) {
        this.myProject = project;
    }

    public static TesfElementFactory getInstance(Project project) {
        return project.getService(TesfElementFactory.class);
    }

    private PsiFile createFile(String text) {
        return PsiFileFactory.getInstance(myProject).createFileFromText(
                "dummy.tesf", TerrascriptLanguage.INSTANCE, text);
    }

    public TesfVarRefExpr createVarref(String name) {
        final PsiFile src = createFile(name + ";" );
        return (TesfVarRefExpr) PsiTreeUtil.getChildOfType(src, TesfExprStmt.class).getExpr();
    }

    public PsiElement createLeaf(String text) {
        return PsiTreeUtil.getDeepestFirst(createFile(text));
    }
}
