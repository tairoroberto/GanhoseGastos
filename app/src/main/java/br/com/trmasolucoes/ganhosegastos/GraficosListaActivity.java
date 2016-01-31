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

import br.com.trmasolucoes.ganhosegastos.adapters.ListaGraficosAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.graficos.GraficoAnualActivity;
import br.com.trmasolucoes.ganhosegastos.graficos.GraficoFormasPagamentoActivity;
import br.com.trmasolucoes.ganhosegastos.graficos.GraficoGanhosActivity;
import br.com.trmasolucoes.ganhosegastos.graficos.GraficoGastoActivity;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;

import static android.view.Gravity.START;


public class GraficosListaActivity extends ActionBarActivity {


    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    //Dialog  e Text view para o fragment da  calculadora
    private Dialog dialog;
    private EditText edtValor;

    public static final String DATEPICKER_TAG = "datepicker";

    private Button btnDataGasto;

    private TextView txtMesGrafico;
    private ListView listViewGraficos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graficos);

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
                if (drawer.isDrawerVisible(START)) {
                    drawer.closeDrawer(START);
                } else {
                    drawer.openDrawer(START);
                }
            }
        });
        /******************************************************************************************/
        /*               Fim dos método de açao do drawer layout                                 */
        /****************************************************************************************/

        ListView listaOpcoes = (ListView) findViewById(R.id.drawer_content);
        listaOpcoes.setAdapter(new ListaOpcoesAdapter(GraficosListaActivity.this,resources.getStringArray(R.array.array_opcoes)));
        listaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(GraficosListaActivity.this,HomeActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 1){
                   /* Intent intent = new Intent(GraficosListaActivity.this,GraficosListaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }else if (position == 2){
                    Intent intent = new Intent(GraficosListaActivity.this,ListaCategoriaGanhoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 3){
                    Intent intent = new Intent(GraficosListaActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 4){
                    Intent intent = new Intent(GraficosListaActivity.this,ListaFormaPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 5){
                    Intent intent = new Intent(GraficosListaActivity.this,BackupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        /******************************************************************************************/
        /*               Inicio doa metodos de graficos                                          */
        /****************************************************************************************/

        txtMesGrafico = (TextView) findViewById(R.id.txtMesGrafico);
        listViewGraficos = (ListView) findViewById(R.id.listViewGraficos);

        String[] lista = {"Ganhos","Gastos"," Formas de pagamento","Gafico anual"};

        listViewGraficos.setAdapter( new ListaGraficosAdapter(GraficosListaActivity.this,lista));
        txtMesGrafico.setText(DateUtil.getMes().toUpperCase());

        listViewGraficos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(GraficosListaActivity.this,GraficoGanhosActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 1){
                    Intent intent = new Intent(GraficosListaActivity.this,GraficoGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (position == 2){
                    Intent intent = new Intent(GraficosListaActivity.this,GraficoFormasPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else if (position == 3){
                    Intent intent = new Intent(GraficosListaActivity.this,GraficoAnualActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }

            }
        });
    }


    public void chamaGraficos(View view){
        Intent intent = new Intent(GraficosListaActivity.this,PieChartActivity.class);
        startActivity(intent);
    }
}
