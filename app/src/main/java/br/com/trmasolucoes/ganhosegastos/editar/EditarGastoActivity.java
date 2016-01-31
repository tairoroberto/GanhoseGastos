package br.com.trmasolucoes.ganhosegastos.editar;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.HomeActivity;
import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.adapters.ItemListCategoriaGastosAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.FormasDePagamentoDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DialogCalculadora;
import br.com.trmasolucoes.ganhosegastos.util.DialogFormasDePagamento;


public class EditarGastoActivity extends ActionBarActivity {

    private Gasto gasto;
    private TextView txtValorGastoEditar;
    private ListView listaCategoriasEditar;
    private LinearLayout btnDeletarEditar;
    private LinearLayout btnSalvar;
    private Button btnDtaVencimentoEditar;
    private CheckBox checkDataPagamentoGastoEditar;
    private Button btnFormaPagamentoEditar;
    private EditText edtComentarioGastoEditar;
    private FormaPagamento formaPagamento = new FormaPagamento();
    private int positionCategoria  = -1;

    public static final String DATEPICKER_PAGAMENTO_TAG = "datepicker_pagamento";
    public static final String DATEPICKER_VENCIMENTO_TAG = "datepicker_vencimento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_gasto);


        txtValorGastoEditar = (TextView)findViewById(R.id.txtValorGastoEditar);
        listaCategoriasEditar = (ListView) findViewById(R.id.listaCategoriasEditar);
        btnDeletarEditar = (LinearLayout) findViewById(R.id.btnDeletarGastosEditar);
        btnSalvar = (LinearLayout) findViewById(R.id.btnSalvarGastosEditar);
        btnDtaVencimentoEditar =(Button)findViewById(R.id.btnDataVencimentoGastoEditar);
        checkDataPagamentoGastoEditar =(CheckBox)findViewById(R.id.checkDataPagamentoGastoEditar);
        btnFormaPagamentoEditar =(Button)findViewById(R.id.btnFormaPagamentoEditar);
        edtComentarioGastoEditar = (EditText)findViewById(R.id.edtComentarioGastoEditar);

        /*Chama o Data Acess Object que faz a transação no banco de dados*/
        FormasDePagamentoDAO formasDePagamentoDAO = new FormasDePagamentoDAO(EditarGastoActivity.this);
        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(EditarGastoActivity.this);


        Intent intent = getIntent();

        if (intent != null){
            Bundle bundle = intent.getExtras();
            if (bundle != null){
                gasto = new Gasto();
                gasto = bundle.getParcelable("gasto");

                /*Valor*/

                txtValorGastoEditar.setText(DateUtil.formataValorToString(gasto.getValor()));

                /*data de vencimento*/
                btnDtaVencimentoEditar.setText(getDateToString(gasto.getDataVencimento()));

                /*data de pagamento*/
                if (gasto.isPago()){
                    checkDataPagamentoGastoEditar.setText(getDateToString(gasto.getDataPagamento()));
                    checkDataPagamentoGastoEditar.setChecked(gasto.isPago());

                    /*forma de pagamento*/
                    btnFormaPagamentoEditar.setText(formasDePagamentoDAO.getFormaPagamento(gasto.getFormaPagamento()).getTitle());
                }

                /*descrição do gasto*/
                edtComentarioGastoEditar.setText(gasto.getComentarios());
            }
        }


        txtValorGastoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o frgment da calculadora
                DialogCalculadora dialogCalculadora = new DialogCalculadora(EditarGastoActivity.this,txtValorGastoEditar);
                dialogCalculadora.chamaNumeros();
            }
        });


        CategoriaGasto catGasto = categoriaGastosDAO.getCategoriaGasto(gasto.getCategoria());

        //list que irá carregar as categorias
        final List<CategoriaGasto> categorias = categoriaGastosDAO.buscar();

        for (int i = 0; i < categorias.size(); i++){
            if (categorias.get(i).getId() == catGasto.getId()){
                positionCategoria = i;
            }
        }


        //adpter que irá peecher a lista de categorias
        final ItemListCategoriaGastosAdapter adapter = new ItemListCategoriaGastosAdapter(this, categorias);
        listaCategoriasEditar.setAdapter(adapter);

        listaCategoriasEditar.setItemChecked(positionCategoria,true);
        adapter.notifyDataSetChanged();

        //imementaoa de click na lista
        listaCategoriasEditar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                positionCategoria = position;
                Toast.makeText(EditarGastoActivity.this, categorias.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


        btnFormaPagamentoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFormasDePagamento dialogFormasDePagamento = new DialogFormasDePagamento(EditarGastoActivity.this,btnFormaPagamentoEditar,formaPagamento);
                dialogFormasDePagamento.chamaFormaDePagamento();
            }
        });


        checkDataPagamentoGastoEditar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Calendar calendar = Calendar.getInstance();

                    DatePickerDialog datePickerDialogDataPagamento = DatePickerDialog.newInstance(new GetDataSetDataPagamento(),
                            calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH), true);

                    datePickerDialogDataPagamento.setVibrate(true);
                    datePickerDialogDataPagamento.setYearRange(1985, 2028);
                    datePickerDialogDataPagamento.setCloseOnSingleTapDay(true);
                    datePickerDialogDataPagamento.show(getSupportFragmentManager(), DATEPICKER_PAGAMENTO_TAG);
                } else {
                    checkDataPagamentoGastoEditar.setText("Data pagamento");
                }
            }
        });


        // setup listener for a date change:
        btnDtaVencimentoEditar.setOnClickListener(new View.OnClickListener() {
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




        /*método para setar o id da forma de pagamento caso esta não seja mudada no clique*/
        final List<FormaPagamento> formasPagamentos = formasDePagamentoDAO.buscar();
        for (int i = 0; i < formasPagamentos.size(); i++){
            if (formasPagamentos.get(i).getId() == gasto.getFormaPagamento()){
                formaPagamento.setId(formasPagamentos.get(i).getId() );
            }
        }



        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtValorGastoEditar.getText().toString().equals(DateUtil.getSymbol()+"0,00")){
                    Toast.makeText(EditarGastoActivity.this, "Insira um valor para o gasto!",Toast.LENGTH_SHORT).show();

                }else if (positionCategoria == -1){
                    Toast.makeText(EditarGastoActivity.this, "Selecione uma categoria!",Toast.LENGTH_SHORT).show();

                }else if (btnDtaVencimentoEditar.getText().toString().equals("Vencimento")){
                    Toast.makeText(EditarGastoActivity.this, "Selecione uma data de vencimento!",Toast.LENGTH_SHORT).show();

                }else if (edtComentarioGastoEditar.getText().toString().equals("")){
                    Toast.makeText(EditarGastoActivity.this, "Insira uma descrição para o gasto!",Toast.LENGTH_SHORT).show();

                }else if (!checkDataPagamentoGastoEditar.isChecked() && !btnFormaPagamentoEditar.getText().toString().equals("Forma de pagamento")){
                    Toast.makeText(EditarGastoActivity.this, "Insira uma data de pagamento para o gasto!",Toast.LENGTH_SHORT).show();

                }else if (checkDataPagamentoGastoEditar.isChecked() &&
                           btnFormaPagamentoEditar.getText().toString().equals("Forma de pagamento") ||
                            formaPagamento.getId() == 0){
                    Toast.makeText(EditarGastoActivity.this, "Insira uma forma de pagamento para o gasto!",Toast.LENGTH_SHORT).show();

                }else{



                    String valor = txtValorGastoEditar.getText().toString().replace("R","");
                    valor = valor.replace("$","");


                    gasto.setValor(new BigDecimal(DateUtil.formataStringToValor(valor)));

                    gasto.setDataVencimento(formataData(btnDtaVencimentoEditar.getText().toString()));

                    if (checkDataPagamentoGastoEditar.isChecked()){
                        gasto.setDataPagamento(formataData(checkDataPagamentoGastoEditar.getText().toString()));
                        gasto.setPago(checkDataPagamentoGastoEditar.isChecked());
                    }else {
                        gasto.setDataPagamento(new Date());
                        gasto.setPago(checkDataPagamentoGastoEditar.isChecked());
                    }

                    gasto.setDataCadastro(getDataHoje());

                    gasto.setComentarios(edtComentarioGastoEditar.getText().toString());

                    gasto.setCategoria((int) categorias.get(positionCategoria).getId());
                    gasto.setFormaPagamento((int) formaPagamento.getId());

                    GastosDAO gastosDAO = new GastosDAO(EditarGastoActivity.this);

                    gastosDAO.atualizar(gasto);

                    Log.i("Script","ID: " + gasto.getId());
                    Log.i("Script","Valor: " + valor);
                    Log.i("Script","Valorde cadastro: " + String.valueOf(gasto.getValor()));
                    Log.i("Script","Data Vencimento: " + getDateToString(gasto.getDataVencimento()));
                    Log.i("Script","Data pagamneto: " + getDateToString(gasto.getDataPagamento()));
                    Log.i("Script","Data cadastro: " + getDateToString(gasto.getDataCadastro()));
                    Log.i("Script","Descrição: " + gasto.getComentarios());
                    Log.i("Script","Pago: " + String.valueOf(gasto.isPago()));
                    Log.i("Script","Categoria: " + gasto.getCategoria()+", "+categorias.get(positionCategoria).getTitle()+", "+categorias.get(positionCategoria).getId());
                    Log.i("Script","Forma de Pagamento: " + gasto.getFormaPagamento());


                    Toast.makeText(EditarGastoActivity.this, "GASTO EDITADO COM SUCESSO!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditarGastoActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
                    EditarGastoActivity.this.finish();
                }
            }
        });


        btnDeletarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GastosDAO gastosDAO = new GastosDAO(EditarGastoActivity.this);
                gastosDAO.deletar(gasto);

                Toast.makeText(EditarGastoActivity.this, "GASTO DELETADO COM SUCESSO!",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditarGastoActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
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
            checkDataPagamentoGastoEditar.setText( dia + "-" + mes + "-" + year);
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

            btnDtaVencimentoEditar.setText( dia + "-" + mes + "-" + year);
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
