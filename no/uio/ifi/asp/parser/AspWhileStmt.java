package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspWhileStmt extends AspCompoundStmt {
    AspExpr expr;
    AspSuite suite;

    AspWhileStmt(int n) {
        super(n);
    }

    static AspWhileStmt parse(Scanner s) {
        enterParser("while stmt");

        AspWhileStmt aws = new AspWhileStmt(s.curLineNum());
        skip(s, TokenKind.whileToken);
        aws.expr = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);
        aws.suite = AspSuite.parse(s);

        leaveParser("while stmt");
        return aws;
    }

    @Override
    void prettyPrint() {
        prettyWrite("while ");
        expr.prettyPrint();
        prettyWrite(": ");
        suite.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        while (true) {
            RuntimeValue v = expr.eval(curScope);
            if (! v.getBoolValue("while", this)) {
                break;
            }
            trace("While True: ");
            suite.eval(curScope);
        }
        trace("While False: ");
        return null;
    }
}
