package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;

public class RuntimeFloatValue extends RuntimeValue {
    double floatValue;

    public RuntimeFloatValue(double floatValue) {
        this.floatValue = floatValue;
    }

    @Override
    public double getFloatValue(String what, AspSyntax where) {
        return floatValue;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return floatValue != 0d;
    }

    @Override
    public RuntimeValue evalAdd(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("+ operand", where);

        return new RuntimeFloatValue(v1 + v2);
    }

    @Override
    public RuntimeValue evalDivide(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("+ operand", where);

        return new RuntimeFloatValue(v1 / v2);
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;

        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }

        double v2 = v.getFloatValue("== operand", where);

        return new RuntimeBoolValue(v1 == v2);
    }

    @Override
    public RuntimeValue evalGreater(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("> operand", where);

        return new RuntimeBoolValue(v1 > v2);
    }

    @Override
    public RuntimeValue evalGreaterEqual(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue(">= operand", where);

        return new RuntimeBoolValue(v1 >= v2);
    }

    @Override
    public RuntimeValue evalIntDivide(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("// operand", where);

        return new RuntimeFloatValue(Math.floor(v1 / v2));
    }

    @Override
    public RuntimeValue evalLess(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("< operand", where);

        return new RuntimeBoolValue(v1 < v2);
    }

    @Override
    public RuntimeValue evalLessEqual(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("<= operand", where);

        return new RuntimeBoolValue(v1 <= v2);
    }

    @Override
    public RuntimeValue evalModulo(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("% operand", where);

        double result = v1 - v2 * Math.floor(v1 / v2);

        return new RuntimeFloatValue(result);
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("* operand", where);

        return new RuntimeFloatValue(v1 * v2);
    }

    @Override
    public RuntimeValue evalNegate(AspSyntax where) {
        return new RuntimeFloatValue(- floatValue);
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;

        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }
        double v2 = v.getFloatValue("!= operand", where);

        return new RuntimeBoolValue(v1 != v2);
    }

    @Override
    public RuntimeValue evalPositive(AspSyntax where) {
        return this;
    }

    @Override
    public RuntimeValue evalSubtract(RuntimeValue v, AspSyntax where) {
        double v1 = floatValue;
        double v2 = v.getFloatValue("- operand", where);

        return new RuntimeFloatValue(v1 - v2);
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not operand", where));
    }

    @Override
    protected String typeName() {
        return "float";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return toString();
    }

    @Override
    public String toString() {
        return String.valueOf(floatValue);
    }
}
