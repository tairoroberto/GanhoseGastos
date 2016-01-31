package br.com.trmasolucoes.ganhosegastos;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import br.com.trmasolucoes.ganhosegastos.adapters.ListaHistoricoGastosMesAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.editar.EditarGastoActivity;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;

import static android.view.Gravity.START;


public class HistoricoGastosMesActivity extends ActionBarActivity {


    private TextView txtMesGanhos;
    private ListView listView;
    private CategoriaGasto categoriaGasto = new CategoriaGasto();

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
                categoriaGasto= bundle.getParcelable("categoria");
            }
        }

        txtMesGanhos.setText(DateUtil.getMes().toUpperCase());

        GastosDAO gastosDAO = new GastosDAO(HistoricoGastosMesActivity.this);


        final List<Gasto> gastos = gastosDAO.buscarMesPorCategoria(categoriaGasto.getId());

        listView.setAdapter(new ListaHistoricoGastosMesAdapter(HistoricoGastosMesActivity.this,gastos));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoricoGastosMesActivity.this,EditarGastoActivity.class);
                intent.putExtra("gasto",gastos.get(position));
                startActivity(intent);
            }
        });

    }
}
