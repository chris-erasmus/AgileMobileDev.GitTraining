package com.swissarmyutility.Calculator;

import com.swissarmyutility.Utility.MathUtils;

import java.util.ArrayList;

/**
 * Created by digvesh.kumar on 7/28/2014.
 */
public class Result {

    private String result="";
    private char operator;
    private ArrayList<String> operands;
    private boolean resultOut;

    public Result()
    {
        operands = new ArrayList<String>();
    }

    public  String getResult()
    {
        return this.result;
    }

    public void setResult(String result)
    {
        if(result.equals("") || result==null)
            this.result="";
        else
            this.result = result;
    }

    public void appendResult(String appendedString)
    {
        this.result = getResult() + appendedString;
    }

    public void setOperator(char operator)
    {
        this.operator = operator;
    }

    public char getOperator()
    {
        return this.operator;
    }

    public void addOperand(String operand)
    {
        try
        {
            if (!operand.equals("") && operand!=null)
                operands.add(operand);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
        }
    }

    public void clearOperands()
    {
        this.getOperands().clear();
    }

    public ArrayList<String> getOperands()
    {
        return this.operands;
    }

    public int getOperandsSize()
    {
        return this.operands.size();
    }

    public String performOperation(char operator)
    {
        String res="0.0";
        if(getOperandsSize()==2)
        res = String.valueOf(MathUtils.performOperation(operands.get(0),operands.get(1),operator));
        operands.clear();
        setResult(res);
        return res;
    }

    public boolean getResultOut()
    {
        return this.resultOut;
    }

    public void setResultOut(boolean result_out)
    {
        this.resultOut = result_out;
    }

}
