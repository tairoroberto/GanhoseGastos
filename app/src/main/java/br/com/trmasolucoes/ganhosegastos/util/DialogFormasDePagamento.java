package br.com.trmasolucoes.ganhosegastos.util;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.adapters.ItemListFormaPagamentoAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.FormasDePagamentoDAO;
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;

/**
 * Created by tairo on 20/03/15.
 */
public class DialogFormasDePagamento {
    private Activity activity;
    private Dialog dialog;
    private ListView listViewFormaPagamento;
    private Button btnvalorSaida;
    private FormaPagamento formaPagamento;

    public DialogFormasDePagamento(Activity activity, Button btnvalorSaida, FormaPagamento formaPagamento) {
        this.activity = activity;
        this.btnvalorSaida = btnvalorSaida;
        this.formaPagamento = formaPagamento;
    }

    //metodo qu echmas o fragment de calculadora e inicia os numeros com a mascara
    public void chamaFormaDePagamento(){

        dialog = new Dialog(this.activity);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        /* inflando o layout pra criação do DialogFragment*/
        dialog.setContentView(R.layout.fragment_forma_pagamento);
        //dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        dialog.setTitle("Formas de pagamento");

        //.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();

        final FormasDePagamentoDAO formasDePagamentoDAO = new FormasDePagamentoDAO(this.activity);
        final List<FormaPagamento> formasPagamentos = formasDePagamentoDAO.buscar();

        //editText que possui o valor a ser armazenado
        listViewFormaPagamento = (ListView) dialog.findViewById(R.id.listViewFormaPagamento);
        listViewFormaPagamento.setAdapter(new ItemListFormaPagamentoAdapter(this.activity,formasPagamentos));


        listViewFormaPagamento.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                btnvalorSaida.setText(formasPagamentos.get(position).getTitle());
                formaPagamento.setId(formasPagamentos.get(position).getId());
                formaPagamento.setTitle(formasPagamentos.get(position).getTitle());
                formaPagamento.setCor(formasPagamentos.get(position).getCor());
                dialog.dismiss();
            }
        });

    }
}
