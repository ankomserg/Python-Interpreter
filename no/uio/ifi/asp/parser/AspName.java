package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspName extends AspAtom {
    public String name;

    AspName(int n) {
        super(n);
    }

    public static AspName parse(Scanner s) {
        enterParser("name");

        AspName aspName = new AspName(s.curLineNum());
        aspName.name = s.curToken().name;
        skip(s, TokenKind.nameToken);

        leaveParser("name");

        return aspName;
    }

    @Override
    void prettyPrint() {
        prettyWrite(name);
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return curScope.find(name, this);
    }
}
