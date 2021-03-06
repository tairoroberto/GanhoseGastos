package br.com.trmasolucoes.ganhosegastos;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import br.com.trmasolucoes.ganhosegastos.adapters.ListaCategoriaGastoAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.editar.EditarGategoriaGastoActivity;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;

import static android.view.Gravity.START;


public class ListaCategoriaGastoActivity extends ActionBarActivity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    private ListView listCategoria;
    private ImageButton imgBtnAddCategoriaGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_categoria_gasto);

        /******************************************************************************************/
        /*               Inicio dos método de açao do drawer layout                              */
        /****************************************************************************************/

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ImageView imageView = (ImageView) findViewById(R.id.drawer_indicator);
        final Resources resources = getResources();

        drawerArrowDrawable = new DrawerArrowDrawable(resources);
        drawerArrowDrawable.setStrokeColor(resources.getColor(R.color.light_gray));
        imageView.setImageDrawable(drawerArrowDrawable);

        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override public void onDrawerSlide(View drawerView, float slideOffset) {
                offset = slideOffset;

                // Sometimes slideOffset ends up so close to but not quite 1 or 0.
                if (slideOffset >= .995) {
                    flipped = true;
                    drawerArrowDrawable.setFlip(flipped);
                } else if (slideOffset <= .005) {
                    flipped = false;
                    drawerArrowDrawable.setFlip(flipped);
                }

                drawerArrowDrawable.setParameter(offset);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (drawer.isDrawerVisible(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        /******************************************************************************************/
        /*               Fimo dos método de açao do drawer layout                                */
        /****************************************************************************************/

        ListView listaOpcoes = (ListView) findViewById(R.id.drawer_content);

        listaOpcoes.setAdapter(new ListaOpcoesAdapter(ListaCategoriaGastoActivity.this,resources.getStringArray(R.array.array_opcoes)));
        listaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(ListaCategoriaGastoActivity.this,HomeActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 1){
                    Intent intent = new Intent(ListaCategoriaGastoActivity.this,GraficosListaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 2){
                    Intent intent = new Intent(ListaCategoriaGastoActivity.this,ListaCategoriaGanhoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 3){
                    /*Intent intent = new Intent(ListaCategoriaGastoActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }else if (position == 4){
                    Intent intent = new Intent(ListaCategoriaGastoActivity.this,ListaFormaPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 5){
                    Intent intent = new Intent(ListaCategoriaGastoActivity.this,BackupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        /******************************************************************************************/
        /*               Fimo dos método de açao do drawer layout                                */
        /****************************************************************************************/


        listCategoria = (ListView) findViewById(R.id.listaCategoriaGasto);
        imgBtnAddCategoriaGastos = (ImageButton)findViewById(R.id.imgBtnAdsCategoriaGasto);

        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(ListaCategoriaGastoActivity.this);
        final List<CategoriaGasto> categoriaGastos = categoriaGastosDAO.buscar();

        ListaCategoriaGastoAdapter adapter = new ListaCategoriaGastoAdapter(ListaCategoriaGastoActivity.this,categoriaGastos);

        listCategoria.setAdapter(adapter);

        listCategoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ListaCategoriaGastoActivity.this, EditarGategoriaGastoActivity.class);
                intent.putExtra("categoriaGasto",categoriaGastos.get(position));
                startActivity(intent);
            }
        });


        imgBtnAddCategoriaGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListaCategoriaGastoActivity.this, CategoriaGastosActivity.class);
                startActivity(intent);
            }
        });


    }
}
