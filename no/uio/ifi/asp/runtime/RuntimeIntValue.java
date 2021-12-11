package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeIntValue extends RuntimeValue {
    long intValue;

    public RuntimeIntValue(long intValue) {
        this.intValue = intValue;
    }

    @Override
    public long getIntValue(String what, AspSyntax where) {
        return intValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return (double) intValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !(intValue == 0);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return this;
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeIntValue(- intValue);
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue +
                    v.getIntValue("+ operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue +
                    v.getFloatValue("+ operand", where));
        }

        runtimeError("Type error for +.", where);
        return null;
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue -
                    v.getIntValue("- operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue -
                    v.getFloatValue("- operand", where));
        }

        runtimeError("Type error for -.", where);
        return null;
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(intValue *
                    v.getIntValue("* operand", where));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue *
                    v.getFloatValue("* operand", where));
        }

        runtimeError("Type error for *.", where);
        return null;
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long divisor = v.getIntValue("/ operand", where);

            if (intValue % divisor != 0) {
                return new RuntimeFloatValue((double) intValue / divisor);
            }

            return new RuntimeIntValue(intValue / divisor);
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(intValue /
                    v.getFloatValue("/ operand", where));
        }

        runtimeError("Type error for /.", where);
        return null;
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            return new RuntimeIntValue(Math.floorDiv(intValue,
                    v.getIntValue("// operand", where)));
        }
        else if (v instanceof RuntimeFloatValue) {
            return new RuntimeFloatValue(Math.floor(intValue /
                    v.getFloatValue("// operand", where)));
        }

        runtimeError("Type error for //.", where);
        return null;
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 = v.getIntValue("% operand", where);
            return new RuntimeIntValue(Math.floorMod(intValue, v2));
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("% operand", where);
            return new RuntimeFloatValue(intValue - v2 * Math.floor(intValue / v2));
        }

        runtimeError("Type error for %.", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 =  v.getIntValue("== operand", where);
            return new RuntimeBoolValue(intValue == v2);
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("== operand", where);
            return new RuntimeBoolValue(intValue == v2);
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }

        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 =  v.getIntValue("> operand", where);
            return new RuntimeBoolValue(intValue > v2);
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("> operand", where);
            return new RuntimeBoolValue(intValue > v2);
        }

        runtimeError("Type error for >.", where);
        return null;
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 =  v.getIntValue(">= operand", where);
            return new RuntimeBoolValue(intValue >= v2);
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue(">= operand", where);
            return new RuntimeBoolValue(intValue >= v2);
        }

        runtimeError("Type error for >=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 =  v.getIntValue("< operand", where);
            return new RuntimeBoolValue(intValue < v2);
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("< operand", where);
            return new RuntimeBoolValue(intValue < v2);
        }

        runtimeError("Type error for <.", where);
        return null;
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 =  v.getIntValue("<= operand", where);
            return new RuntimeBoolValue(intValue <= v2);
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("<= operand", where);
            return new RuntimeBoolValue(intValue <= v2);
        }

        runtimeError("Type error for <=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            long v2 =  v.getIntValue("!= operand", where);
            return new RuntimeBoolValue(intValue != v2);
        }
        else if (v instanceof RuntimeFloatValue) {
            double v2 = v.getFloatValue("!= operand", where);
            return new RuntimeBoolValue(intValue != v2);
        }
        else if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }

        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not operand", where));
    }

    @Override
    protected String typeName() {
        return "integer";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return toString();
    }

    @Override
    public String toString() {
        return String.valueOf(intValue);
    }
}
