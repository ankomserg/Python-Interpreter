package no.uio.ifi.asp.parser;

import java.util.ArrayList;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.runtime.*;
import no.uio.ifi.asp.scanner.*;
import static no.uio.ifi.asp.scanner.TokenKind.*;

public class AspProgram extends AspSyntax {
    //-- Must be changed in part 2:
    ArrayList<AspStmt> stmts = new ArrayList<>();

    AspProgram(int n) {
	super(n);
    }


    public static AspProgram parse(Scanner s) {
	enterParser("program");

	AspProgram ap = new AspProgram(s.curLineNum());
	while (s.curToken().kind != eofToken) {
	    if (s.curToken().kind == dedentToken) {
	        skip(s, dedentToken);
        }
	    else {
            ap.stmts.add(AspStmt.parse(s));
        }
	}

	leaveParser("program");
	return ap;
    }


    @Override
    public void prettyPrint() {
	//-- Must be changed in part 2:
        for (int i = 0; i < stmts.size(); i++) {
            stmts.get(i).prettyPrint();
        }
    }


    @Override
    public RuntimeValue eval(RuntimeScope curScope) throws RuntimeReturnValue {
        for (AspStmt stmt : stmts) {
            try {
                stmt.eval(curScope);
            }
            catch (RuntimeReturnValue rrv) {
                RuntimeValue.runtimeError("Return statement outside function!",
                        rrv.lineNum);
            }
        }

	    return null;
    }
}
