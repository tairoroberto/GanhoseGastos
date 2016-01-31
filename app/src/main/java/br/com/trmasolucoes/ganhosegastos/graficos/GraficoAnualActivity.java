package br.com.trmasolucoes.ganhosegastos.graficos;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.adapters.ListaGraficoAnualAdapter;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class GraficoAnualActivity extends ActionBarActivity {

    private ListView listViewGraficoAnual;
    private List<Date> datas = new ArrayList<Date>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_anual);

        listViewGraficoAnual = (ListView) findViewById(R.id.listViewGraphAnual);

        for (int i = 0; i < 12; i++){

            if (i == 0){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-01-01"+" 00:00:00"));

            }else if (i == 1){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-02-01"+" 00:00:00"));

            }else if (i == 2){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-03-01"+" 00:00:00"));

            }else if (i == 3){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-04-01"+" 00:00:00"));

            }else if (i == 4){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-05-01"+" 00:00:00"));

            }else if (i == 5){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-06-01"+" 00:00:00"));

            }else if (i == 6){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-07-01"+" 00:00:00"));

            }else if (i == 7){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-08-01"+" 00:00:00"));

            }else if (i == 8){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-09-01"+" 00:00:00"));

            }else if (i == 9){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-10-01"+" 00:00:00"));

            }else if (i == 10){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-11-01"+" 00:00:00"));

            }else if (i == 11){
                datas.add(DateUtil.getStringToDate(DateUtil.getAno()+"-12-01"+" 00:00:00"));

            }
        }

        for (int i = 0; i < datas.size(); i++) {
            Log.i("Script", "Data: "+ DateUtil.getDateToString(datas.get(i)) );
        }



        ListaGraficoAnualAdapter adapter = new ListaGraficoAnualAdapter(GraficoAnualActivity.this,datas);

        listViewGraficoAnual.setAdapter(adapter);
    }
}
