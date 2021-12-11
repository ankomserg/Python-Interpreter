package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspCompoundStmt extends AspStmt {

    AspCompoundStmt(int n) {
        super(n);
    }

    static AspCompoundStmt parse(Scanner s) {
        enterParser("compound stmt");

        AspCompoundStmt acs;

        if (s.curToken().kind == TokenKind.ifToken) {
            acs = AspIfStmt.parse(s);
        }
        else if (s.curToken().kind == TokenKind.forToken) {
            acs = AspForStmt.parse(s);
        }
        else if (s.curToken().kind == TokenKind.whileToken) {
            acs = AspWhileStmt.parse(s);
        }
        else {
            acs = AspFuncDef.parse(s);
        }

        leaveParser("compound stmt");
        return acs;
    }
}
