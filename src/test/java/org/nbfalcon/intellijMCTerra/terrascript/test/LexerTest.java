package org.nbfalcon.intellijMCTerra.terrascript.test;

import com.intellij.lexer.Lexer;
import com.intellij.psi.TokenType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptParserDefinition;
import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptElementTypes;

public class LexerTest {
    @Test
    public void lexBasics() {
        final Lexer lexer = new TerrascriptParserDefinition().createLexer(null);
        lexer.start("id \"meow\";");
        Assertions.assertEquals(TerrascriptElementTypes.ID_KW, lexer.getTokenType());
        advanceSkipWS(lexer);
        Assertions.assertEquals("\"meow\"", lexer.getTokenText());
        advanceSkipWS(lexer);
        Assertions.assertEquals(TerrascriptElementTypes.SEMI, lexer.getTokenType());
    }

    private void advanceSkipWS(Lexer lexer) {
        do {
            lexer.advance();
        }
        while (TokenType.WHITE_SPACE.equals(lexer.getTokenType()));
    }
}
