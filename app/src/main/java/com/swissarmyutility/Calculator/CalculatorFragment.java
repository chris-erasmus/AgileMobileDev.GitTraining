package com.swissarmyutility.Calculator;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;
import com.swissarmyutility.globalnavigation.SlidingMenuFragmentActivity;


/**
 * Created by Digvesh.Kumar on 16-07-2014.
 */
public class CalculatorFragment extends AppFragment  {

    private Result result;
    private EditText tvResult;
    private UpdateResultTextView resultView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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
        result = new Result();
        resultView = new UpdateResultTextView(tvResult);
        setOnClickListener(clickListener);
        if(getActivity() instanceof SlidingMenuFragmentActivity)
            ((SlidingMenuFragmentActivity )getActivity()).getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
    }

    private void validateOperation(char operator)
    {
        if(!TextUtils.isEmpty(tvResult.getText())) {
            result.setOperator(operator);
            if(result.getResultOut())
            {
                result.setResultOut(false);
                resultView.clearText();
                return;
            }
            if(result.getOperandsSize()<2)
            {
                result.addOperand(tvResult.getText().toString());
                resultView.clearText();
            }
            else
                result.performOperation(result.getOperator());
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId())
            {
                case R.id.btnAdd :
                    validateOperation('+');
                    break;
                case R.id.btnDivide :
                    validateOperation('/');
                    break;
                case R.id.btnMultiply :
                    validateOperation('*');
                    break;
                case R.id.btnSubtract :
                    validateOperation('-');
                    break;
                case R.id.btnClear :
                    resultView.refresh();
                    break;
                case R.id.btnDot :
                    if(!tvResult.getText().toString().contains("."))
                    result.appendResult(".");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnEquals :
                    if(result.getOperandsSize()==0)
                        return;
                    else if(result.getOperandsSize()==2)
                        result.performOperation(result.getOperator());
                    else if(result.getOperandsSize()<2)
                        result.addOperand(tvResult.getText().toString());
                    resultView.printResult();
                    break;
                case R.id.btnZero :
                    result.appendResult("0");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnOne :
                    result.appendResult("1");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnTwo :
                    result.appendResult("2");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnThree :
                    result.appendResult("3");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnFour :
                    result.appendResult("4");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnFive :
                    result.appendResult("5");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnSix :
                    result.appendResult("6");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnSeven :
                    result.appendResult("7");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnEight :
                    result.appendResult("8");
                    resultView.updateTextField(result.getResult());
                    break;
                case R.id.btnNine :
                    result.appendResult("9");
                    resultView.updateTextField(result.getResult());
                    break;
            }
        }
    };

    class UpdateResultTextView {

        EditText etResult;

        public UpdateResultTextView(EditText tvResult)
        {
            this.etResult = tvResult;
        }
        private void updateTextField(String resultString)
        {
            this.etResult.setText(resultString);
        }

        public void clearText()
        {
            this.etResult.setText("");
            result.setResult("");
        }

        public void printResult()
        {
            result.performOperation(result.getOperator());
            updateTextField(result.getResult());
            result.clearOperands();
            result.getOperands().add(result.getResult());
            result.setResultOut(true);
        }

        public void refresh()
        {
            this.etResult.setText("");
            result.setResult("");
            result.clearOperands();
            result.setResultOut(false);
        }
    }
}
