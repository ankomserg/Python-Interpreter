package no.uio.ifi.asp.parser;

import no.uio.ifi.asp.scanner.Scanner;
import no.uio.ifi.asp.scanner.TokenKind;

public abstract class AspPrimarySuffix extends AspSyntax {

    AspPrimarySuffix(int n) {
        super(n);
    }

    static AspPrimarySuffix parse(Scanner s) {
        enterParser("primary suffix");

        AspPrimarySuffix aps;

        if (s.curToken().kind == TokenKind.leftParToken) {
            aps = AspArguments.parse(s);
        }
        else {
            aps = AspSubscription.parse(s);
        }

        leaveParser("primary suffix");
        return aps;
    }
}
