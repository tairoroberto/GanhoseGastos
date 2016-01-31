package br.com.trmasolucoes.ganhosegastos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.trmasolucoes.ganhosegastos.adapters.ColorSpinnerAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.ColorItem;


public class CategoriaGastosActivity extends ActionBarActivity {

    private Spinner spinnerCorCategoriaGastos;
    private EditText edtNomeCategoriaGastos;
    private int positionColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_gastos);


        spinnerCorCategoriaGastos = (Spinner) findViewById(R.id.spinnerCorCategoriaGastos);
        edtNomeCategoriaGastos =(EditText) findViewById(R.id.edtNomeCategoriaGastos);


        final ArrayList<ColorItem> colors = new ArrayList<ColorItem>();
        int indice = 0;
        colors.add(new ColorItem(indice,"#C71585"));
        colors.add(new ColorItem(indice + 1,"#FF1493"));
        colors.add(new ColorItem(indice + 1,"#FF69B4"));
        colors.add(new ColorItem(indice + 1,"#C71585"));
        colors.add(new ColorItem(indice + 1,"#FFC0CB"));
        colors.add(new ColorItem(indice + 1,"#F08080"));
        colors.add(new ColorItem(indice + 1,"#CD5C5C"));
        colors.add(new ColorItem(indice + 1,"#DC143C"));

        colors.add(new ColorItem(indice + 1,"#6A5ACD"));
        colors.add(new ColorItem(indice + 1,"#191970"));
        colors.add(new ColorItem(indice + 1,"#0000FF"));
        colors.add(new ColorItem(indice + 1,"#6495ED"));
        colors.add(new ColorItem(indice + 1,"#4169E1"));

        colors.add(new ColorItem(indice + 1,"#000000"));
        colors.add(new ColorItem(indice + 1,"#696969"));
        colors.add(new ColorItem(indice + 1,"#808080"));
        colors.add(new ColorItem(indice + 1,"#DCDCDC"));
        colors.add(new ColorItem(indice + 1,"#FFFFFF"));

        colors.add(new ColorItem(indice + 1,"#00BFFF"));
        colors.add(new ColorItem(indice + 1,"#87CEFA"));
        colors.add(new ColorItem(indice + 1,"#ADD8E6"));
        colors.add(new ColorItem(indice + 1,"#B0C4DE"));
        colors.add(new ColorItem(indice + 1,"#708090"));
        colors.add(new ColorItem(indice + 1,"#778899"));

        colors.add(new ColorItem(indice + 1,"#00FFFF"));
        colors.add(new ColorItem(indice + 1,"#00CED1"));
        colors.add(new ColorItem(indice + 1,"#40E0D0"));
        colors.add(new ColorItem(indice + 1,"#48D1CC"));
        colors.add(new ColorItem(indice + 1,"#20B2AA"));
        colors.add(new ColorItem(indice + 1,"#008B8B"));
        colors.add(new ColorItem(indice + 1,"#008B8B"));
        colors.add(new ColorItem(indice + 1,"#7FFFD4"));
        colors.add(new ColorItem(indice + 1,"#66CDAA"));
        colors.add(new ColorItem(indice + 1,"#5F9EA0"));

        colors.add(new ColorItem(indice + 1,"#2F4F4F"));
        colors.add(new ColorItem(indice + 1,"#00FA9A"));
        colors.add(new ColorItem(indice + 1,"#00FF7F"));
        colors.add(new ColorItem(indice + 1,"#98FB98"));
        colors.add(new ColorItem(indice + 1,"#90EE90"));
        colors.add(new ColorItem(indice + 1,"#8FBC8F"));
        colors.add(new ColorItem(indice + 1,"#3CB371"));
        colors.add(new ColorItem(indice + 1,"#2E8B57"));
        colors.add(new ColorItem(indice + 1,"#006400"));
        colors.add(new ColorItem(indice + 1,"#008000"));
        colors.add(new ColorItem(indice + 1,"#228B22"));
        colors.add(new ColorItem(indice + 1,"#32CD32"));
        colors.add(new ColorItem(indice + 1,"#00FF00"));
        colors.add(new ColorItem(indice + 1,"#7CFC00"));
        colors.add(new ColorItem(indice + 1,"#7FFF00"));
        colors.add(new ColorItem(indice + 1,"#ADFF2F"));
        colors.add(new ColorItem(indice + 1,"#9ACD32"));
        colors.add(new ColorItem(indice + 1,"#6B8E23"));
        colors.add(new ColorItem(indice + 1,"#556B2F"));
        colors.add(new ColorItem(indice + 1,"#808000"));

        colors.add(new ColorItem(indice + 1,"#800000"));
        colors.add(new ColorItem(indice + 1,"#8B0000"));
        colors.add(new ColorItem(indice + 1,"#B22222"));
        colors.add(new ColorItem(indice + 1,"#A52A2A"));
        colors.add(new ColorItem(indice + 1,"#FA8072"));
        colors.add(new ColorItem(indice + 1,"#E9967A"));
        colors.add(new ColorItem(indice + 1,"#FFA07A"));
        colors.add(new ColorItem(indice + 1,"#FF7F50"));
        colors.add(new ColorItem(indice + 1,"#FF6347"));
        colors.add(new ColorItem(indice + 1,"#FF0000"));

        colors.add(new ColorItem(indice + 1,"#FF4500"));
        colors.add(new ColorItem(indice + 1,"#FF8C00"));
        colors.add(new ColorItem(indice + 1,"#FFA500"));
        colors.add(new ColorItem(indice + 1,"#FFD700"));
        colors.add(new ColorItem(indice + 1,"#FFFF00"));
        colors.add(new ColorItem(indice + 1,"#F0E68C"));

        colors.add(new ColorItem(indice + 1,"#FF00FF"));
        colors.add(new ColorItem(indice + 1,"#BA55D3"));
        colors.add(new ColorItem(indice + 1,"#EE82EE"));
        colors.add(new ColorItem(indice + 1,"#DA70D6"));
        colors.add(new ColorItem(indice + 1,"#7B68EE"));
        colors.add(new ColorItem(indice + 1,"#8A2BE2"));
        colors.add(new ColorItem(indice + 1,"#4B0082"));
        colors.add(new ColorItem(indice + 1,"#9400D3"));


        spinnerCorCategoriaGastos.setAdapter(new ColorSpinnerAdapter(CategoriaGastosActivity.this,colors));

        spinnerCorCategoriaGastos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                positionColor = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        LinearLayout btnSalvar = (LinearLayout)findViewById(R.id.btnSalvarCategoriaGastos);
        edtNomeCategoriaGastos = (EditText)findViewById(R.id.edtNomeCategoriaGastos);

        btnSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*verifica se foi inserido o titulo da categoria*/
                if (edtNomeCategoriaGastos.getText().toString().equals("")){
                    Toast.makeText(CategoriaGastosActivity.this, "Escolha um nome para a categoria!", Toast.LENGTH_SHORT).show();
                }else {

                    /*Intancia uma nova Categoria de Ganhos para salvar no banco*/
                    CategoriaGasto categoriaGasto = new CategoriaGasto();
                    categoriaGasto.setTitle(edtNomeCategoriaGastos.getText().toString());
                    categoriaGasto.setCor(colors.get(positionColor).getName());

                    /*Chama o Data Acess Object que faz a transação no banco de dados*/
                    CategoriaGastosDAO categoriaGanhosDAO = new CategoriaGastosDAO(CategoriaGastosActivity.this);

                    categoriaGanhosDAO.inserir(categoriaGasto);

                    Toast.makeText(CategoriaGastosActivity.this,"CATEGORIA CADASTRADA COM SUCESSO!",Toast.LENGTH_SHORT).show();


                   /*Finaliza a activity*/
                    Intent intent = new Intent(CategoriaGastosActivity.this,ListaCategoriaGastoActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });


    }

    public void cancelar(View view){
       CategoriaGastosActivity.this.finish();
        overridePendingTransition(R.anim.push_right_enter, R.anim.push_left_exit);
    }
}
