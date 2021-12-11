package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeNoneValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspNoneLiteral extends AspAtom {

    AspNoneLiteral(int n) {
        super(n);
    }

    static AspNoneLiteral parse(Scanner s) {
        enterParser("none literal");

        AspNoneLiteral anl = new AspNoneLiteral(s.curLineNum());
        skip(s, TokenKind.noneToken);

        leaveParser("none literal");
        return anl;
    }

    @Override
    void prettyPrint() {
        prettyWrite("None");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        trace("None");
        return new RuntimeNoneValue();
    }
}
