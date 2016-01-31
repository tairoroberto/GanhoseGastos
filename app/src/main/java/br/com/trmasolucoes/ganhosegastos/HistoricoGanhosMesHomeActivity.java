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

import br.com.trmasolucoes.ganhosegastos.adapters.ListaHistoricoGanhosMesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.editar.EditarGanhoActivity;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class HistoricoGanhosMesHomeActivity extends ActionBarActivity {

    private TextView txtMesGanhos;
    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);


        listView = (ListView) findViewById(R.id.listView);
        txtMesGanhos = (TextView)findViewById(R.id.txtMes);



        txtMesGanhos.setText(DateUtil.getMes().toUpperCase());


        GanhosDAO ganhosDAO = new GanhosDAO(HistoricoGanhosMesHomeActivity.this);

        final List<Ganho> ganhos = ganhosDAO.buscarMes();

        listView.setAdapter(new ListaHistoricoGanhosMesAdapter(HistoricoGanhosMesHomeActivity.this,ganhos));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoricoGanhosMesHomeActivity.this,EditarGanhoActivity.class);
                intent.putExtra("ganho",ganhos.get(position));
                startActivity(intent);
            }
        });

    }
}
