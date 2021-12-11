package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.Token;
import no.uio.ifi.asp.scanner.TokenKind;

public class AspFactorPrefix extends AspSyntax {
    Token prefix;
    int orderNumber;

    AspFactorPrefix(int n, int orderNumber) {
        super(n);
        this.orderNumber = orderNumber;
    }

    static AspFactorPrefix parse(Scanner s, int orderNumber) {
        enterParser("factor prefix");

        AspFactorPrefix afp = new AspFactorPrefix(s.curLineNum(), orderNumber);
        afp.prefix = s.curToken();
        s.readNextToken();

        leaveParser("factor prefix");
        return afp;
    }

    @Override
    void prettyPrint() {
        prettyWrite(prefix.kind + " ");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        return null;
    }
}
