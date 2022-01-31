package org.nbfalcon.intellijMCTerra.terrascript.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfStmt;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;

public class TesfForDeclarationScopeMixin extends TesfDeclarationScopeMixin {
    public TesfForDeclarationScopeMixin(@NotNull ASTNode node) {
        super(node);
    }

    @Override
    public @NotNull TerrascriptSymbolTable getSymbolTable() {
        if (lazySymbolTable == null) {
            lazySymbolTable = new TerrascriptSymbolTable();
            final TesfStmt decl = PsiTreeUtil.getChildOfType(this, TesfStmt.class);
            if (decl instanceof TesfVarDeclStmt) {
                lazySymbolTable.registerVarDecl((TesfVarDeclStmt) decl);
            }
        }
        return lazySymbolTable;
    }
}
