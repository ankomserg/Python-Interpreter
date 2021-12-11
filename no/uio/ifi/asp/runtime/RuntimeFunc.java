package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspFuncDef;
import no.uio.ifi.asp.parser.AspName;
import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeFunc extends RuntimeValue {
    AspFuncDef def;
    RuntimeScope defScope;
    String name;

    public RuntimeFunc(AspFuncDef def, RuntimeScope defScope) {
        this.def = def;
        this.defScope = defScope;
        name = def.name.name;
    }

    public RuntimeFunc(String info) {
        name = info;
    }

    @Override
    public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams, AspSyntax where) throws RuntimeReturnValue {
        int numberOfArgsPassed = actualParams.size();
        int numberOfArgsDefined = def.names.size();

        if (numberOfArgsPassed == numberOfArgsDefined) {
            RuntimeScope newScope = new RuntimeScope(defScope);
            for (int i = 0; i < numberOfArgsDefined; i++) {
                newScope.assign(def.names.get(i).name, actualParams.get(i));
            }
            try {
                def.suite.eval(newScope);
            }
            catch (RuntimeReturnValue rrv) {
                return rrv.value;
            }
            return new RuntimeNoneValue();
        }
        else {
            runtimeError(String.format("Expected %d parameters but got %d",
                    numberOfArgsDefined,
                    numberOfArgsPassed),
                    where);
        }

        return null;
    }

    @Override
    protected String typeName() {
        return "function";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return name;
    }
}
