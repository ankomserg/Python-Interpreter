package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspStmt extends AspSyntax {
    AspStmt(int n) {
        super(n);
    }

     static AspStmt parse(Scanner s) {
        enterParser("stmt");

        AspStmt as;
        if (s.curToken().kind == TokenKind.forToken
                || s.curToken().kind == TokenKind.defToken
                || s.curToken().kind == TokenKind.ifToken
                || s.curToken().kind == TokenKind.whileToken) {
            as = AspCompoundStmt.parse(s);
        }
        else {
            as = AspSmallStmtList.parse(s);
        }

        leaveParser("stmt");
        return as;
    }
}
