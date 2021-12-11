package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.Token;

public class AspCompOpr extends AspSyntax {
    Token token;

    AspCompOpr(int n) {
        super(n);
    }

    static AspCompOpr parse(Scanner s) {
        enterParser("comp opr");

        AspCompOpr aco = new AspCompOpr(s.curLineNum());
        aco.token = s.curToken();
        s.readNextToken();

        leaveParser("comp opr");
        return aco;
    }

    @Override
    void prettyPrint() {
        prettyWrite(" " + token.kind + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
