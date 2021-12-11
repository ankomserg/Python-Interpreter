package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeIntValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspIntegerLiteral extends AspAtom {
    long value;

    AspIntegerLiteral(int n) {
        super(n);
    }

    static AspIntegerLiteral parse(Scanner s) {
        enterParser("integer literal");

        AspIntegerLiteral ail = new AspIntegerLiteral(s.curLineNum());
        ail.value = s.curToken().integerLit;
        skip(s, TokenKind.integerToken);

        leaveParser("integer literal");
        return ail;
    }

    @Override
    void prettyPrint() {
        prettyWrite(String.valueOf(value));
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeIntValue(value);
    }
}
