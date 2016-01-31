package br.com.trmasolucoes.ganhosegastos.graficos;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.DefaultValueFormatter;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.HistoricoGastosMesActivity;
import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;
import br.com.trmasolucoes.ganhosegastos.util.DisplayUtil;


public class GraficoGastoActivity extends ActionBarActivity {

    private List<Gasto> gastos;
    private List<CategoriaGasto> categoriaGastos;
    private CategoriaGastosDAO categoriaGastosDAO;
    private GastosDAO gastosDAO;
    private PieChart pieChart;
    private BarChart barChart;
    private TextView txtMesGraficoGastos;
    private TextView txtValorGraficoGastos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico_gasto);

        barChart = (BarChart) findViewById(R.id.graphGastos);
        pieChart = (PieChart) findViewById(R.id.graphGastosPie);
        txtMesGraficoGastos = (TextView) findViewById(R.id.txtMesGraficoGastos);
        txtValorGraficoGastos = (TextView) findViewById(R.id.txtValorMesGraficoGasto);

        gastosDAO = new GastosDAO(GraficoGastoActivity.this);
        categoriaGastosDAO = new CategoriaGastosDAO(GraficoGastoActivity.this);

        categoriaGastos = categoriaGastosDAO.buscar();


        //ganhos = ganhosDAO.ge


        if (!DisplayUtil.getScreenOrientation(GraficoGastoActivity.this).equals("landscape")){

            barChart.setVisibility(View.VISIBLE);
            pieChart.setVisibility(View.GONE);


            //  seta a posicão x do grafico
            ArrayList<BarEntry> values = new ArrayList<>();

            //  seta a posicão y do grafico
            ArrayList<String> labels = new ArrayList<String>();

            ArrayList<Integer> cores = new ArrayList<Integer>();


            for (int i = 0; i < categoriaGastos.size(); i++) {
                values.add(new BarEntry(gastosDAO.getTotalGastosMesCategoria(categoriaGastos.get(i).getId()).floatValue(), i));
                labels.add(categoriaGastos.get(i).getTitle());
                cores.add(Color.parseColor(categoriaGastos.get(i).getCor()));
            }


            // seta dataset para colocar os valores no grafico
            BarDataSet dataset = new BarDataSet(values, "Categorias de ganhos");

            BarData data = new BarData(labels, dataset);
            barChart.setData(data);

            // seta a decricao  do grafico
            barChart.setDescription("");

            // seta acor do grafico
            dataset.setColors(cores);

            //legendas
            Legend barLegends = barChart.getLegend();
            barLegends.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
            barLegends.setXEntrySpace(7f);
            barLegends.setYEntrySpace(5f);

            // Seta a animação
            barChart.animateY(3000);

            barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry entry, int i, Highlight highlight) {
                    if (entry.getVal() > 0){
                        Intent intent = new Intent(GraficoGastoActivity.this, HistoricoGastosMesActivity.class);
                        intent.putExtra("categoria",categoriaGastos.get(entry.getXIndex()));
                        startActivity(intent);
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });

        }else{

            pieChart.setVisibility(View.VISIBLE);
            barChart.setVisibility(View.GONE);

            pieChart.setHoleRadius(60f);

            pieChart.setDrawCenterText(true);

            pieChart.setDrawHoleEnabled(true);

            pieChart.setRotationAngle(0);
            // enable rotation of the chart by touch
            pieChart.setRotationEnabled(true);

            //  seta a posicão x do grafico
            ArrayList<Entry> values = new ArrayList<>();

            //  seta a posicão y do grafico
            ArrayList<String> labels = new ArrayList<String>();

            ArrayList<Integer> cores = new ArrayList<Integer>();


            for (int i = 0; i < categoriaGastos.size(); i++) {
                values.add(new BarEntry(gastosDAO.getTotalGastosMesCategoria(categoriaGastos.get(i).getId()).floatValue(), i));
                labels.add(categoriaGastos.get(i).getTitle());
                cores.add(Color.parseColor(categoriaGastos.get(i).getCor()));
            }

            PieDataSet dataSet = new PieDataSet(values, "Categorias de gastos");
            dataSet.setSliceSpace(3f);


            dataSet.setColors(cores);

            PieData data2 = new PieData(labels, dataSet);
            data2.setValueFormatter(new DefaultValueFormatter(2));
            data2.setValueTextSize(11f);
            data2.setValueTextColor(Color.WHITE);
            pieChart.setData(data2);

            // undo all highlights
            pieChart.highlightValues(null);

            // seta a decricao  do grafico
            pieChart.setDescription("Total de gastos");

            pieChart.animateXY(1500, 1500);

            //legendas
            Legend pieLegends = pieChart.getLegend();
            pieLegends.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
            pieLegends.setXEntrySpace(7f);
            pieLegends.setYEntrySpace(5f);

            pieChart.invalidate();


            pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                @Override
                public void onValueSelected(Entry entry, int i, Highlight highlight) {
                    if (entry.getVal() > 0){
                        Intent intent = new Intent(GraficoGastoActivity.this, HistoricoGastosMesActivity.class);
                        intent.putExtra("categoria",categoriaGastos.get(entry.getXIndex()));
                        startActivity(intent);
                    }
                }

                @Override
                public void onNothingSelected() {

                }
            });
        }

        txtMesGraficoGastos.setText(DateUtil.getMes().toUpperCase());
        txtValorGraficoGastos.setText(DateUtil.formataValorToString(gastosDAO.getTotalGastosMes()));
    }
}
