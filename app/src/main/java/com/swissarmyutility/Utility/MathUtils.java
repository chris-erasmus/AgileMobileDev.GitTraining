package com.swissarmyutility.Utility;

/**
 * Created by digvesh.kumar on 7/28/2014.
 */
public class MathUtils {

     private static double parseAsDouble(String operand)
    {
        double number = (operand.equals("") || operand==null)? 0.0 : Double.parseDouble(operand);
        return number;
    }

    public static double performOperation(String operand1,String operand2,char operator)
    {
        double result=0.0;
        switch(operator)
        {
            case '+' :
                result =  parseAsDouble(operand1) + parseAsDouble(operand2);
                break;
            case '-' :
                result =  parseAsDouble(operand1) - parseAsDouble(operand2);
                break;
            case '/' :
                result =  parseAsDouble(operand1) / parseAsDouble(operand2);
                break;
            case '*' :
                result =  parseAsDouble(operand1) * parseAsDouble(operand2);
                break;
        }
        return result;
    }
}
