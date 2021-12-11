package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeBoolValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspComparison extends AspSyntax {
    ArrayList<AspTerm> terms = new ArrayList<>();
    ArrayList<AspCompOpr> oprs = new ArrayList<>();

    AspComparison(int n) {
        super(n);
    }

    static AspComparison parse(Scanner s) {
        enterParser("comparison");

        AspComparison ac = new AspComparison(s.curLineNum());
        ac.terms.add(AspTerm.parse(s));

        while (s.isCompOpr()) {
            ac.oprs.add(AspCompOpr.parse(s));
            ac.terms.add(AspTerm.parse(s));
        }

        leaveParser("comparison");
        return ac;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < terms.size(); i++) {
            if (i != 0) {
                oprs.get(i - 1).prettyPrint();
            }
            terms.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = terms.get(0).eval(curScope); // current value of comparison
        RuntimeValue v1 = v; // will be used for comparison

        for (int i = 1; i < terms.size(); i++) {
            RuntimeValue v2 = terms.get(i).eval(curScope); // next item for comparison
            TokenKind k = oprs.get(i - 1).token.kind;

            // compare two items and store bool value in v
            switch (k) {
                case lessToken:
                    v = v1.evalLess(v2, this);
                    break;
                case greaterToken:
                    v = v1.evalGreater(v2, this);
                    break;
                case doubleEqualToken:
                    v = v1.evalEqual(v2, this);
                    break;
                case greaterEqualToken:
                    v = v1.evalGreaterEqual(v2, this);
                    break;
                case lessEqualToken:
                    v = v1.evalLessEqual(v2, this);
                    break;
                case notEqualToken:
                    v = v1.evalNotEqual(v2, this);
                    break;
                default:
                    Main.panic("Illegal term operator: " + k + "!");
            }
            // if some comparison in a chain evaluates to false then
            // it means that the whole chain evaluates to false
            if (!v.getBoolValue("comparison", this)) {
                return new RuntimeBoolValue(false);
            }

            v1 = v2; // current v2 will be compared to next v2
        }

        return v;
    }
}
