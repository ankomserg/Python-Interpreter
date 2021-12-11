package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.awt.*;
import java.util.ArrayList;

public class AspForStmt extends AspCompoundStmt {
    AspName name;
    AspExpr expr;
    AspSuite suite;

    AspForStmt(int n) {
        super(n);
    }

    static AspForStmt parse(Scanner s) {
        enterParser("for stmt");

        AspForStmt afs = new AspForStmt(s.curLineNum());
        skip(s, TokenKind.forToken);

        test(s, TokenKind.nameToken);
        afs.name = AspName.parse(s);
        skip(s, TokenKind.inToken);

        afs.expr = AspExpr.parse(s);
        skip(s, TokenKind.colonToken);

        afs.suite = AspSuite.parse(s);

        leaveParser("for stmt");
        return afs;
    }

    @Override
    void prettyPrint() {
        prettyWrite("for ");
        name.prettyPrint();
        prettyWrite(" in ");
        expr.prettyPrint();
        prettyWrite(": ");
        suite.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue value = expr.eval(curScope);
        if (value instanceof RuntimeListValue) {
            ArrayList<RuntimeValue> list = (ArrayList<RuntimeValue>) ((RuntimeListValue) value).getValues();
            for (int i = 0; i < list.size(); i++) {
                RuntimeValue currentValue = list.get(i);
                trace(String.format("for #%d: %s = %s", i + 1, name.name, currentValue.showInfo()));
                curScope.assign(name.name, currentValue);
                suite.eval(curScope);
            }
        }
        return null;
    }
}
