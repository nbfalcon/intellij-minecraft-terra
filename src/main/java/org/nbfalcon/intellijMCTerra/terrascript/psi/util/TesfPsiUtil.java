package org.nbfalcon.intellijMCTerra.terrascript.psi.util;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiNamedElement;
import com.intellij.psi.PsiWhiteSpace;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.nbfalcon.intellijMCTerra.terrascript.intellisense.completion.functions.FunctionDescription;
import org.nbfalcon.intellijMCTerra.terrascript.intellisense.completion.functions.TesfBuiltinFunctions;
import org.nbfalcon.intellijMCTerra.terrascript.psi.*;

import java.util.ArrayList;
import java.util.List;

public class TesfPsiUtil {
    public static @Nullable TesfPsiUtil.TesfType getType(TesfVarDeclStmt decl) {
        final org.nbfalcon.intellijMCTerra.terrascript.psi.TesfType type = decl.getType();
        if (type.getBoolTy() != null) return TesfType.BOOL;
        else if (type.getNumTy() != null) return TesfType.NUM;
        else if (type.getStrTy() != null) return TesfType.STR;
        return null;
    }

    public static @Nullable TesfPsiUtil.TesfType inferType(@Nullable TesfExpr expr) {
        if (expr == null) return null;
        if (expr instanceof TesfAddBinexpr) {
            TesfAddBinexpr bexpr = (TesfAddBinexpr) expr;
            if (bexpr.getAdd() != null) {
                boolean str = inferType(bexpr.getLeft()) != TesfType.STR
                        && bexpr.getRight() != null && inferType(bexpr.getRight()) == TesfType.STR;
                if (str) {
                    return TesfType.STR;
                }
            }
        }
        if (expr instanceof TesfAddBinexpr || expr instanceof TesfMulBinexpr) {
            return TesfType.NUM;
        } else if (expr instanceof TesfOrBinexpr || expr instanceof TesfAndBinexpr
                || expr instanceof TesfEqualsBinexpr || expr instanceof TesfGreatherThanBinexpr) {
            return TesfType.BOOL;
        } else if (expr instanceof TesfAssignmentBinexpr) {
            return inferType(((TesfAssignmentBinexpr) expr).getRight());
        } else if (expr instanceof TesfNegExpr) {
            return ((TesfNegExpr) expr).getNeg() != null ? TesfType.BOOL : TesfType.NUM;
        } else if (expr instanceof TesfLiteralExpr) {
            if (((TesfLiteralExpr) expr).getNumber() != null) return TesfType.NUM;
            else if (((TesfLiteralExpr) expr).getBoolLit() != null) return TesfType.BOOL;
            else return TesfType.STR;
        } else if (expr instanceof TesfVarRefExpr) {
            final PsiElement decl = ((TesfVarRefExpr) expr).resolve();
            return decl == null ? null : getType((TesfVarDeclStmt) decl);
        } else if (expr instanceof TesfFuncallExpr) {
            final TesfFuncRefCallee callee = ((TesfFuncallExpr) expr).getFuncRefCallee();
            final String name = callee.getText();
            final FunctionDescription desc = TesfBuiltinFunctions.getInstance().getDescription(name);
            return desc == null ? null : desc.returnType;
        }
        else if (expr instanceof TesfParenExpr) {
            return inferType(((TesfParenExpr) expr).getExpr());
        }
        throw new AssertionError("Unhandled expression type: '" + expr.getClass() + "'; this is a bug in the plugin");
    }

    public static @NotNull String getNameSafe(PsiNamedElement element) {
        final String name = element.getName();
        return name == null ? "" : name;
    }

    private static void expectType(@Nullable TesfExpr expr,
                                   @Nullable TesfPsiUtil.TesfType expected,
                                   TypeErrorConsumer onTypeError) {
        if (expr != null && expected != null) {
            final TesfType actual = inferType(expr);
            // If we can't infer the type, there shouldn't be an error (we can't do anything)
            if (actual != null && expected != actual) {
                onTypeError.onTypeError(expr, expected, actual);
            }
        }
    }

    public static void checkTypes1(TesfExpr expr, TypeErrorConsumer onTypeError) {
        if ((expr instanceof TesfAddBinexpr && inferType(expr) != TesfType.STR)
                || expr instanceof TesfMulBinexpr
                || expr instanceof TesfGreatherThanBinexpr) {
            expectType(((TesfBinexpr) expr).getLeft(), TesfType.NUM, onTypeError);
            expectType(((TesfBinexpr) expr).getRight(), TesfType.NUM, onTypeError);
        } else if (expr instanceof TesfOrBinexpr || expr instanceof TesfAndBinexpr) {
            expectType(((TesfBinexpr) expr).getLeft(), TesfType.BOOL, onTypeError);
            expectType(((TesfBinexpr) expr).getRight(), TesfType.BOOL, onTypeError);
        } else if (expr instanceof TesfEqualsBinexpr) {
            final TesfType left = inferType(((TesfEqualsBinexpr) expr).getLeft());
            expectType(((TesfEqualsBinexpr) expr).getRight(), left, onTypeError);
        } else if (expr instanceof TesfNegExpr) {
            expectType(((TesfNegExpr) expr).getExpr(), inferType(expr), onTypeError);
        } else if (expr instanceof TesfAssignmentBinexpr) {
            // Don't do expensive resolution if we can't even check anything in the first place
            if (((TesfAssignmentBinexpr) expr).getRight() != null) {
                final TesfVarRefExpr lhs = (TesfVarRefExpr) ((TesfAssignmentBinexpr) expr).getLeft();
                final PsiElement target = lhs.resolve();
                if (target != null) {
                    final TesfType type = getType((TesfVarDeclStmt) target);
                    if (type != null) {
                        expectType(((TesfAssignmentBinexpr) expr).getRight(), type, onTypeError);
                    }
                }
            }
        } else if (expr instanceof TesfFuncallExpr) {
            final String name = ((TesfFuncallExpr) expr).getFuncRefCallee().getText();
            final FunctionDescription desc = TesfBuiltinFunctions.getInstance().getDescription(name);
            if (desc != null) {
                final TesfCallArglist args = ((TesfFuncallExpr) expr).getCallArglist();
                final List<TesfExpr> argExprs = args.getExprList();
                int i = 0;
                for (TesfExpr arg : argExprs) {
                    if (i < desc.args.length) {
                        expectType(arg, desc.args[i].type, onTypeError);
                    } else {
                        if (desc.varArg != null) {
                            expectType(arg, desc.varArg.type, onTypeError);
                        }
                        else {
                            onTypeError.onUnexpectedArgument(expr);
                        }
                    }
                    i++;
                }
                if (argExprs.size() < desc.args.length) {
                    List<String> missing = new ArrayList<>();
                    for (int iMissing = argExprs.size(); iMissing < desc.args.length; iMissing++) {
                        if (!desc.args[iMissing].isOptional) {
                            missing.add(desc.args[iMissing].name);
                        }
                    }
                    if (!missing.isEmpty()) {
                        onTypeError.onMissingArguments((TesfFuncallExpr) expr, missing);
                    }
                }
            }
        }
        else if (!(expr instanceof TesfParenExpr || expr instanceof TesfLiteralExpr || expr instanceof TesfVarRefExpr)) {
            throw new AssertionError("Unhandled expression type: '" + expr.getClass() + "'; this is a bug in the plugin");
        }
    }

    public static void checkTypes1(TesfStmt stmt, TypeErrorConsumer onTypeError) {
        if (stmt instanceof TesfVarDeclStmt) {
            expectType(((TesfVarDeclStmt) stmt).getExpr(), getType((TesfVarDeclStmt) stmt), onTypeError);
        } else if (stmt instanceof TesfIfKwStmt) {
            expectType(((TesfIfKwStmt) stmt).getExpr(), TesfType.BOOL, onTypeError);
        } else if (stmt instanceof TesfWhileKwStmt) {
            expectType(((TesfWhileKwStmt) stmt).getExpr(), TesfType.BOOL, onTypeError);
        } else if (stmt instanceof TesfForKwStmt) {
            expectType(getForMiddleExpr((TesfForKwStmt) stmt), TesfType.BOOL, onTypeError);
        }
    }

    public static @Nullable TesfExpr getForMiddleExpr(TesfForKwStmt forStmt) {
        final PsiElement semi = forStmt.getSemi();
        if (semi != null) {
            PsiElement sibling = semi.getPrevSibling();
            if (sibling instanceof PsiWhiteSpace) sibling = sibling.getPrevSibling();
            if (sibling instanceof TesfExpr) {
                return (TesfExpr) sibling;
            }
        }
        return null;
    }

    public enum TesfType {
        BOOL, NUM, STR,
        @SuppressWarnings("unused") // Used in TesfBuiltins.json
        VOID;

        @Override
        public String toString() {
            return super.toString().toLowerCase();
        }
    }

    public interface TypeErrorConsumer {
        void onTypeError(TesfExpr element, TesfType expected, TesfType actual);

        void onUnexpectedArgument(TesfExpr argument);

        void onMissingArguments(TesfFuncallExpr expr, List<String> arguments);
    }
}
