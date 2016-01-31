package br.com.trmasolucoes.ganhosegastos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.font.FontAwesome;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.adapters.ListaHomeCategoriasAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.FormasDePagamentoDAO;
import br.com.trmasolucoes.ganhosegastos.domain.FundoCaixaDao;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;
import br.com.trmasolucoes.ganhosegastos.model.FundoCaixa;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;

/**
 * Created by tairo on 19/03/15.
 */
public class HomeActivity extends FragmentActivity {
    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;
    private Dialog dialog;
    private EditText edtValor;

    private ListView listaOpcoes;
    private LinearLayout btnGanhos;
    private LinearLayout btnGastos;
    private ListView listaHomeCategorias;
    private TextView txtValorGanhosHome;
    private TextView txtValorGastosHome;
    private TextView txtValorTotalHome;
    private AwesomeTextView txtIconeValorTotalHome;

    private AwesomeTextView iconeGanhosHome;
    private TextView txtTextoGanhosHome;

    private AwesomeTextView iconeGastosHome;
    private TextView txtTextoGastosHome;

    private LinearLayout content_list;

    private NumberFormat numberFormat = new DecimalFormat ("#,##0.00", new DecimalFormatSymbols());
    private String symbol = numberFormat.getCurrency().getSymbol();
    private BigDecimal valorGanhos;
    private BigDecimal valorGastos;

    //FragmentManager para gerenciar os fragments
    FragmentManager fragmentManager = getSupportFragmentManager();

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


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



        listaOpcoes = (ListView) findViewById(R.id.drawer_content);
        btnGanhos = (LinearLayout)findViewById(R.id.btnGanhos);
        btnGastos = (LinearLayout)findViewById(R.id.btnGastos);
        listaHomeCategorias = (ListView) findViewById(R.id.listaHomeCategorias);
        txtValorGanhosHome = (TextView)findViewById(R.id.txtValorGanhosHome);
        txtValorGastosHome = (TextView)findViewById(R.id.txtValorGastosHome);
        txtValorTotalHome = (TextView)findViewById(R.id.txtValorTotalHome);
        txtIconeValorTotalHome = (AwesomeTextView)findViewById(R.id.txtIconeValorTotalHome);
        content_list = (LinearLayout)findViewById(R.id.content_list);

        iconeGanhosHome = (AwesomeTextView)findViewById(R.id.iconeGanhosHome);
        txtTextoGanhosHome = (TextView)findViewById(R.id.txtTextoGanhosHome);
        iconeGastosHome = (AwesomeTextView)findViewById(R.id.iconeGastosHome);
        txtTextoGastosHome = (TextView)findViewById(R.id.txtTextoGastosHome);


        listaOpcoes.setAdapter(new ListaOpcoesAdapter(HomeActivity.this,resources.getStringArray(R.array.array_opcoes)));
        listaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                   /* Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);*/
                }else if (position == 1){
                    Intent intent = new Intent(HomeActivity.this,GraficosListaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 2){
                    Intent intent = new Intent(HomeActivity.this,ListaCategoriaGanhoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 3){
                    Intent intent = new Intent(HomeActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 4){
                    Intent intent = new Intent(HomeActivity.this,ListaFormaPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 5){
                    Intent intent = new Intent(HomeActivity.this,BackupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });



        btnGanhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GanhosActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
            }
        });


        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, GastosActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_left_enter, R.anim.push_left_exit);
            }
        });


        txtValorGanhosHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HistoricoGanhosMesHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        txtValorGastosHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this,HistoricoGastosMesHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        GanhosDAO ganhosDAO = new GanhosDAO(HomeActivity.this);
        GastosDAO gastosDAO = new GastosDAO(HomeActivity.this);
        CategoriaGanhosDAO categoriaGanhosDAO = new CategoriaGanhosDAO(HomeActivity.this);
        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(HomeActivity.this);
        FormasDePagamentoDAO formasDePagamentoDAO = new FormasDePagamentoDAO(HomeActivity.this);

        List<CategoriaGanho> categoriaGanhosTest = categoriaGanhosDAO.buscar();
        List<CategoriaGasto> categoriaGastosTest = categoriaGastosDAO.buscar();
        List<FormaPagamento> formaPagamentosTest = formasDePagamentoDAO.buscar();

       /*verifica se as categorias estão cadastradas, senão chamas as categorias default para inclui-las na base de dados*/
        if (categoriaGanhosTest.isEmpty()){
            categoriaGanhosDAO.inserirDefault();
        }
        if (categoriaGastosTest.isEmpty()){
            categoriaGastosDAO.inserirDefault();
        }
        if (formaPagamentosTest.isEmpty()){
            formasDePagamentoDAO.inserirDefault();
        }


        /*Pega os valores dos gastos e ganhos do mes corrente*/
        double valorGanhosMes = Double.parseDouble(ganhosDAO.getTotalGanhosMes().toString());
        double valorGastosMes = Double.parseDouble(gastosDAO.getTotalGastosMes().toString());
        double valorTotalMes = valorGanhosMes - valorGastosMes;

        /*Logica para não mostrar icones e texto se não tiver nada cadastrado*/
        if (valorGanhosMes > 0){
            iconeGanhosHome.setVisibility(View.VISIBLE);
            txtTextoGanhosHome.setVisibility(View.VISIBLE);
            txtValorGanhosHome.setVisibility(View.VISIBLE);
            txtValorGanhosHome.setText(symbol + numberFormat.format(ganhosDAO.getTotalGanhosMes()));
        }else {
            iconeGanhosHome.setVisibility(View.INVISIBLE);
            txtTextoGanhosHome.setVisibility(View.INVISIBLE);
            txtValorGanhosHome.setVisibility(View.INVISIBLE);
        }

        if (valorGastosMes > 0){
            iconeGastosHome.setVisibility(View.VISIBLE);
            txtTextoGastosHome.setVisibility(View.VISIBLE);
            txtValorGastosHome.setText(symbol + numberFormat.format(gastosDAO.getTotalGastosMes()));
        }else {
            iconeGastosHome.setVisibility(View.INVISIBLE);
            txtTextoGastosHome.setVisibility(View.INVISIBLE);
            txtValorGastosHome.setVisibility(View.INVISIBLE);
        }

        /*Se ganhos e gastos forem menores que 0, nã mostra icones de tota e separador*/
        if (valorGanhosMes > 0 || valorGastosMes > 0){
            txtIconeValorTotalHome.setVisibility(View.VISIBLE);
            txtValorTotalHome.setVisibility(View.VISIBLE);
            findViewById(R.id.separador).setVisibility(View.VISIBLE);

        }else{
            txtIconeValorTotalHome.setVisibility(View.INVISIBLE);
            txtValorTotalHome.setVisibility(View.INVISIBLE);
            findViewById(R.id.separador).setVisibility(View.INVISIBLE);
        }




        /*Se Ganhos for maior que Gastos Mostra icone feliz, senão mostra o triste*/
        if (valorGanhosMes >= valorGastosMes){
            txtIconeValorTotalHome.setFontAwesomeIcon(FontAwesome.FA_SMILE_O);
        }else{
            txtIconeValorTotalHome.setFontAwesomeIcon(FontAwesome.FA_THUMBS_O_DOWN);
            txtIconeValorTotalHome.setTextColor(Color.parseColor("#DC143C"));
            txtValorTotalHome.setTextColor(Color.parseColor("#DC143C"));
        }

        txtValorTotalHome.setText(symbol + numberFormat.format(valorTotalMes));



        final List<CategoriaGasto> categoriasGastos = categoriaGastosDAO.buscarComValores();

        ListaHomeCategoriasAdapter adapter = new ListaHomeCategoriasAdapter(HomeActivity.this,categoriasGastos);

        listaHomeCategorias.setAdapter(adapter);

        /*
        *evento de click no listviw  de categorias de gasto da homeActivity
        * ao clicar faz o reple do linear layout por outra lista, com os gastos cadastrados
        */
        listaHomeCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HomeListaCategoriaFragment fragment = new HomeListaCategoriaFragment();
                /*Dados para enviar ao fragment com a lista de gasto*/
                Bundle args = new Bundle();
                args.putParcelable("categoriaGasto",categoriasGastos.get(position));
                fragment.setArguments(args);

                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fade_in_enter,R.anim.push_right_exit,R.anim.fade_in_enter,R.anim.push_right_exit);
                transaction.replace(R.id.content_list, fragment,"fragment");
                transaction.addToBackStack("pilha");
                transaction.commit();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(HomeActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        if (id == R.id.action_add_fundo_caixa){

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage("Deseja realmente adicionar os GANHOS e GASTOS do mês anterior aos deste mês?")
                    .setTitle("Atenção")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            GregorianCalendar gc = (GregorianCalendar) GregorianCalendar.getInstance();
                            gc.add(Calendar.MONTH, -1);
                            gc.set(Calendar.DAY_OF_MONTH, 1);

                            valorGanhos = new GanhosDAO(HomeActivity.this).getTotalGanhosMes(gc.getTime());
                            valorGastos = new GastosDAO(HomeActivity.this).getTotalGastosMes(gc.getTime());

                            Log.i("Script","Valor Ganhos: " + valorGanhos);
                            Log.i("Script","Valor Gastos: " + valorGastos);

                            Ganho ganho = new Ganho();
                            Gasto gasto = new Gasto();
                            FormaPagamento formaPagamento;
                            CategoriaGanho categoriaGanho;
                            CategoriaGasto categoriaGasto;
                            FundoCaixa fundoCaixa;
                            GanhosDAO ganhosDAO = new GanhosDAO(HomeActivity.this);
                            GastosDAO gastosDAO = new GastosDAO(HomeActivity.this);
                            FundoCaixaDao fundoCaixaDao = new FundoCaixaDao(HomeActivity.this);

                            categoriaGanho = new CategoriaGanhosDAO(HomeActivity.this).getCategoriaOutros();
                            fundoCaixa = fundoCaixaDao.getFundoCaixaMes(gc.getTime());


                            if (valorGanhos.compareTo(valorGastos) > 0){

                                if (fundoCaixa.getValorFundo() == null){
                                    categoriaGanho = new CategoriaGanhosDAO(HomeActivity.this).getCategoriaOutros();
                                    ganho.setValor(valorGanhos.subtract(valorGastos));
                                    ganho.setDataPagamento(DateUtil.getDataHoje());
                                    ganho.setDataCadastro(DateUtil.getDataHoje());
                                    ganho.setComentarios("Ganhos do mês anterior");
                                    ganho.setCategoria((int) categoriaGanho.getId());
                                    ganhosDAO.inserir(ganho);

                                    fundoCaixa.setDataIclusao(gc.getTime());
                                    fundoCaixa.setValorFundo(valorGanhos.subtract(valorGastos));
                                    fundoCaixaDao.inserir(fundoCaixa);
                                }

                            }else {

                                if (fundoCaixa.getValorFundo() == null){
                                    formaPagamento = new FormasDePagamentoDAO(HomeActivity.this).getCategoriaOutros();
                                    categoriaGasto = new CategoriaGastosDAO(HomeActivity.this).getCategoriaOutros();
                                    gasto.setValor(valorGastos.subtract(valorGanhos));
                                    gasto.setDataVencimento(DateUtil.getDataHoje());
                                    gasto.setDataPagamento(DateUtil.getDataHoje());
                                    gasto.setPago(false);
                                    gasto.setDataCadastro(DateUtil.getDataHoje());
                                    gasto.setComentarios("Gastos do mês anterior");
                                    gasto.setCategoria((int)categoriaGasto.getId());
                                    gasto.setFormaPagamento((int) formaPagamento.getId());
                                    gastosDAO.inserir(gasto);

                                    fundoCaixa.setDataIclusao(gc.getTime());
                                    fundoCaixa.setValorFundo(valorGanhos.subtract(valorGastos));
                                    fundoCaixaDao.inserir(fundoCaixa);
                                }

                            }

                            Intent intent = new Intent(HomeActivity.this,HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        }
                    })
                    .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    });

            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

}
