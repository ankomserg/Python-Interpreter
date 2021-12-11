package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspReturnStmt extends AspSmallStmt {
    AspExpr expr;

    AspReturnStmt(int n) {
        super(n);
    }

    static AspReturnStmt parse(Scanner s) {
        enterParser("return stmt");

        AspReturnStmt ars = new AspReturnStmt(s.curLineNum());
        skip(s, TokenKind.returnToken);
        ars.expr = AspExpr.parse(s);

        leaveParser("return stmt");
        return ars;
    }

    @Override
    void prettyPrint() {
        prettyWrite("return ");
        expr.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = expr.eval(curScope);
        trace("return " + v.showInfo());
        throw new RuntimeReturnValue(v, lineNum);
    }
}
