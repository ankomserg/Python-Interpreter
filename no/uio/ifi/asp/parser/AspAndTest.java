package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspAndTest extends AspSyntax {
    ArrayList<AspNotTest> aspNotTests = new ArrayList<>();

    AspAndTest(int n) {
        super(n);
    }

    static AspAndTest parse(Scanner s) {
        enterParser("and test");

        AspAndTest aat = new AspAndTest(s.curLineNum());
        aat.aspNotTests.add(AspNotTest.parse(s));
        while (s.curToken().kind == TokenKind.andToken) {
            skip(s, TokenKind.andToken);
            aat.aspNotTests.add(AspNotTest.parse(s));
        }

        leaveParser("and test");
        return aat;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < aspNotTests.size(); i++) {
            if (i != 0) {
                prettyWrite(" and ");
            }
            aspNotTests.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = aspNotTests.get(0).eval(curScope);
        if (!v.getBoolValue(v.showInfo(), this)) {
            return v;
        }
        for (int i = 1; i < aspNotTests.size(); i++) {
            v = aspNotTests.get(i).eval(curScope);
            if (!v.getBoolValue(v.showInfo(), this)) {
                return v;
            }
        }

        return v;
    }
}
