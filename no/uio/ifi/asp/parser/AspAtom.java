package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspAtom extends AspSyntax {

    AspAtom(int n) {
        super(n);
    }

    static AspAtom parse(Scanner s) {
        enterParser("atom");

        AspAtom a = null;

        if (s.curToken().kind == TokenKind.nameToken) {
            a = AspName.parse(s);
        }
        else if (s.curToken().kind == TokenKind.integerToken) {
            a = AspIntegerLiteral.parse(s);
        }
        else if (s.curToken().kind == TokenKind.floatToken) {
            a = AspFloatLiteral.parse(s);
        }
        else if (s.curToken().kind == TokenKind.stringToken) {
            a = AspStringLiteral.parse(s);
        }
        else if (s.curToken().kind == TokenKind.falseToken
                || s.curToken().kind == TokenKind.trueToken) {
            a = AspBooleanLiteral.parse(s);
        }
        else if (s.curToken().kind == TokenKind.noneToken) {
            a = AspNoneLiteral.parse(s);
        }
        else if (s.curToken().kind == TokenKind.leftParToken) {
            a = AspInnerExpr.parse(s);
        }
        else if (s.curToken().kind == TokenKind.leftBracketToken) {
            a = AspListDisplay.parse(s);
        }
        else if (s.curToken().kind == TokenKind.leftBraceToken) {
            a = AspDictDisplay.parse(s);
        }
        else {
            String message = String.format("Expected an expression atom but found a %s!",
                    s.curToken().kind.toString());
            parserError(message, s.curLineNum());
        }

        leaveParser("atom");
        return a;
    }

}
