package no.uio.ifi.asp.runtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import no.uio.ifi.asp.main.*;
import no.uio.ifi.asp.parser.AspSyntax;

public class RuntimeLibrary extends RuntimeScope {
    private Scanner keyboard = new Scanner(System.in);

    public RuntimeLibrary() {
        // len
        assign("len", new RuntimeFunc("len") {
            @Override
            public RuntimeValue evalFuncCall(
                    ArrayList<RuntimeValue> actualParams,
                    AspSyntax where) {
                checkNumParams(actualParams, 1, "len", where);
                return actualParams.get(0).evalLen(where);
            }});

        // print
        assign("print", new RuntimeFunc("print") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                                             AspSyntax where) {
                for (int i = 0; i < actualParams.size(); ++i) {
                    if (i > 0) System.out.print(" ");
                    System.out.print(actualParams.get(i).toString());
                }
                System.out.println();
                return new RuntimeNoneValue();
            }});

        // exit
        assign("exit", new RuntimeFunc("exit") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                                             AspSyntax where) {
                checkNumParams(actualParams, 1, "exit", where);
                if (! (actualParams.get(0) instanceof RuntimeIntValue)) {
                    runtimeError(String.format("Parameter must be of integer type"), where);
                }
                System.exit((int) (actualParams.get(0).getIntValue("exit", where)));
                return null;
            }});

        // float
        assign("float", new RuntimeFunc("float") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams,
                                             AspSyntax where) {
                checkNumParams(actualParams, 1, "float", where);
                RuntimeValue param = actualParams.get(0);

                if (param instanceof RuntimeStringValue) {
                    String strParam = param.getStringValue("float", where);
                    try {
                        double number = Double.parseDouble(strParam);
                        return new RuntimeFloatValue(number);
                    }
                    catch (Exception e) {
                        runtimeError("String " + strParam + " is not a legal float", where);
                    }
                }
                else if (param instanceof RuntimeIntValue) {
                    double number = param.getFloatValue("float", where);
                    return new RuntimeFloatValue(number);
                }
                else if (param instanceof RuntimeFloatValue) {
                    return param;
                }

                runtimeError("Type error: parameter to float is neither number nor text string", where);
                return null;
            }
        });

        // input
        assign("input", new RuntimeFunc("input") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
                    , AspSyntax where) {
                checkNumParams(actualParams, 1, "input", where);
                System.out.print(actualParams.get(0)
                        .getStringValue("input", where));
                String in = keyboard.nextLine();
                return new RuntimeStringValue(in);
            }
        });

        // int
        assign("int", new RuntimeFunc("int") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
                    , AspSyntax where) {
                checkNumParams(actualParams, 1, "int", where);
                RuntimeValue param = actualParams.get(0);

                if (param instanceof RuntimeStringValue) {
                    String strParam = param.getStringValue("int", where);
                    try {
                        long number = Long.parseLong(strParam);
                        return new RuntimeIntValue(number);
                    }
                    catch (Exception e) {
                        runtimeError("String " + strParam + " is not a legal int", where);
                    }
                }
                else if (param instanceof RuntimeFloatValue) {
                    double floatParam = param.getFloatValue("int", where);
                    long number = (long) floatParam;
                    return new RuntimeIntValue(number);
                }
                else if (param instanceof RuntimeIntValue) {
                    return param;
                }

                runtimeError("Type error: parameter to int is neither number nor text string", where);
                return null;
            }
        });

        // range
        assign("range", new RuntimeFunc("range") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
                    , AspSyntax where) {
                checkNumParams(actualParams, 2, "range", where);
                List<RuntimeValue> values = new ArrayList<>();
                int first = (int) actualParams.get(0)
                        .getIntValue("Type error: 1st parameter to range is not an integer!", where);
                int second = (int) actualParams.get(1)
                        .getIntValue("Type error: 2nd parameter to range is not an integer!", where);

                for (int i = first; i < second; i++) {
                    values.add(new RuntimeIntValue(i));
                }
                return new RuntimeListValue(values);
            }
        });

        // str
        assign("str", new RuntimeFunc("str") {
            @Override
            public RuntimeValue evalFuncCall(ArrayList<RuntimeValue> actualParams
                    , AspSyntax where) {
                StringBuilder sb = new StringBuilder();
                for (RuntimeValue param : actualParams) {
                    sb.append(param.toString());
                }
                return new RuntimeStringValue(sb.toString());
            }
        });
    }


    private void checkNumParams(ArrayList<RuntimeValue> actArgs, 
				int nCorrect, String id, AspSyntax where) {
	if (actArgs.size() != nCorrect)
	    RuntimeValue.runtimeError("Wrong number of parameters to "+id+"!",where);
    }
}
