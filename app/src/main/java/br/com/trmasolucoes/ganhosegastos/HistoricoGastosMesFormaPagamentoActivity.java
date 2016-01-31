package br.com.trmasolucoes.ganhosegastos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.trmasolucoes.ganhosegastos.adapters.ListaHistoricoGastosMesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.editar.EditarGastoActivity;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class HistoricoGastosMesFormaPagamentoActivity extends ActionBarActivity {


    private TextView txtMesGanhos;
    private ListView listView;
    private FormaPagamento formaPagamento = new FormaPagamento();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


        listView = (ListView) findViewById(R.id.listView);
        txtMesGanhos = (TextView)findViewById(R.id.txtMes);

        Intent intent = getIntent();
        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                formaPagamento= bundle.getParcelable("formaPagamento");
            }
        }

        txtMesGanhos.setText(DateUtil.getMes().toUpperCase());

        GastosDAO gastosDAO = new GastosDAO(HistoricoGastosMesFormaPagamentoActivity.this);


        final List<Gasto> gastos = gastosDAO.buscarMesPorFormaPagamento(formaPagamento.getId());

        listView.setAdapter(new ListaHistoricoGastosMesAdapter(HistoricoGastosMesFormaPagamentoActivity.this,gastos));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoricoGastosMesFormaPagamentoActivity.this,EditarGastoActivity.class);
                intent.putExtra("gasto",gastos.get(position));
                startActivity(intent);
            }
        });

    }
}
