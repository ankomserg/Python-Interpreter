package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspIfStmt extends AspCompoundStmt {
    ArrayList<AspExpr> exprs = new ArrayList<>();
    ArrayList<AspSuite> suites = new ArrayList<>();

    AspIfStmt(int n) {
        super(n);
    }

    static AspIfStmt parse(Scanner s) {
        enterParser("if stmt");

        AspIfStmt ais = new AspIfStmt(s.curLineNum());
        skip(s, TokenKind.ifToken);
        ais.exprs.add(AspExpr.parse(s));
        skip(s, TokenKind.colonToken);
        ais.suites.add(AspSuite.parse(s));

        while (s.curToken().kind == TokenKind.elifToken) {
            skip(s, TokenKind.elifToken);
            ais.exprs.add(AspExpr.parse(s));
            skip(s, TokenKind.colonToken);
            ais.suites.add(AspSuite.parse(s));
        }
        if (s.curToken().kind == TokenKind.elseToken) {
            skip(s, TokenKind.elseToken);
            skip(s, TokenKind.colonToken);
            ais.suites.add(AspSuite.parse(s));
        }

        leaveParser("if stmt");
        return ais;
    }

    @Override
    void prettyPrint() {
        prettyWrite("if ");
        for (int i = 0; i < exprs.size(); i++) {
            if (i != 0) {
                prettyWrite("elif ");
            }
            exprs.get(i).prettyPrint();
            prettyWrite(": ");
            suites.get(i).prettyPrint();
        }
        if (suites.size() > exprs.size()) {
            prettyWrite("else: ");
            suites.get(suites.size() - 1).prettyPrint();
        }
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (int i = 0; i < exprs.size(); i++) {
            if (exprs.get(i).eval(curScope).getBoolValue("if statement", this)) {
                trace(String.format("if True alt #%d: ...", i + 1));
                return suites.get(i).eval(curScope);
            }
        }
        if (suites.size() > exprs.size()) {
            trace("else: ...");
            return suites.get(suites.size() - 1).eval(curScope);
        }

        return null;
    }
}
