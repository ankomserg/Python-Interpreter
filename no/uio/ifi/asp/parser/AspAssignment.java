package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.runtime.RuntimeListValue;
import no.uio.ifi.asp.runtime.RuntimeReturnValue;
import no.uio.ifi.asp.runtime.RuntimeScope;
import no.uio.ifi.asp.runtime.RuntimeValue;
import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

import java.util.ArrayList;

public class AspAssignment extends AspSmallStmt {
    AspName name;
    ArrayList<AspSubscription> subscriptions = new ArrayList<>();
    AspExpr expression;

    AspAssignment(int n) {
        super(n);
    }

    public static AspAssignment parse(Scanner s) {
        enterParser("assignment");

        AspAssignment aa = new AspAssignment(s.curLineNum());
        test(s, TokenKind.nameToken);
        aa.name = AspName.parse(s);

        while (s.curToken().kind == TokenKind.leftBracketToken) {
            aa.subscriptions.add(AspSubscription.parse(s));
        }
        skip(s, TokenKind.equalToken);

        aa.expression = AspExpr.parse(s);

        leaveParser("assignment");
        return aa;
    }
    @Override
    void prettyPrint() {
        name.prettyPrint();
        for (AspSubscription subscription : subscriptions) {
            subscription.prettyPrint();
        }
        prettyWrite(" = ");
        expression.prettyPrint();
    }

    @Override
    RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        if (subscriptions.isEmpty()) {
            RuntimeValue value = expression.eval(curScope);
            curScope.assign(name.name, value);
            trace(name.name + " = " + value.showInfo());
        }
        else {
            RuntimeValue a = curScope.find(name.name, this);
            RuntimeValue assignmentValue = expression.eval(curScope);
            RuntimeValue index = subscriptions.get(subscriptions.size() - 1).eval(curScope);

            StringBuilder sb = new StringBuilder();
            sb.append(name.name);

            for (int i = 0; i < subscriptions.size() - 1; i++) {
                RuntimeValue subscription = subscriptions.get(i).eval(curScope);
                a = a.evalSubscription(subscription, this);
                sb.append("[").append(subscription.showInfo()).append("]");
            }

            sb.append("[").append(index.showInfo()).append("]").append(" = ").append(assignmentValue.showInfo());

            trace(sb.toString());
            a.evalAssignElem(index, assignmentValue, this);
        }

        return null;
    }
}
