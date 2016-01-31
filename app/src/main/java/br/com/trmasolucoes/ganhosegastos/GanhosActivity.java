package br.com.trmasolucoes.ganhosegastos;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.adapters.ItemListCategoriaGanhosAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DialogCalculadora;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;

import static android.view.Gravity.START;


public class GanhosActivity extends ActionBarActivity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    //Dialog  e Text view para o fragment da  calculadora
    private Dialog dialog;
    private EditText edtValor;
    private ListView listaOpcoes ;
    private TextView txtValorGanho ;
    private ListView listaCategorias;
    private LinearLayout btnSalvar;
    private LinearLayout btnCancelar;
    private Button btnDataGanho;
    private EditText edtComentariosGanho;
    public static final String DATEPICKER_TAG = "datepicker";
    private int positionCategoria = -1;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ganhos);

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
        /*               Fim dos método de açao do drawer layout                                 */
        /****************************************************************************************/

        listaOpcoes = (ListView) findViewById(R.id.drawer_content);
        txtValorGanho = (TextView)findViewById(R.id.txtValorGanho);
        //lista com as ctegorias
        listaCategorias = (ListView)findViewById(R.id.listaCategorias);
        btnSalvar = (LinearLayout) findViewById(R.id.btnSalvarGanhos);
        btnCancelar = (LinearLayout) findViewById(R.id.btnCancelarGanhos);
        btnDataGanho = (Button) findViewById(R.id.btnDataGanho);
        edtComentariosGanho = (EditText) findViewById(R.id.edtComentarioGanho);


        listaOpcoes.setAdapter(new ListaOpcoesAdapter(GanhosActivity.this,resources.getStringArray(R.array.array_opcoes)));
        listaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(GanhosActivity.this,HomeActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 1){
                    Intent intent = new Intent(GanhosActivity.this,GraficosListaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 2){
                    Intent intent = new Intent(GanhosActivity.this,ListaCategoriaGanhoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 3){
                    Intent intent = new Intent(GanhosActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 4){
                    Intent intent = new Intent(GanhosActivity.this,ListaFormaPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 5){
                    Intent intent = new Intent(GanhosActivity.this,BackupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });



        //txtview qu epega o valor digitado no fragment da calculadora

        //chama o frgment da calculadora
        DialogCalculadora dialogCalculadora = new DialogCalculadora(GanhosActivity.this,txtValorGanho);
        dialogCalculadora.chamaNumeros();

        txtValorGanho.setText(numberFormat.getCurrency().getSymbol()+"0,00");

        txtValorGanho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o frgment da calculadora
                DialogCalculadora dialogCalculadora = new DialogCalculadora(GanhosActivity.this,txtValorGanho);
                dialogCalculadora.chamaNumeros();
            }
        });



        /*Chama o Data Acess Object que faz a transação no banco de dados*/
        CategoriaGanhosDAO categoriaGanhosDAO = new CategoriaGanhosDAO(GanhosActivity.this);


        //list que irá carregar as categorias
        final List<CategoriaGanho> categorias = categoriaGanhosDAO.buscar();

        //adpter que irá peecher a lista de categorias
        final ItemListCategoriaGanhosAdapter adapter = new ItemListCategoriaGanhosAdapter(this, categorias);
        listaCategorias.setAdapter(adapter);


        //implementação de click na lista
        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                positionCategoria = position;
                Toast.makeText(GanhosActivity.this, categorias.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


        // setup listener for a date change:
        btnDataGanho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialogDataVencimento = DatePickerDialog.newInstance(new GetDataSetDataPagamento(),
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), true);

                datePickerDialogDataVencimento.setVibrate(true);
                datePickerDialogDataVencimento.setYearRange(1985, 2028);
                datePickerDialogDataVencimento.setCloseOnSingleTapDay(true);
                datePickerDialogDataVencimento.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });



        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GanhosActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                GanhosActivity.this.finish();
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);

            }
        });



        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtValorGanho.getText().toString().equals(numberFormat.getCurrency().getSymbol() + "0,00")){
                    Toast.makeText(GanhosActivity.this, "Insira um valor para o ganho!",Toast.LENGTH_SHORT).show();

                }else  if (positionCategoria == -1){
                    Toast.makeText(GanhosActivity.this, "Selecione uma categoria!",Toast.LENGTH_SHORT).show();

                }else  if (btnDataGanho.getText().toString().equals("Selecione a data")){
                    Toast.makeText(GanhosActivity.this, "Selecione a data de pagamento do ganho!",Toast.LENGTH_SHORT).show();

                }else  if (edtComentariosGanho.getText().toString().equals("")){
                    Toast.makeText(GanhosActivity.this, "Insira uma descrição para o ganho!",Toast.LENGTH_SHORT).show();

                }else{

                    Ganho ganho = new Ganho();

                    String valor = txtValorGanho.getText().toString().replace("R","");
                    valor = valor.replace("$","");


                    try {
                        Number number = null;
                        number = numberFormat.parse(valor);
                        ganho.setValor(BigDecimal.valueOf(number.doubleValue()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ganho.setDataPagamento(formataData(btnDataGanho.getText().toString()));


                    ganho.setDataCadastro(getDataHoje());

                    ganho.setComentarios(edtComentariosGanho.getText().toString());

                    ganho.setCategoria((int) categorias.get(positionCategoria).getId());

                    GanhosDAO ganhosDAO = new GanhosDAO(GanhosActivity.this);

                    ganhosDAO.inserir(ganho);

                    Log.i("Script", "Valor: " + valor);
                    Log.i("Script","Valorde cadastro: " + String.valueOf(ganho.getValor()));
                    Log.i("Script","Data pagamneto: " + getDateToString(ganho.getDataPagamento()));
                    Log.i("Script","Data cadastro: " + getDateToString(ganho.getDataCadastro()));
                    Log.i("Script","Descrição: " + ganho.getComentarios());
                    Log.i("Script","Categoria: " + ganho.getCategoria()+", "+categorias.get(positionCategoria).getTitle()+", "+categorias.get(positionCategoria).getId());


                    Intent intent = new Intent(GanhosActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    GanhosActivity.this.finish();
                    overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
                }
            }
        });


        /******************************************************************************************/
        /*               verifica se há alguma instancia salvapra recuperar os valores           */
        /****************************************************************************************/
        if (savedInstanceState != null){
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(new GetDataSetDataPagamento());
            }
        }

    }


    private class GetDataSetDataPagamento extends FragmentActivity implements  DatePickerDialog.OnDateSetListener{

        private GetDataSetDataPagamento() {
        }

        @Override
        public void onDateSet(DatePickerDialog datePickerDialog, int year, int monthOfYear, int dayOfMonth) {

            String mes= "";
            String dia= "";

            if ((monthOfYear + 1) < 10){
                mes = "0"+(monthOfYear + 1);
            }else {
                mes = ""+(monthOfYear + 1);
            }

            if(dayOfMonth < 10){
                dia = "0"+dayOfMonth;
            }else{
                dia = ""+dayOfMonth;
            }

            btnDataGanho.setText( dia + "-" + mes  + "-" + year);
        }
    }


    private Date formataData(String data) {
        if (data == null || data.equals(""))
            return null;

        Date date = new Date();
        try {
            data = data + " 00:00:00";
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//SimpleDateFormat("dd/MM/yyyy");
            date = formatter.parse(data);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    private Date getDataHoje(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    private String getDateToString(Date date){
        if (date == null || date.equals(""))
            return null;

        DateFormat formatador = new SimpleDateFormat("dd-MM-yyyy");
        return formatador.format(date);
    }

}
