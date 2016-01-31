package br.com.trmasolucoes.ganhosegastos.editar;

import android.app.Dialog;
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
import br.com.trmasolucoes.ganhosegastos.adapters.ItemListCategoriaGanhosAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.FormasDePagamentoDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DialogCalculadora;


public class EditarGanhoActivity extends ActionBarActivity {


    //Dialog  e Text view para o fragment da  calculadora
    private Ganho ganho;
    private Dialog dialog;
    private EditText edtValorEditar;
    private ListView listaOpcoesEditar ;
    private TextView txtValorGanhoEditar ;
    private ListView listaCategoriasEditar;
    private LinearLayout btnSalvarEditar;
    private LinearLayout btnDeletarEditar;
    private Button btnDataGanhoEditar;
    private EditText edtComentariosGanhoEditar;
    public static final String DATEPICKER_TAG = "datepicker";
    private int positionCategoria = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_ganho);


        listaOpcoesEditar = (ListView) findViewById(R.id.drawer_content);
        txtValorGanhoEditar = (TextView)findViewById(R.id.txtValorGanhoEditar);
        //lista com as ctegorias
        listaCategoriasEditar = (ListView)findViewById(R.id.listaCategoriasEditar);
        btnSalvarEditar = (LinearLayout) findViewById(R.id.btnSalvarGanhosEditar);
        btnDeletarEditar = (LinearLayout) findViewById(R.id.btnDeletarGanhosEditar);
        btnDataGanhoEditar = (Button) findViewById(R.id.btnDataGanhoEditar);
        edtComentariosGanhoEditar = (EditText) findViewById(R.id.edtComentarioGanhoEditar);



        /*Chama o Data Acess Object que faz a transação no banco de dados*/
        FormasDePagamentoDAO formasDePagamentoDAO = new FormasDePagamentoDAO(EditarGanhoActivity.this);
        CategoriaGanhosDAO categoriaGanhosDAO = new CategoriaGanhosDAO(EditarGanhoActivity.this);


        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                ganho = new Ganho();
                ganho = bundle.getParcelable("ganho");

                 /*Valor*/
                txtValorGanhoEditar.setText(DateUtil.formataValorToString(ganho.getValor()));

                /*data de vencimento*/
                btnDataGanhoEditar.setText(getDateToString(ganho.getDataPagamento()));

                /*descrição do gasto*/
                edtComentariosGanhoEditar.setText(ganho.getComentarios());

            }
        }

             /*Eventos ações*/

        txtValorGanhoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //chama o frgment da calculadora
                DialogCalculadora dialogCalculadora = new DialogCalculadora(EditarGanhoActivity.this,txtValorGanhoEditar);
                dialogCalculadora.chamaNumeros();
            }
        });


                /*Categoria para comparar busca na base de dados*/
        CategoriaGanho catGanho = categoriaGanhosDAO.getCategoriaGanho(ganho.getCategoria());

        //list que irá carregar as categorias
        final List<CategoriaGanho> categorias = categoriaGanhosDAO.buscar();

        for (int i = 0; i < categorias.size(); i++){
            if (categorias.get(i).getId() == catGanho.getId()){
                positionCategoria = i;
            }
        }

        //adpter que irá peecher a lista de categorias
        final ItemListCategoriaGanhosAdapter adapter = new ItemListCategoriaGanhosAdapter(this, categorias);
        listaCategoriasEditar.setAdapter(adapter);

        listaCategoriasEditar.setItemChecked(positionCategoria,true);
        adapter.notifyDataSetChanged();

        //implementação de click na lista
        listaCategoriasEditar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.notifyDataSetChanged();
                positionCategoria = position;
                Toast.makeText(EditarGanhoActivity.this, categorias.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });


        // setup listener for a date change:
        btnDataGanhoEditar.setOnClickListener(new View.OnClickListener() {
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



        btnSalvarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtValorGanhoEditar.getText().toString().equals(DateUtil.getSymbol() + "0,00")){
                    Toast.makeText(EditarGanhoActivity.this, "Insira um valor para o ganho!",Toast.LENGTH_SHORT).show();

                }else  if (positionCategoria == -1){
                    Toast.makeText(EditarGanhoActivity.this, "Selecione uma categoria!",Toast.LENGTH_SHORT).show();

                }else  if (btnDataGanhoEditar.getText().toString().equals("Selecione a data")){
                    Toast.makeText(EditarGanhoActivity.this, "Selecione a data de pagamento do ganho!",Toast.LENGTH_SHORT).show();

                }else  if (edtComentariosGanhoEditar.getText().toString().equals("")){
                    Toast.makeText(EditarGanhoActivity.this, "Insira uma descrição para o ganho!",Toast.LENGTH_SHORT).show();

                }else{


                    String valor = txtValorGanhoEditar.getText().toString().replace("R","");
                    valor = valor.replace("$","");


                    ganho.setValor(BigDecimal.valueOf(DateUtil.formataStringToValor(valor)));


                    ganho.setDataPagamento(formataData(btnDataGanhoEditar.getText().toString()));

                    ganho.setComentarios(edtComentariosGanhoEditar.getText().toString());

                    ganho.setCategoria((int) categorias.get(positionCategoria).getId());

                    GanhosDAO ganhosDAO = new GanhosDAO(EditarGanhoActivity.this);

                    ganhosDAO.atualizar(ganho);

                    Log.i("Script","ID: " + ganho.getId());
                    Log.i("Script", "Valor: " + valor);
                    Log.i("Script","Valorde cadastro: " + String.valueOf(ganho.getValor()));
                    Log.i("Script","Data pagamneto: " + getDateToString(ganho.getDataPagamento()));
                    Log.i("Script","Data cadastro: " + getDateToString(ganho.getDataCadastro()));
                    Log.i("Script","Descrição: " + ganho.getComentarios());
                    Log.i("Script","Categoria: " + ganho.getCategoria()+", "+categorias.get(positionCategoria).getTitle()+", "+categorias.get(positionCategoria).getId());


                    Toast.makeText(EditarGanhoActivity.this, "GANHO EDITADO COM SUCESSO!",Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(EditarGanhoActivity.this,HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
                    EditarGanhoActivity.this.finish();
                }
            }
        });



        btnDeletarEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GanhosDAO ganhosDAO = new GanhosDAO(EditarGanhoActivity.this);
                ganhosDAO.deletar(ganho);

                Toast.makeText(EditarGanhoActivity.this, "GASTO DELETADO COM SUCESSO!",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditarGanhoActivity.this,HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
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

            btnDataGanhoEditar.setText( dia + "-" + mes  + "-" + year);
        }
    }


    private Date formataData(String data) {
        if (data == null || data.equals(""))
            return null;

        Date date = new Date();
        try {
            data += " 00:00:00";
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
