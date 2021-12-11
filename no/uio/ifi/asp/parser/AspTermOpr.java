package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.Token;

public class AspTermOpr extends AspSyntax {
    Token token;

    AspTermOpr(int n) {
        super(n);
    }

    static AspTermOpr parse(Scanner s) {
        enterParser("term opr");

        AspTermOpr ato = new AspTermOpr(s.curLineNum());
        ato.token = s.curToken();
        s.readNextToken();

        leaveParser("term opr");
        return ato;
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
