package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;
import java.util.List;

public class AspPrimary extends AspSyntax {
    ArrayList<AspPrimarySuffix> suffixes = new ArrayList<>();
    AspAtom atom;

    AspPrimary(int n) {
        super(n);
    }

    static AspPrimary parse(Scanner s) {
        enterParser("primary");

        AspPrimary ap = new AspPrimary(s.curLineNum());
        ap.atom = AspAtom.parse(s);

        while (s.curToken().kind == TokenKind.leftParToken
                || s.curToken().kind == TokenKind.leftBracketToken) {
            ap.suffixes.add(AspPrimarySuffix.parse(s));
        }

        leaveParser("primary");
        return ap;
    }

    @Override
    void prettyPrint() {
        atom.prettyPrint();
        for (AspPrimarySuffix suffix : suffixes) {
            suffix.prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeValue v = atom.eval(curScope);

        for (AspPrimarySuffix suffix : suffixes) {
            if (suffix instanceof AspSubscription) {
                v = v.evalSubscription(suffix.eval(curScope), this);
            }
            else if (suffix instanceof AspArguments) {
                RuntimeListValue list = (RuntimeListValue) suffix.eval(curScope);
                trace("Call function " + v.showInfo() + " with params " + list.showInfo());
                v = v.evalFuncCall((ArrayList<RuntimeValue>) list.getValues(),
                        this);
            }
        }

        return v;
    }
}
