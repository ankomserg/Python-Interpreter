package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspSubscription extends AspPrimarySuffix {
    AspExpr expression;

    AspSubscription(int n) {
        super(n);
    }

    static AspSubscription parse(Scanner s) {
        enterParser("subscription");

        skip(s, TokenKind.leftBracketToken);
        AspSubscription as = new AspSubscription(s.curLineNum());
        as.expression = AspExpr.parse(s);
        skip(s, TokenKind.rightBracketToken);

        leaveParser("subscription");
        return as;
    }

    @Override
    void prettyPrint() {
        prettyWrite("[");
        expression.prettyPrint();
        prettyWrite("]");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return expression.eval(curScope);
    }
}
