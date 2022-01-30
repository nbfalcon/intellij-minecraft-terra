package org.nbfalcon.intellijMCTerra.terrascript;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;import org.nbfalcon.intellijMCTerra.terrascript.lang.TerrascriptParserDefinition;import org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptParser;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static org.nbfalcon.intellijMCTerra.terrascript.parser.TerrascriptElementTypes.*;

%%

%{
  public _TerrascriptLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _TerrascriptLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

STRING=\"([^\"]|\\.)*\"
ID=[a-zA-Z][a-zA-Z_0-9]*
NUMBER=[0-9]*

WHITE_SPACE=\s+
LINE_COMMENT=\/\/[^\R]*
BLOCK_COMMENT=\/\*([^*]*|\*+[^/])*\**\/

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return WHITE_SPACE; }
  {LINE_COMMENT}     { return TerrascriptParserDefinition.LINE_COMMENT; }
  {BLOCK_COMMENT}    { return TerrascriptParserDefinition.BLOCK_COMMENT; }
  {NUMBER}           { return NUMBER; }
  {STRING}           { return STRING; }

  "id"               {return ID_KW;}
  "fail"             { return FAIL; }
  "if"               { return IF; }
  "else"             { return ELSE; }
  "for"              { return FOR; }
  "while"            { return WHILE; }
  "num"              { return NUM_TY; }
  "bool"             { return BOOL_TY; }
  "str"              { return STR_TY; }
  {ID}               { return ID; }
  ";"                { return SEMI; }
  "{"                { return LBRACE; }
  "}"                { return RBRACE; }
  "="                { return ASSIGN; }
  "("                { return LPAR; }
  ")"                { return RPAR; }
  ","                { return COMMA; }
  "+"                { return ADD; }
  "-"                { return MINUS; }
  "*"                { return MUL; }
  "/"                { return DIV; }
  "%"                { return MODULO; }
  "<"                { return LT; }
  ">"                { return GT; }
  "<="               { return LTEQ; }
  ">="               { return GTEQ; }
  "=="               { return EQ; }
  "!="               { return NEQ; }
  "&&"               { return AND; }
  "||"               { return OR; }
  "!"                { return NEG; }
}

[^] { return BAD_CHARACTER; }
