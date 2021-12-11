package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspPassStmt extends AspSmallStmt {
    AspPassStmt(int n) {
        super(n);
    }

    static AspPassStmt parse(Scanner s) {
        enterParser("pass stmt");

        AspPassStmt aps = new AspPassStmt(s.curLineNum());
        skip(s, TokenKind.passToken);

        leaveParser("pass stmt");
        return aps;
    }

    @Override
    void prettyPrint() {
        prettyWrite("pass");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("pass");
        return null;
    }
}
