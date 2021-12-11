package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class RuntimeListValue extends RuntimeValue {
    List<RuntimeValue> values;

    public RuntimeListValue(List<RuntimeValue> values) {
        this.values = values;
    }

    public List<RuntimeValue> getValues() {
        return values;
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(values.size());
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !values.isEmpty();
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not", where));
    }

    @Override
    public RuntimeValue evalMultiply(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeIntValue) {
            int n = (int) v.getIntValue("* operand", where);
            return new RuntimeListValue(Collections.nCopies(n, values)
            .stream()
            .flatMap(List::stream)
            .collect(Collectors.toList()));
        }

        runtimeError("Type error for *.", where);
        return null;
    }

    @Override
    public RuntimeValue evalEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(false);
        }

        runtimeError("Type error for ==.", where);
        return null;
    }

    @Override
    public RuntimeValue evalNotEqual(RuntimeValue v, AspSyntax where) {
        if (v instanceof RuntimeNoneValue) {
            return new RuntimeBoolValue(true);
        }

        runtimeError("Type error for !=.", where);
        return null;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        int position = (int) v.getIntValue("subscription", where);

        if ((position >= values.size())
                || (position < 0)) {
            runtimeError(String.format("list index %d out of range!", position), where);
        }

        return values.get(position);
    }

    @Override
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        int index = (int) inx.getIntValue("list index", where);
        if ((index >= values.size())
                || (index < 0)) {
            runtimeError(String.format("list index %d out of range!", index), where);
        }

        values.set(index, val);
    }

    @Override
    protected String typeName() {
        return "list";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (!values.isEmpty()) {
            for (RuntimeValue value : values) {
                sb.append(value.showInfo());
                sb.append(", ");
            }
            sb = new StringBuilder(sb.substring(0, sb.length() - 2));
        }
        sb.append("]");

        return sb.toString();
    }
}
