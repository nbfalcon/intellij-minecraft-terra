package org.nbfalcon.intellijMCTerra.terrascript.intellisense.syntaxHighlighting;

import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptParserDefinition;
import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptElementTypes;

import static com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey;

public class TerrascriptSyntaxHighlighter implements SyntaxHighlighter {
    public static final TerrascriptSyntaxHighlighter INSTANCE = new TerrascriptSyntaxHighlighter();

    public static final TextAttributesKey LINE_COMMENT = createTextAttributesKey(
            "LINE_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT);
    public static final TextAttributesKey BLOCK_COMMENT = createTextAttributesKey(
            "BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT);
    public static final TextAttributesKey STRING = createTextAttributesKey(
            "STRING", DefaultLanguageHighlighterColors.STRING);
    public static final TextAttributesKey NUMBER = createTextAttributesKey(
            "NUMBER", DefaultLanguageHighlighterColors.NUMBER);
    public static final TextAttributesKey OPERATOR_SIGN = createTextAttributesKey(
            "OPERATOR_SIGN", DefaultLanguageHighlighterColors.OPERATION_SIGN);
    public static final TextAttributesKey PARENTHESES = createTextAttributesKey(
            "PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES);
    public static final TextAttributesKey BRACES = createTextAttributesKey(
            "BRACES", DefaultLanguageHighlighterColors.BRACES);
    public static final TextAttributesKey KEYWORD = createTextAttributesKey(
            "KEYWORD", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey TYPE = createTextAttributesKey(
            "TYPE", DefaultLanguageHighlighterColors.KEYWORD);
    public static final TextAttributesKey SYMBOL = createTextAttributesKey(
            "SYMBOL", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    public static final TextAttributesKey FUNCTION = createTextAttributesKey(
            "FUNCTION", DefaultLanguageHighlighterColors.FUNCTION_CALL);
    public static final TextAttributesKey VARIABLE_DECL = createTextAttributesKey(
            "VARIABLE_DECL", DefaultLanguageHighlighterColors.GLOBAL_VARIABLE);
    public static final TextAttributesKey VAR_REF = createTextAttributesKey(
            "VAR_REF", DefaultLanguageHighlighterColors.LOCAL_VARIABLE);

    public static final TextAttributesKey[] LINE_COMMENT_KEYS = new TextAttributesKey[]{LINE_COMMENT};
    public static final TextAttributesKey[] BLOCK_COMMENT_KEYS = new TextAttributesKey[]{BLOCK_COMMENT};
    public static final TextAttributesKey[] STRING_KEYS = new TextAttributesKey[]{STRING};
    public static final TextAttributesKey[] NUMBER_KEYS = new TextAttributesKey[]{NUMBER};
    public static final TextAttributesKey[] OPERATOR_SIGN_KEYS = new TextAttributesKey[]{OPERATOR_SIGN};
    public static final TextAttributesKey[] PARENTHESES_KEYS = new TextAttributesKey[]{PARENTHESES};
    public static final TextAttributesKey[] BRACES_KEYS = new TextAttributesKey[]{BRACES};
    public static final TextAttributesKey[] KEYWORD_KEYS = new TextAttributesKey[]{KEYWORD};
    public static final TextAttributesKey[] TYPE_KEYS = new TextAttributesKey[]{TYPE};
    public static final TextAttributesKey[] SYMBOL_KEYS = new TextAttributesKey[]{SYMBOL};

    @Override
    public @NotNull Lexer getHighlightingLexer() {
        return TerrascriptParserDefinition.getLexer();
    }

    @Override
    public TextAttributesKey @NotNull [] getTokenHighlights(IElementType tokenType) {
        if (tokenType.equals(TerrascriptParserDefinition.LINE_COMMENT)) {
            return LINE_COMMENT_KEYS;
        } else if (tokenType.equals(TerrascriptParserDefinition.BLOCK_COMMENT)) {
            return BLOCK_COMMENT_KEYS;
        } else if (tokenType.equals(TerrascriptElementTypes.STRING)) {
            return STRING_KEYS;
        } else if (tokenType.equals(TerrascriptElementTypes.NUMBER)) {
            return NUMBER_KEYS;
        } else if (TerrascriptParserDefinition.OPERATORS.contains(tokenType)) {
            return OPERATOR_SIGN_KEYS;
        } else if (TerrascriptParserDefinition.KEYWORDS.contains(tokenType)) {
            return KEYWORD_KEYS;
        } else if (tokenType.equals(TerrascriptElementTypes.ID)) {
            return SYMBOL_KEYS;
        } else if (TerrascriptParserDefinition.PARENTHESES.contains(tokenType)) {
            return PARENTHESES_KEYS;
        } else if (TerrascriptParserDefinition.BRACES.contains(tokenType)) {
            return BRACES_KEYS;
        } else if (TerrascriptParserDefinition.TYPES.contains(tokenType)) {
            return TYPE_KEYS;
        }
        return TextAttributesKey.EMPTY_ARRAY;
    }
}
