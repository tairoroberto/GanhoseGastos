package br.com.trmasolucoes.ganhosegastos;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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


import br.com.trmasolucoes.ganhosegastos.adapters.ItemListCategoriaGastosAdapter;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaOpcoesAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DialogCalculadora;
import br.com.trmasolucoes.ganhosegastos.util.DrawerArrowDrawable;
import br.com.trmasolucoes.ganhosegastos.util.DialogFormasDePagamento;

import static android.view.Gravity.START;


public class GastosActivity extends FragmentActivity {

    private DrawerArrowDrawable drawerArrowDrawable;
    private float offset;
    private boolean flipped;

    //Dialog  e Text view para o fragment da  calculadora
    private Dialog dialog;
    private EditText edtValor;
    private EditText edtCometariosGasto;

    public static final String DATEPICKER_PAGAMENTO_TAG = "datepicker_pagamento";
    public static final String DATEPICKER_VENCIMENTO_TAG = "datepicker_vencimento";

    private Button btnDataGasto;
    private DatePickerDialog datePickerDialogDataVencimento;
    private DatePickerDialog datePickerDialogDataPagamento;
    private CheckBox checkDataPagamentoGasto;
    private FormaPagamento formaPagamento = new FormaPagamento();
    private int positionCategoria = -1;

    private ListView listaOpcoes ;
    private TextView txtValorGasto;
    private ListView listaCategorias;
    private Button btnFormaPagamento;
    private LinearLayout btnSalvar;
    private LinearLayout btnCancelar;
    private NumberFormat numberFormat = NumberFormat.getNumberInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);

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

        ListView listaOpcoes = (ListView) findViewById(R.id.drawer_content);

        //chama o frgment da calculadora
        txtValorGasto = (TextView)findViewById(R.id.txtValorGasto);
        //lista com as ctegorias
        listaCategorias = (ListView)findViewById(R.id.listaCategorias);
        btnFormaPagamento = (Button) findViewById(R.id.btnFormaPagamento);
        btnSalvar = (LinearLayout) findViewById(R.id.btnSalvarGastos);
        btnCancelar = (LinearLayout) findViewById(R.id.btnCancelarGastos);

        btnDataGasto = (Button) findViewById(R.id.btnDataVencimentoGasto);
        checkDataPagamentoGasto = (CheckBox) findViewById(R.id.checkDataPagamentoGasto);
        edtCometariosGasto = (EditText)findViewById(R.id.edtComentarioGasto);


        listaOpcoes.setAdapter(new ListaOpcoesAdapter(GastosActivity.this,resources.getStringArray(R.array.array_opcoes)));
        listaOpcoes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0){
                    Intent intent = new Intent(GastosActivity.this,HomeActivity.class);
                    //tira todas as atividades da pilha e vai para a home
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 1){
                    Intent intent = new Intent(GastosActivity.this,GraficosListaActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 2){
                    Intent intent = new Intent(GastosActivity.this,ListaCategoriaGanhoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 3){
                    Intent intent = new Intent(GastosActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 4){
                    Intent intent = new Intent(GastosActivity.this,ListaFormaPagamentoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if (position == 5){
                    Intent intent = new Intent(GastosActivity.this,BackupActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        //txtview qu epega o valor digitado no fragment da calculadora


        DialogCalculadora dialogCalculadora = new DialogCalculadora(GastosActivity.this,txtValorGasto);
        dialogCalculadora.chamaNumeros();


        txtValorGasto.setText(numberFormat.getCurrency().getSymbol()+"0,00");

        txtValorGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o frgment da calculadora
                DialogCalculadora dialogCalculadora = new DialogCalculadora(GastosActivity.this,txtValorGasto);
                dialogCalculadora.chamaNumeros();
            }
        });



        /*Chama o Data Acess Object que faz a transação no banco de dados*/
        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(GastosActivity.this);

        //list que irá carregar as categorias
        final List<CategoriaGasto> categorias = categoriaGastosDAO.buscar();


        //adpter que irá peecher a lista de categorias
        final ItemListCategoriaGastosAdapter adapter = new ItemListCategoriaGastosAdapter(this, categorias);
        listaCategorias.setAdapter(adapter);


        //imementaoa de click na lista
        listaCategorias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                positionCategoria = position;
                Toast.makeText(GastosActivity.this,categorias.get(position).getTitle(),Toast.LENGTH_SHORT).show();
            }
        });


        btnFormaPagamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFormasDePagamento dialogFormasDePagamento = new DialogFormasDePagamento(GastosActivity.this,btnFormaPagamento,formaPagamento);
                dialogFormasDePagamento.chamaFormaDePagamento();
            }
        });


        checkDataPagamentoGasto.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Calendar calendar = Calendar.getInstance();

                    DatePickerDialog datePickerDialogDataPagamento = DatePickerDialog.newInstance(new GetDataSetDataPagamento(),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH), true);

                    datePickerDialogDataPagamento.setVibrate(true);
                    datePickerDialogDataPagamento.setYearRange(1985, 2028);
                    datePickerDialogDataPagamento.setCloseOnSingleTapDay(true);
                    datePickerDialogDataPagamento.show(getSupportFragmentManager(), DATEPICKER_PAGAMENTO_TAG);
                }else{
                    checkDataPagamentoGasto.setText("Data pagamento");
                }
            }
        });



        // setup listener for a date change:
        btnDataGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePickerDialogDataVencimento = DatePickerDialog.newInstance(new GetDataSetDataVencimento(),
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), true);

                datePickerDialogDataVencimento.setVibrate(true);
                datePickerDialogDataVencimento.setYearRange(1985, 2028);
                datePickerDialogDataVencimento.setCloseOnSingleTapDay(true);
                datePickerDialogDataVencimento.show(getSupportFragmentManager(), DATEPICKER_VENCIMENTO_TAG);
            }
        });


        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GastosActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
                GastosActivity.this.finish();
            }
        });


        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtValorGasto.getText().toString().equals(numberFormat.getCurrency().getSymbol() + "0,00")){
                    Toast.makeText(GastosActivity.this, "Insira um valor para o gasto!",Toast.LENGTH_SHORT).show();

                }else  if (positionCategoria == -1){
                    Toast.makeText(GastosActivity.this, "Selecione uma categoria!",Toast.LENGTH_SHORT).show();

                }else  if (btnDataGasto.getText().toString().equals("Vencimento")){
                    Toast.makeText(GastosActivity.this, "Selecione uma data de vencimento!",Toast.LENGTH_SHORT).show();

                }else  if (edtCometariosGasto.getText().toString().equals("")){
                    Toast.makeText(GastosActivity.this, "Insira uma descrição para o gasto!",Toast.LENGTH_SHORT).show();

                }else  if (!checkDataPagamentoGasto.isChecked() && !btnFormaPagamento.getText().toString().equals("Forma de pagamento")){
                    Toast.makeText(GastosActivity.this, "Insira uma data de pagamento para o gasto!",Toast.LENGTH_SHORT).show();

                }else  if (checkDataPagamentoGasto.isChecked() && btnFormaPagamento.getText().toString().equals("Forma de pagamento")){
                    Toast.makeText(GastosActivity.this, "Insira uma forma de pagamento para o gasto!",Toast.LENGTH_SHORT).show();

                }else{

                    Gasto gasto = new Gasto();

                    String valor = txtValorGasto.getText().toString().replace("R","");
                    valor = valor.replace("$","");


                    try {
                        Number number = null;
                        number = numberFormat.parse(valor);
                        gasto.setValor(BigDecimal.valueOf(number.doubleValue()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    gasto.setDataVencimento(formataData(btnDataGasto.getText().toString()));

                    if (checkDataPagamentoGasto.isChecked()){
                        gasto.setDataPagamento(formataData(checkDataPagamentoGasto.getText().toString()));
                        gasto.setPago(checkDataPagamentoGasto.isChecked());
                    }else {
                        gasto.setDataPagamento(new Date());
                        gasto.setPago(checkDataPagamentoGasto.isChecked());
                    }

                    gasto.setDataCadastro(getDataHoje());

                    gasto.setComentarios(edtCometariosGasto.getText().toString());

                    gasto.setCategoria((int) categorias.get(positionCategoria).getId());
                    gasto.setFormaPagamento((int) formaPagamento.getId());

                    GastosDAO gastosDAO = new GastosDAO(GastosActivity.this);

                    gastosDAO.inserir(gasto);

                    Log.i("Script","GastosActivity Valor: " + valor);
                    Log.i("Script","GastosActivity Valorde cadastro: " + String.valueOf(gasto.getValor()));
                    Log.i("Script","GastosActivity Data Vencimento: " + getDateToString(gasto.getDataVencimento()));
                    Log.i("Script","GastosActivity Data pagamneto: " + getDateToString(gasto.getDataPagamento()));
                    Log.i("Script","GastosActivity Data cadastro: " + getDateToString(gasto.getDataCadastro()));
                    Log.i("Script","GastosActivity Descrição: " + gasto.getComentarios());
                    Log.i("Script","GastosActivity Pago: " + String.valueOf(gasto.isPago()));
                    Log.i("Script","GastosActivity Categoria: " + gasto.getCategoria()+", "+categorias.get(positionCategoria).getTitle()+", "+categorias.get(positionCategoria).getId());
                    Log.i("Script","GastosActivity Forma de Pagamento: " + gasto.getFormaPagamento());

                    Intent intent = new Intent(GastosActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
                    GastosActivity.this.finish();
                }
            }
        });


        /******************************************************************************************/
        /*               verifica se há alguma instancia salvapra recuperar os valores           */
        /****************************************************************************************/
        if (savedInstanceState != null){
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_PAGAMENTO_TAG);
            DatePickerDialog dpd2 = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_VENCIMENTO_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(new GetDataSetDataPagamento());
            }
            if (dpd2 != null) {
                dpd2.setOnDateSetListener(new GetDataSetDataVencimento());
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
            checkDataPagamentoGasto.setText( dia+ "-" + mes + "-" +year);
        }
    }


    private class GetDataSetDataVencimento extends FragmentActivity implements  DatePickerDialog.OnDateSetListener{

        private GetDataSetDataVencimento() {
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

            btnDataGasto.setText( dia+ "-" + mes + "-" +year);
        }
    }

    private Date formataData(String data) {
        if (data == null || data.equals(""))
            return null;

        Date date = new Date();
        try {

            data = data + " 00:00:00";
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");//SimpleDateFormat("dd/MM/yyyy");
            date = (Date)formatter.parse(data);

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
