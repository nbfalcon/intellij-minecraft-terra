package org.nbfalcon.intellijMCTerra.terrascript.lang;

import com.intellij.ide.lightEdit.LightEditCompatible;
import com.intellij.lang.ASTNode;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.FlexAdapter;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.TokenType;
import com.intellij.psi.tree.IElementType;
import com.intellij.psi.tree.IFileElementType;
import com.intellij.psi.tree.TokenSet;
import org.jetbrains.annotations.NotNull;
import org.nbfalcon.intellijMCTerra.terrascript._TerrascriptLexer;
import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptElementTypes;
import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptParser;
import org.nbfalcon.intellijMCTerra.terrascript.psi.TerrascriptElementType;
import org.nbfalcon.intellijMCTerra.terrascript.psi.impl.TesfFileImpl;

public class TerrascriptParserDefinition implements ParserDefinition, LightEditCompatible {
    public static final IElementType LINE_COMMENT = new TerrascriptElementType("LINE_COMMENT");
    public static final IElementType BLOCK_COMMENT = new TerrascriptElementType("BLOCK_COMMENT");

    public static final TokenSet STRING_TOKENS = TokenSet.create(TerrascriptElementTypes.STRING);
    public static final TokenSet COMMENT_TOKENS = TokenSet.create(LINE_COMMENT, BLOCK_COMMENT);
    public static final TokenSet WHITESPACE_TOKENS = TokenSet.create(TokenType.WHITE_SPACE);
    public static final TokenSet IDENTIFIER_TOKENS = TokenSet.create(TerrascriptElementTypes.ID);

    public static final TokenSet OPERATORS = TokenSet.create(
            TerrascriptElementTypes.ADD, TerrascriptElementTypes.MINUS,
            TerrascriptElementTypes.MUL, TerrascriptElementTypes.DIV, TerrascriptElementTypes.MODULO,
            TerrascriptElementTypes.LT, TerrascriptElementTypes.GT,
            TerrascriptElementTypes.LTEQ, TerrascriptElementTypes.GTEQ,
            TerrascriptElementTypes.EQ, TerrascriptElementTypes.NEQ,
            TerrascriptElementTypes.AND, TerrascriptElementTypes.OR,
            TerrascriptElementTypes.NEG);
    public static final TokenSet PARENTHESES = TokenSet.create(
            TerrascriptElementTypes.LPAR, TerrascriptElementTypes.RPAR);
    public static final TokenSet BRACES = TokenSet.create(
            TerrascriptElementTypes.LBRACE, TerrascriptElementTypes.RBRACE);
    public static final TokenSet KEYWORDS = TokenSet.create(
            TerrascriptElementTypes.ID_KW, TerrascriptElementTypes.FAIL,
            TerrascriptElementTypes.IF, TerrascriptElementTypes.ELSE,
            TerrascriptElementTypes.WHILE, TerrascriptElementTypes.FOR,
            TerrascriptElementTypes.BOOL_LIT);
    public static TokenSet TYPES = TokenSet.create(
            TerrascriptElementTypes.NUM_TY, TerrascriptElementTypes.STR_TY, TerrascriptElementTypes.BOOL_TY);

    public static IFileElementType FILE = new IFileElementType(TerrascriptLanguage.INSTANCE);

    @NotNull
    public static Lexer getLexer() {
        return new FlexAdapter(new _TerrascriptLexer());
    }

    @Override
    public @NotNull Lexer createLexer(Project project) {
        return getLexer();
    }

    @Override
    public @NotNull PsiParser createParser(Project project) {
        return new TerrascriptParser();
    }

    @Override
    public @NotNull IFileElementType getFileNodeType() {
        return FILE;
    }

    @Override
    public @NotNull TokenSet getWhitespaceTokens() {
        return WHITESPACE_TOKENS;
    }

    @Override
    public @NotNull TokenSet getCommentTokens() {
        return COMMENT_TOKENS;
    }

    @Override
    public @NotNull TokenSet getStringLiteralElements() {
        return STRING_TOKENS;
    }

    @Override
    public @NotNull PsiElement createElement(ASTNode node) {
        return TerrascriptElementTypes.Factory.createElement(node);
    }

    @Override
    public @NotNull PsiFile createFile(@NotNull FileViewProvider viewProvider) {
        return new TesfFileImpl(viewProvider);
    }
}
