package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspSmallStmtList extends AspStmt {
    ArrayList<AspSmallStmt> statements = new ArrayList<>();

    AspSmallStmtList(int n) {
        super(n);
    }

    public static AspSmallStmtList parse(Scanner s) {
        enterParser("small stmt list");

        AspSmallStmtList sst = new AspSmallStmtList(s.curLineNum());
        sst.statements.add(AspSmallStmt.parse(s));

        while (s.curToken().kind == TokenKind.semicolonToken) {
            skip(s, TokenKind.semicolonToken);
            if (s.curToken().kind == TokenKind.newLineToken) {
                break;
            }
            sst.statements.add(AspSmallStmt.parse(s));
        }
        skip(s, TokenKind.newLineToken);

        leaveParser("small stmt list");
        return sst;
    }

    @Override
    void prettyPrint() {
        if (statements.size() == 1) {
            statements.get(0).prettyPrint();
        }
        else {
            for (int i = 0; i < statements.size(); i++) {
                statements.get(i).prettyPrint();
                if (i != statements.size() - 1) {
                    prettyWrite("; ");
                }
            }
        }
        prettyWriteLn();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspSmallStmt statement : statements) {
            statement.eval(curScope);
        }
        return null;
    }
}
