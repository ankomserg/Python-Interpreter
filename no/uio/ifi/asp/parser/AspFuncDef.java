package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeFunc;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspFuncDef extends AspCompoundStmt {
    public AspName name;
    public ArrayList<AspName> names = new ArrayList<>();
    public AspSuite suite;

    AspFuncDef(int n) {
        super(n);
    }

    static AspFuncDef parse(Scanner s) {
        enterParser("func def");

        AspFuncDef afd = new AspFuncDef(s.curLineNum());
        skip(s, TokenKind.defToken);

        test(s, TokenKind.nameToken);
        afd.name = AspName.parse(s);
        skip(s, TokenKind.leftParToken);

        while (s.curToken().kind != TokenKind.rightParToken) {
            test(s, TokenKind.nameToken);
            afd.names.add(AspName.parse(s));
            if (s.curToken().kind == TokenKind.commaToken) {
                s.readNextToken();
            }
        }

        skip(s, TokenKind.rightParToken);
        skip(s, TokenKind.colonToken);

        afd.suite = AspSuite.parse(s);

        leaveParser("func def");

        return afd;
    }

    @Override
    void prettyPrint() {
        prettyWrite("def ");
        names.get(0).prettyPrint();
        prettyWrite("(");
        for (int i = 1; i < names.size(); i++) {
            names.get(i).prettyPrint();
            if (i != names.size() - 1) {
                prettyWrite(", ");
            }
        }
        prettyWrite("):");
        suite.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        RuntimeFunc f = new RuntimeFunc(this, curScope);
        curScope.assign(name.name, f);
        trace("def " + name.name);
        return f;
    }
}
