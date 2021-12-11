package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspArguments extends AspPrimarySuffix {
    ArrayList<AspExpr> arguments = new ArrayList<>();

    AspArguments(int n) {
        super(n);
    }

    static AspArguments parse(Scanner s) {
        enterParser("arguments");

        AspArguments aa = new AspArguments(s.curLineNum());
        skip(s, TokenKind.leftParToken);
        if (s.curToken().kind != TokenKind.rightParToken) {
            aa.arguments.add(AspExpr.parse(s));

            while (s.curToken().kind == TokenKind.commaToken) {
                skip(s, TokenKind.commaToken);
                aa.arguments.add(AspExpr.parse(s));
            }
        }
        skip(s, TokenKind.rightParToken);

        leaveParser("arguments");
        return aa;
    }

    @Override
    void prettyPrint() {
        prettyWrite("(");
        for (int i = 0; i < arguments.size(); i++) {
            arguments.get(i).prettyPrint();
            if (i != arguments.size() - 1) {
                prettyWrite(", ");
            }
        }
        prettyWrite(")");
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        ArrayList<RuntimeValue> args = new ArrayList<>();
        for (AspExpr argument : arguments) {
            args.add(argument.eval(curScope));
        }

        return new RuntimeListValue(args);
    }
}
