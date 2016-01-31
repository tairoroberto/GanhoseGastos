package br.com.trmasolucoes.ganhosegastos.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;

import br.com.trmasolucoes.ganhosegastos.R;

/**
 * Created by tairo on 20/03/15.
 */
public class DialogCalculadora {
    private Activity activity;
    private Dialog dialog;
    private EditText edtValor;
    private TextView valorSaida;

    private NumberFormat numberFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols());

    public DialogCalculadora(Activity activity, TextView valorSaida) {
        this.activity = activity;
        this.valorSaida = valorSaida;
    }

    //metodo qu echmas o fragment de calculadora e inicia os numeros com a mascara
    public void chamaNumeros(){

        dialog = new Dialog(this.activity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /* inflando o layout pra criação do DialogFragment*/
        dialog.setContentView(R.layout.fragment_calculadora);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        //.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();


        //editText que possui o valor a ser armazenado
        edtValor = (EditText) dialog.findViewById(R.id.edtValor);
        edtValor.setText("");

        //add o listener para inserir a mascara
        edtValor.addTextChangedListener(new MascaraMonetaria(edtValor));

        Button btn1 = (Button) dialog.findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "1");
            }
        });

        Button btn2 = (Button) dialog.findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "2");
            }
        });

        Button btn3 = (Button) dialog.findViewById(R.id.btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "3");
            }
        });

        Button btn4 = (Button) dialog.findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "4");
            }
        });

        Button btn5 = (Button) dialog.findViewById(R.id.btn5);
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "5");
            }
        });

        Button btn6 = (Button) dialog.findViewById(R.id.btn6);
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "6");
            }
        });

        Button btn7 = (Button) dialog.findViewById(R.id.btn7);
        btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "7");
            }
        });

        Button btn8 = (Button) dialog.findViewById(R.id.btn8);
        btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "8");
            }
        });

        Button btn9 = (Button) dialog.findViewById(R.id.btn9);
        btn9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "9");
            }
        });

        Button btn0 = (Button) dialog.findViewById(R.id.btn0);
        btn0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText(old_value + "0");
            }
        });

        ImageView btnDeletar = (ImageView) dialog.findViewById(R.id.btnDeletar);
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_value = edtValor.getText().toString();
                edtValor.setText("");
            }
        });

        Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtValor.getText().toString().equals("")){
                    setValor(numberFormat.getCurrency().getSymbol()+"0,00");
                }else{
                    setValor(edtValor.getText().toString());
                }

                dialog.dismiss();
            }
        });

        Button btnCancelar = (Button) dialog.findViewById(R.id.btnCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtValor.setText("");
                setValor(numberFormat.getCurrency().getSymbol()+"0,00");
                dialog.dismiss();
            }
        });
    }

    public String getValor() {
        return edtValor.getText().toString();
    }

    public void setValor(String valor) {
        this.valorSaida.setText(valor);
    }
}
