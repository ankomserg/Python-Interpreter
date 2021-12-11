package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspSuite extends AspSyntax {
    ArrayList<AspStmt> stmts = new ArrayList<>();
    AspSmallStmtList list;

    AspSuite(int n) {
        super(n);
    }

    static AspSuite parse(Scanner s) {
        enterParser("suite");

        AspSuite as = new AspSuite(s.curLineNum());
        if (s.curToken().kind == TokenKind.newLineToken) {
            skip(s, TokenKind.newLineToken);
            skip(s, TokenKind.indentToken);
            while (s.curToken().kind != TokenKind.dedentToken) {
                as.stmts.add(AspStmt.parse(s));
            }
            skip(s, TokenKind.dedentToken);
        }
        else {
            as.list = AspSmallStmtList.parse(s);
        }

        leaveParser("suite");
        return as;
    }

    @Override
    void prettyPrint() {
        if (!stmts.isEmpty()) {
            prettyWriteLn();
            prettyIndent();
            for (AspStmt stmt : stmts) {
                stmt.prettyPrint();
            }
            prettyDedent();
        }
        else {
            list.prettyPrint();
        }
    }

    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (stmts.isEmpty()) {
            list.eval(curScope);
        }
        else {
            for (AspStmt stmt : stmts) {
                stmt.eval(curScope);
            }
        }
        return null;
    }
}
