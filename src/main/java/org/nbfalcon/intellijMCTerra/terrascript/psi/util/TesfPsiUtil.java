package org.nbfalcon.intellijMCTerra.terrascript.psi.util;

import com.intellij.psi.PsiNamedElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfType;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TesfVarDeclStmt;

public class TesfPsiUtil {
    public static @Nullable VarType getType(TesfVarDeclStmt decl) {
        final TesfType type = decl.getType();
        if (type.getBoolTy() != null) return VarType.BOOL;
        else if (type.getNumTy() != null) return VarType.NUM;
        else if (type.getStrTy() != null) return VarType.STR;
        return null;
    }
    
    public enum VarType {
        BOOL, NUM, STR;
    }

    public static @NotNull String getNameSafe(PsiNamedElement element) {
        final String name = element.getName();
        return name == null ? "" : name;
    }
}
