package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.Token;

public class AspFactorOpr extends AspSyntax {
    Token token;

    AspFactorOpr(int n) {
        super(n);
    }

    static AspFactorOpr parse(Scanner s) {
        enterParser("factor opr");

        AspFactorOpr afo = new AspFactorOpr(s.curLineNum());
        afo.token = s.curToken();
        s.readNextToken();

        leaveParser("factor opr");
        return afo;
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
