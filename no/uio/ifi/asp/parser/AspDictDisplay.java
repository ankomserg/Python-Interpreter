package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class AspDictDisplay extends AspAtom {
    LinkedHashMap<AspStringLiteral, AspExpr> dict = new LinkedHashMap<>();

    AspDictDisplay(int n) {
        super(n);
    }

    static AspDictDisplay parse(Scanner s) {
        enterParser("dict display");

        AspDictDisplay add = new AspDictDisplay(s.curLineNum());
        skip(s, TokenKind.leftBraceToken);
        if (s.curToken().kind != TokenKind.rightBraceToken) {
            test(s, TokenKind.stringToken);
            AspStringLiteral asl = AspStringLiteral.parse(s);
            skip(s, TokenKind.colonToken);
            AspExpr ae = AspExpr.parse(s);
            add.dict.put(asl, ae);

            while (s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
                test(s, TokenKind.stringToken);
                asl = AspStringLiteral.parse(s);
                skip(s, TokenKind.colonToken);
                ae = AspExpr.parse(s);
                add.dict.put(asl, ae);
            }
        }
        skip(s, TokenKind.rightBraceToken);

        leaveParser("dict display");
        return add;
    }

    @Override
    void prettyPrint() {
        prettyWrite("{");

        Iterator<Map.Entry<AspStringLiteral, AspExpr>> itr = dict.entrySet().iterator();
        while (itr.hasNext()) {
            Map.Entry<AspStringLiteral, AspExpr> entry = itr.next();
            entry.getKey().prettyPrint();
            prettyWrite(": ");
            entry.getValue().prettyPrint();
            if (itr.hasNext()) {
                prettyWrite(",");
            }
        }
        prettyWrite("}");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        LinkedHashMap<String, RuntimeValue> values = new LinkedHashMap<>();
        for (Map.Entry<AspStringLiteral, AspExpr>  entry : dict.entrySet()) {
            RuntimeStringValue rsv = (RuntimeStringValue) entry.getKey().eval(curScope);
            RuntimeValue rv = entry.getValue().eval(curScope);

            values.put(rsv.toString(), rv);
        }

        return new RuntimeDictValue(values);
    }
}
