package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspTerm extends AspSyntax {
    ArrayList<AspTermOpr> oprs = new ArrayList<>();
    ArrayList<AspFactor> factors = new ArrayList<>();

    AspTerm(int n) {
        super(n);
    }

    static AspTerm parse(Scanner s) {
        enterParser("term");

        AspTerm at = new AspTerm(s.curLineNum());
        at.factors.add(AspFactor.parse(s));

        while (s.isTermOpr()) {
            at.oprs.add(AspTermOpr.parse(s));
            at.factors.add(AspFactor.parse(s));
        }

        leaveParser("term");
        return at;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < factors.size(); i++) {
            if (i != 0) {
                oprs.get(i - 1).prettyPrint();
            }
            factors.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = factors.get(0).eval(curScope);

        for (int i = 1; i < factors.size(); i++) {
            TokenKind k = oprs.get(i - 1).token.kind;

            switch (k) {
                case plusToken:
                    v = v.evalAdd(factors.get(i).eval(curScope), this);
                    break;
                case minusToken:
                    v = v.evalSubtract(factors.get(i).eval(curScope), this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }
        }

        return v;
    }
}
