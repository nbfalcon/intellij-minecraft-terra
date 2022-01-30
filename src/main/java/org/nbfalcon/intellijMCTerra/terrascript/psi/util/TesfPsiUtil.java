package org.nbfalcon.intellijMCTerra.terrascript.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiWhiteSpace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.psi.*;

public class TesfPsiUtil {
    public static @Nullable VarType getType(TesfVarDeclStmt decl) {
        final TesfType type = decl.getType();
        if (type.getBoolTy() != null) return VarType.BOOL;
        else if (type.getNumTy() != null) return VarType.NUM;
        else if (type.getStrTy() != null) return VarType.STR;
        return null;
    }

    public static @Nullable TesfPsiUtil.VarType inferType(@Nullable TesfExpr expr) {
        if (expr == null) return null;
        if (expr instanceof TesfAddBinexpr) {
            TesfAddBinexpr bexpr = (TesfAddBinexpr) expr;
            if (bexpr.getAdd() != null) {
                boolean str = inferType(bexpr.getLeft()) != TesfPsiUtil.VarType.STR
                        && bexpr.getRight() != null && inferType(bexpr.getRight()) == TesfPsiUtil.VarType.STR;
                if (str) {
                    return TesfPsiUtil.VarType.STR;
                }
            }
        }
        if (expr instanceof TesfAddBinexpr || expr instanceof TesfMulBinexpr) {
            return TesfPsiUtil.VarType.NUM;
        } else if (expr instanceof TesfOrBinexpr || expr instanceof TesfAndBinexpr) {
            return VarType.BOOL;
        } else if (expr instanceof TesfAssignmentBinexpr) {
            return inferType(((TesfAssignmentBinexpr) expr).getRight());
        } else if (expr instanceof TesfNegExpr) {
            return ((TesfNegExpr) expr).getNeg() != null ? TesfPsiUtil.VarType.BOOL : TesfPsiUtil.VarType.NUM;
        } else if (expr instanceof TesfLiteralExpr) {
            if (((TesfLiteralExpr) expr).getNumber() != null) return VarType.NUM;
            else if (((TesfLiteralExpr) expr).getBoolLit() != null) return VarType.BOOL;
            else return VarType.STR;
        } else if (expr instanceof TesfVarRefExpr) {
            final PsiElement decl = ((TesfVarRefExpr) expr).resolve();
            return decl == null ? null : getType((TesfVarDeclStmt) decl);
        } else if (expr instanceof TesfFuncallExpr) {
            return null; // FIXME: we can't infer type yet
        }
        throw new AssertionError("Unhandled expression type: '" + expr.getClass() + "'; this is a bug in the plugin");
    }

    public static @NotNull String getNameSafe(PsiNamedElement element) {
        final String name = element.getName();
        return name == null ? "" : name;
    }

    private static void expectType(@Nullable TesfExpr expr,
                                   @Nullable TesfPsiUtil.VarType expected,
                                   TypeErrorConsumer onTypeError) {
        if (expr != null && expected != null) {
            final VarType actual = inferType(expr);
            if (expected != actual) {
                onTypeError.onTypeError(expr, expected, actual);
            }
        }
    }

    public static void checkTypes1(TesfExpr expr, TypeErrorConsumer onTypeError) {
        if ((expr instanceof TesfAddBinexpr && inferType(expr) != VarType.STR)
                || expr instanceof TesfMulBinexpr
                || expr instanceof TesfGreatherThanBinexpr) {
            expectType(((TesfBinexpr) expr).getLeft(), VarType.NUM, onTypeError);
            expectType(((TesfBinexpr) expr).getRight(), VarType.NUM, onTypeError);
        } else if (expr instanceof TesfOrBinexpr || expr instanceof TesfAndBinexpr) {
            expectType(((TesfBinexpr) expr).getLeft(), VarType.BOOL, onTypeError);
            expectType(((TesfBinexpr) expr).getRight(), VarType.BOOL, onTypeError);
        } else if (expr instanceof TesfEqualsBinexpr) {
            final VarType left = inferType(((TesfEqualsBinexpr) expr).getLeft());
            expectType(((TesfEqualsBinexpr) expr).getRight(), left, onTypeError);
        } else if (expr instanceof TesfNegExpr) {
            expectType(((TesfNegExpr) expr).getExpr(), inferType(expr), onTypeError);
        } else if (expr instanceof TesfAssignmentBinexpr) {
            // Don't do expensive resolution if we can't even check anything in the first place
            if (((TesfAssignmentBinexpr) expr).getRight() != null) {
                final TesfVarRefExpr lhs = (TesfVarRefExpr) ((TesfAssignmentBinexpr) expr).getLeft();
                final PsiElement target = lhs.resolve();
                if (target != null) {
                    final VarType type = getType((TesfVarDeclStmt) target);
                    if (type != null) {
                        expectType(((TesfAssignmentBinexpr) expr).getRight(), type, onTypeError);
                    }
                }
            }
        } else if (expr instanceof TesfFuncallExpr) {
            // FIXME
        } else if (!(expr instanceof TesfLiteralExpr || expr instanceof TesfVarRefExpr)) {
            throw new AssertionError("Unhandled expression type: '" + expr.getClass() + "'; this is a bug in the plugin");
        }
    }

    public static void checkTypes1(TesfStmt stmt, TypeErrorConsumer onTypeError) {
        if (stmt instanceof TesfVarDeclStmt) {
            expectType(((TesfVarDeclStmt) stmt).getExpr(), getType((TesfVarDeclStmt) stmt), onTypeError);
        } else if (stmt instanceof TesfIfKwStmt) {
            expectType(((TesfIfKwStmt) stmt).getExpr(), VarType.BOOL, onTypeError);
        } else if (stmt instanceof TesfWhileKwStmt) {
            expectType(((TesfWhileKwStmt) stmt).getExpr(), VarType.BOOL, onTypeError);
        } else if (stmt instanceof TesfForKwStmt) {
            expectType(getForMiddleExpr((TesfForKwStmt) stmt), VarType.BOOL, onTypeError);
        }
    }

    public static @Nullable TesfExpr getForMiddleExpr(TesfForKwStmt forStmt) {
        final PsiElement semi = forStmt.getSemi();
        if (semi != null) {
            PsiElement sibling = semi.getNextSibling();
            if (sibling instanceof PsiWhiteSpace) sibling = sibling.getNextSibling();
            if (sibling instanceof TesfExpr) {
                return (TesfExpr) sibling;
            }
        }
        return null;
    }

    public enum VarType {
        BOOL, NUM, STR;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    @FunctionalInterface
    public interface TypeErrorConsumer {
        void onTypeError(TesfExpr element, TesfPsiUtil.VarType expected, TesfPsiUtil.VarType actual);
    }
}
