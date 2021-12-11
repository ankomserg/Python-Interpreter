package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.Token;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspNotTest extends AspSyntax {
    boolean isNotPresent = false;
    AspComparison comparison;

    AspNotTest(int n) {
        super(n);
    }

    static AspNotTest parse(Scanner s) {
        enterParser("not test");

        AspNotTest ant = new AspNotTest(s.curLineNum());
        if (s.curToken().kind == TokenKind.notToken) {
            ant.isNotPresent = true;
            skip(s, TokenKind.notToken);
        }
        ant.comparison = AspComparison.parse(s);

        leaveParser("not test");
        return ant;
    }

    @Override
    void prettyPrint() {
        if (isNotPresent) {
            prettyWrite("not ");
        }
        comparison.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = comparison.eval(curScope);

        if (isNotPresent) {
            v = v.evalNot(this);
        }

        return v;
    }
}
