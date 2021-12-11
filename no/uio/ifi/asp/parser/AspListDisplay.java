package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspListDisplay extends AspAtom {
    ArrayList<AspExpr> expressions = new ArrayList<>();

    AspListDisplay(int n) {
        super(n);
    }

    static AspListDisplay parse(Scanner s) {
        enterParser("list display");

        AspListDisplay ald = new AspListDisplay(s.curLineNum());

        skip(s, TokenKind.leftBracketToken);
        if (s.curToken().kind != TokenKind.rightBracketToken) {
            ald.expressions.add(AspExpr.parse(s));

            while (s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
                ald.expressions.add(AspExpr.parse(s));
            }
        }
        skip(s, TokenKind.rightBracketToken);

        leaveParser("list display");
        return ald;
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");
        for (int i = 0; i < expressions.size(); i++) {
            expressions.get(i).prettyPrint();
            if (i != expressions.size() - 1) {
                prettyWrite(", ");
            }
        }
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> values = new ArrayList<>();
        for (AspExpr expr : expressions) {
            values.add(expr.eval(curScope));
        }

        RuntimeListValue rlv = new RuntimeListValue(values);
        return rlv;
    }
}
