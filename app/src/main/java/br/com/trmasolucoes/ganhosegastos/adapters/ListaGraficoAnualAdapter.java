package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

/**
 * Created by tairo on 21/03/15.
 */
public class ListaGraficoAnualAdapter extends BaseAdapter {

    private List<Date> list;
    private Context context;

    private List<Gasto> gastos;
    private List<CategoriaGasto> categoriaGastos;
    private CategoriaGastosDAO categoriaGastosDAO;
    private GastosDAO gastosDAO;

    public ListaGraficoAnualAdapter(Context context, List<Date> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

         // Re-use the view if possible
        // --
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lista_grafico_anual_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        gastosDAO = new GastosDAO(context);
        categoriaGastosDAO = new CategoriaGastosDAO(context);

        categoriaGastos = categoriaGastosDAO.buscar();

        //  seta a posicão x do grafico
        ArrayList<BarEntry> values = new ArrayList<>();

        //  seta a posicão y do grafico
        ArrayList<String> labels = new ArrayList<String>();

        ArrayList<Integer> cores = new ArrayList<Integer>();


        for (int i = 0; i < categoriaGastos.size(); i++) {
            values.add(new BarEntry(gastosDAO.getTotalGastosMesCategoria(categoriaGastos.get(i).getId(),list.get(position)).floatValue(), i));
            labels.add(categoriaGastos.get(i).getTitle());
            cores.add(Color.parseColor(categoriaGastos.get(i).getCor()));
        }


        // seta dataset para colocar os valores no grafico
        BarDataSet dataset = new BarDataSet(values, "Categorias de ganhos");

        BarData data = new BarData(labels, dataset);
        holder.barChart.setData(data);

        // seta a decricao  do grafico
        holder.barChart.setDescription("");

        // seta acor do grafico
        dataset.setColors(cores);

        //legendas
        Legend barLegends = holder.barChart.getLegend();
        barLegends.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        barLegends.setXEntrySpace(7f);
        barLegends.setYEntrySpace(5f);

        // Seta a animação
        holder.barChart.animateY(1500);

        holder.txtMesGraficoAnual.setText(DateUtil.getMes(list.get(position)));
        holder.txtValorMesGraficoAnual.setText(DateUtil.formataValorToString(gastosDAO.getTotalGastosMes(list.get(position))));



        return convertView;
    }

    /** ViewHolder para ter uma melhor performace na lista de categoria*/
    private static class ViewHolder {

        public ViewHolder(View root) {
            barChart = (BarChart) root.findViewById(R.id.barChartMes);
            txtMesGraficoAnual = (TextView) root.findViewById(R.id.txtMesGraficoAnual);
            txtValorMesGraficoAnual = (TextView) root.findViewById(R.id.txtValorMesGraficoAnual);
        }

        public BarChart barChart;
        public TextView txtMesGraficoAnual;
        public TextView txtValorMesGraficoAnual;
    }

}
