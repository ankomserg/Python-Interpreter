package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeBoolValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspBooleanLiteral extends AspAtom {
    boolean value;

    AspBooleanLiteral(int n) {
        super(n);
    }

    static AspBooleanLiteral parse(Scanner s) {
        enterParser("boolean literal");

        AspBooleanLiteral abl = new AspBooleanLiteral(s.curLineNum());
        if (s.curToken().kind == TokenKind.falseToken) {
            abl.value = false;
        }
        else if (s.curToken().kind == TokenKind.trueToken) {
            abl.value = true;
        }
        s.readNextToken();

        leaveParser("boolean literal");
        return abl;
    }

    @Override
    void prettyPrint() {
        if (value) {
            prettyWrite("True");
        }
        else {
            prettyWrite("False");
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeBoolValue(value);
    }
}
