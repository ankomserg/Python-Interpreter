package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeStringValue extends RuntimeValue {
    String strValue;

    public RuntimeStringValue(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !strValue.isEmpty();
    }

    @Override
    public String getStringValue(String what, AspSyntax where) {
        return strValue;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        int index = (int) v.getIntValue("subscription", where);

        if (index >= strValue.length()
                || index < 0) {
            runtimeError(String.format("String index %s out of range!", index), where);
        }
        String character = String.valueOf(strValue.charAt(index));

        return new RuntimeStringValue(character);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue("+ operand", where);

            return new RuntimeStringValue(s1 + s2);
        }

        runtimeError("Type error for +.", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue("== operand", where);

            return new RuntimeBoolValue(s1.equals(s2));
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }

        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue("> operand", where);

            return new RuntimeBoolValue(s1.compareTo(s2) > 0);
        }

        runtimeError("Type error for >.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue(">= operand", where);

            return new RuntimeBoolValue(s1.compareTo(s2) >= 0);
        }

        runtimeError("Type error for >=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue("< operand", where);

            return new RuntimeBoolValue(s1.compareTo(s2) < 0);
        }

        runtimeError("Type error for <.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue("<= operand", where);

            return new RuntimeBoolValue(s1.compareTo(s2) <= 0);
        }

        runtimeError("Type error for <=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long times = v.getIntValue("* operand", where);

            StringBuilder sb = new StringBuilder();
            for (long i = 0; i < times; i++) {
                sb.append(strValue);
            }

            return new RuntimeStringValue(sb.toString());
        }

        runtimeError("Type error for *.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not operand", where));
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeStringValue) {
            String s1 = strValue;
            String s2 = v.getStringValue("!= operand", where);

            return new RuntimeBoolValue(!s1.equals(s2));
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }

        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(strValue.length());
    }

    @Override
    protected String typeName() {
        return "string";
    }

    @Override
    public String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        if (strValue.contains("'"))
            return String.format("\"%s\"", strValue);
        else
            return String.format("'%s'", strValue);
    }

    @Override
    public String toString() {
        return strValue;
    }
}
