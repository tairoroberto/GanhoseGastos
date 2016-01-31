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
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class HistoricoGastosMesHomeActivity extends ActionBarActivity {


    private TextView txtMesGanhos;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


        listView = (ListView) findViewById(R.id.listView);
        txtMesGanhos = (TextView)findViewById(R.id.txtMes);



        txtMesGanhos.setText(DateUtil.getMes().toUpperCase());

        GastosDAO gastosDAO = new GastosDAO(HistoricoGastosMesHomeActivity.this);


        final List<Gasto> gastos = gastosDAO.buscarMes();

        listView.setAdapter(new ListaHistoricoGastosMesAdapter(HistoricoGastosMesHomeActivity.this,gastos));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoricoGastosMesHomeActivity.this,EditarGastoActivity.class);
                intent.putExtra("gasto",gastos.get(position));
                startActivity(intent);
            }
        });

    }
}
