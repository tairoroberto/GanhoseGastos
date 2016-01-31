package br.com.trmasolucoes.ganhosegastos;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
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

import br.com.trmasolucoes.ganhosegastos.adapters.ListaHistoricoGanhosMesAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.editar.EditarGanhoActivity;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;

import static android.view.Gravity.START;


public class HistoricoGanhosMesActivity extends ActionBarActivity {

    private TextView txtMesGanhos;
    private ListView listView;
    CategoriaGanho categoriaGanho = new CategoriaGanho();


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
                categoriaGanho= bundle.getParcelable("categoria");
            }
        }


        txtMesGanhos.setText(DateUtil.getMes().toUpperCase());


        GanhosDAO ganhosDAO = new GanhosDAO(HistoricoGanhosMesActivity.this);

        final List<Ganho> ganhos = ganhosDAO.buscarMesPorCategoria(categoriaGanho.getId());

        listView.setAdapter(new ListaHistoricoGanhosMesAdapter(HistoricoGanhosMesActivity.this,ganhos));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(HistoricoGanhosMesActivity.this,EditarGanhoActivity.class);
                intent.putExtra("ganho",ganhos.get(position));
                startActivity(intent);
            }
        });

    }
}
