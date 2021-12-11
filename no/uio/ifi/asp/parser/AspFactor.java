package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.main.Main;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class AspFactor extends AspSyntax {
    ArrayList<AspFactorPrefix> prefixes = new ArrayList<>();
    ArrayList<AspPrimary> primaries = new ArrayList<>();
    ArrayList<AspFactorOpr> oprs = new ArrayList<>();

    AspFactor(int n) {
        super(n);
    }

    static AspFactor parse(Scanner s) {
        enterParser("factor");

        AspFactor f = new AspFactor(s.curLineNum());
        if (s.isFactorPrefix()) {
            f.prefixes.add(AspFactorPrefix.parse(s, f.primaries.size()));
        }
        f.primaries.add(AspPrimary.parse(s));

        while (s.isFactorOpr()) {
            f.oprs.add(AspFactorOpr.parse(s));

            if (s.isFactorPrefix()) {
                f.prefixes.add(AspFactorPrefix.parse(s, f.primaries.size()));
            }
            f.primaries.add(AspPrimary.parse(s));
        }

        leaveParser("factor");
        return f;
    }

    @Override
    void prettyPrint() {
        for (int i = 0; i < primaries.size(); i++) {
            if (i != 0) {
                oprs.get(i - 1).prettyPrint();
            }
            for (AspFactorPrefix prefix : prefixes) {
                if (prefix.orderNumber == i) {
                    prefix.prettyPrint();
                    break;
                }
            }
           primaries.get(i).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = primaries.get(0).eval(curScope);

        for (AspFactorPrefix prefix : prefixes) {
            if (prefix.orderNumber == 0) {
                TokenKind k = prefix.prefix.kind;

                switch (k) {
                    case plusToken:
                        v = v.evalPositive(this);
                        break;
                    case minusToken:
                        v = v.evalNegate(this);
                        break;
                    default:
                        Main.panic("Illegal prefix: " + k + "!");
                }
                break;
            }
        }

        for (int i = 1; i < primaries.size(); i++) {
            RuntimeValue vv = primaries.get(i).eval(curScope);

            for (AspFactorPrefix prefix : prefixes) {
                if (prefix.orderNumber == i) {
                    TokenKind k = prefix.prefix.kind;

                    switch (k) {
                        case plusToken:
                            vv = vv.evalPositive(this);
                            break;
                        case minusToken:
                            vv = vv.evalNegate(this);
                            break;
                        default:
                            Main.panic("Illegal prefix: " + k + "!");
                    }
                    break;
                }
            }

            TokenKind k = oprs.get(i - 1).token.kind;

            switch (k) {
                case astToken:
                    v = v.evalMultiply(vv, this);
                    break;
                case slashToken:
                    v = v.evalDivide(vv, this);
                    break;
                case percentToken:
                    v = v.evalModulo(vv, this);
                    break;
                case doubleSlashToken:
                    v = v.evalIntDivide(vv, this);
                    break;
                default:
                    Main.panic("Illegal factor operator: " + k + "!");
            }
        }

        return v;
    }
}
