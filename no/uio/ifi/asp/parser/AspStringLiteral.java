package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeStringValue;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspStringLiteral extends AspAtom {
    String text;

    AspStringLiteral(int n) {
        super(n);
    }

    static AspStringLiteral parse(Scanner s) {
        enterParser("string literal");

        AspStringLiteral asl = new AspStringLiteral(s.curLineNum());
        asl.text = s.curToken().stringLit;
        skip(s, TokenKind.stringToken);

        leaveParser("string literal");
        return asl;
    }

    @Override
    void prettyPrint() {
        prettyWrite("\"" + text + "\"");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return new RuntimeStringValue(text);
    }
}
