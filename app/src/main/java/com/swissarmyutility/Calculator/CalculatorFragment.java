package com.swissarmyutility.Calculator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;
import com.swissarmyutility.globalnavigation.SlidingMenuFragmentActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */
public class CalculatorFragment extends AppFragment  {

    UpdateResult result;
    EditText tvResult;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //return the view here
        View view = inflater.inflate(R.layout.calculator_fragment,null);
        tvResult = (EditText) view.findViewById(R.id.tvResult);
        return view;
    }

    @Override
    public void onDestroy() {
        if(getActivity() instanceof SlidingMenuFragmentActivity)
            ((SlidingMenuFragmentActivity )getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        super.onDestroy();
    }

    private void setOnClickListener(View.OnClickListener clickListener) {
        getView().findViewById(R.id.btnAdd).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnClear).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnDivide).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnDot).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnMultiply).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnSubtract).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnOne).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnTwo).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnThree).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnFour).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnFive).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnSix).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnSeven).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnEight).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnEquals).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnNine).setOnClickListener(clickListener);
        getView().findViewById(R.id.btnZero).setOnClickListener(clickListener);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.setTitle("Calculator");
        super.onActivityCreated(savedInstanceState);
        result = new UpdateResult();
        tvResult.setText("");
        setOnClickListener(clickListener);
        if(getActivity() instanceof SlidingMenuFragmentActivity)
            ((SlidingMenuFragmentActivity )getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.btnAdd :
                    if(!tvResult.getText().toString().equals("")) {
                        result.setOperator('+');
                        if(result.resultOut)
                        {
                            result.resultOut=false;
                            tvResult.setText("");
                            result.setResult("");
                            return;
                        }
                        if(result.operands.size()<2)
                        result.addOperand(tvResult.getText().toString());
                        else
                        result.performOperation(result.operator);
                        tvResult.setText("");
                        result.setResult("");
                    }
                    break;
                case R.id.btnClear :
                    result.clearText();
                    result.resultOut=false;
                    break;
                case R.id.btnDivide :
                    if(!tvResult.getText().toString().equals("")) {
                        result.setOperator('/');
                        if(result.resultOut)
                        {
                            result.resultOut=false;
                            tvResult.setText("");
                            result.setResult("");
                            return;
                        }
                        if(result.operands.size()<2)
                            result.addOperand(tvResult.getText().toString());
                        else
                            result.performOperation(result.operator);
                        tvResult.setText("");
                        result.setResult("");
                    }
                    break;
                case R.id.btnDot :
                    if(!tvResult.getText().toString().contains("."))
                    result.appendResult(".");
                    break;
                case R.id.btnEquals :
                    if(result.operands.size()==0)
                    {
                        return;
                    }
                    else if(result.operands.size()<2)
                        result.addOperand(tvResult.getText().toString());
                    else
                        result.performOperation(result.operator);
                    tvResult.setText("");
                    result.setResult("");
                    result.printResult();
                    break;
                case R.id.btnMultiply :
                    if(!tvResult.getText().toString().equals("")) {
                        result.setOperator('x');
                        if(result.resultOut)
                        {
                            result.resultOut=false;
                            tvResult.setText("");
                            result.setResult("");
                            return;
                        }
                        if(result.operands.size()<2)
                            result.addOperand(tvResult.getText().toString());
                        else
                            result.performOperation(result.operator);
                        tvResult.setText("");
                        result.setResult("");
                    }
                    break;
                case R.id.btnSubtract :
                    if(!tvResult.getText().toString().equals("")) {
                        result.setOperator('-');
                        if(result.resultOut)
                        {
                            result.resultOut=false;
                            tvResult.setText("");
                            result.setResult("");
                            return;
                        }
                        if(result.operands.size()<2)
                            result.addOperand(tvResult.getText().toString());
                        else
                            result.performOperation(result.operator);
                        tvResult.setText("");
                        result.setResult("");
                    }
                    break;
                case R.id.btnZero :
                    result.appendResult("0");
                    break;
                case R.id.btnOne :
                    result.appendResult("1");
                    break;
                case R.id.btnTwo :
                    result.appendResult("2");
                    break;
                case R.id.btnThree :
                    result.appendResult("3");
                    break;
                case R.id.btnFour :
                    result.appendResult("4");
                    break;
                case R.id.btnFive :
                    result.appendResult("5");
                    break;
                case R.id.btnSix :
                    result.appendResult("6");
                    break;
                case R.id.btnSeven :
                    result.appendResult("7");
                    break;
                case R.id.btnEight :
                    result.appendResult("8");
                    break;
                case R.id.btnNine :
                    result.appendResult("9");
                    break;
            }
        }
    };

    class UpdateResult {

        private String result="";
        char operator;
        ArrayList<Double> operands;
        boolean resultOut;

        public UpdateResult()
        {
            operands = new ArrayList<Double>();
        }

        public  String getResult()    {   return this.result;   }



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
            updateTextField(this.result);
        }

        public void setOperator(char operator)
        {
            this.operator = operator;
        }

        public void addOperand(String operand)
        {
            try {
                if (!operand.equals("") && operand!=null)
                    operands.add(Double.parseDouble(operand));
            }
            catch (NumberFormatException e)
            {
                e.printStackTrace();
            }
        }

        public String performOperation(char operator)
        {
            String res="0.0";
            if(operands.size()==2) {
                if (operator == '+')
                    res = String.valueOf(operands.get(0) + operands.get(1));
                if (operator == '-')
                    res = String.valueOf(operands.get(0) - operands.get(1));
                if (operator == '/')
                    res = String.valueOf(operands.get(0) / operands.get(1));
                if (operator == 'x')
                    res = String.valueOf(operands.get(0) * operands.get(1));
            }
            operands.clear();

            return res;
        }

        private void updateTextField(String resultString)
        {
            tvResult.setText(resultString);
        }

        private void clearText()
        {
            tvResult.setText("");
            setResult("");
            operands.clear();
        }
        private void printResult()
        {
            this.result = performOperation(operator);
            tvResult.setText(this.result);
            operands.add(Double.parseDouble(this.result));
            resultOut = true;
        }
    }
}
