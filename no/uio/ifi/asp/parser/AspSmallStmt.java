package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspSmallStmt extends AspSyntax {

    AspSmallStmt(int n) {
        super(n);
    }

     static AspSmallStmt parse(Scanner s) {
        enterParser("small stmt");

        AspSmallStmt ass;
        if (s.anyEqualToken()) {
            ass = AspAssignment.parse(s);
        }
        else if (s.curToken().kind == TokenKind.passToken) {
            ass = AspPassStmt.parse(s);
        }
        else if (s.curToken().kind == TokenKind.returnToken) {
            ass = AspReturnStmt.parse(s);
        }
        else {
            ass = AspExprStmt.parse(s);
        }

        leaveParser("small stmt");
        return ass;
    }


}
