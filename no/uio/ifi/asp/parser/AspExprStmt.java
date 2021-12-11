package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;

public class AspExprStmt extends AspSmallStmt {
    AspExpr expr;

    AspExprStmt(int n) {
        super(n);
    }

    static AspExprStmt parse(Scanner s) {
        enterParser("expr stmt");

        AspExprStmt aes = new AspExprStmt(s.curLineNum());
        aes.expr = AspExpr.parse(s);

        leaveParser("expr stmt");
        return aes;
    }

    @Override
    void prettyPrint() {
        expr.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        expr.eval(curScope);
        return null;
    }
}
