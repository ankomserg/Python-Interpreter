package no.uio.ifi.asp.runtime;

import no.uio.ifi.asp.parser.AspSyntax;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class RuntimeDictValue extends RuntimeValue {
    LinkedHashMap<String, RuntimeValue> dict;

    public RuntimeDictValue(LinkedHashMap<String, RuntimeValue> dict) {
        this.dict = dict;
    }

    @Override
    public RuntimeValue evalSubscription(RuntimeValue v, AspSyntax where) {
        String key = v.getStringValue("subscription", where);

        if (dict.containsKey(key)) {
            return dict.get(key);
        }

        runtimeError(String.format("Dictionary key '%s' undefined!", key), where);
        return null;
    }

    @Override
    public boolean getBoolValue(String what, AspSyntax where) {
        return !dict.isEmpty();
    }

    @Override
    public RuntimeValue evalNot(AspSyntax where) {
        return new RuntimeBoolValue(!getBoolValue("not", where));
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
    public void evalAssignElem(RuntimeValue inx, RuntimeValue val, AspSyntax where) {
        if (inx instanceof RuntimeStringValue) {
            dict.put(inx.toString(), val);
        }
        else {
            runtimeError("Dict key must be String", where);
        }
    }

    @Override
    public RuntimeValue evalLen(AspSyntax where) {
        return new RuntimeIntValue(dict.size());
    }

    @Override
    protected String typeName() {
        return "dictionary";
    }

    @Override
    protected String showInfo(ArrayList<RuntimeValue> inUse, boolean toPrint) {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        for (Map.Entry<String, RuntimeValue>  entry : dict.entrySet()) {
            sb.append("\"").append(entry.getKey()).append("\"");
            sb.append(": ");
            sb.append(entry.getValue().showInfo());
            sb.append(", ");
        }
        if (sb.length() > 1) {
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
        }

        sb.append("}");
        return sb.toString();
    }
}
