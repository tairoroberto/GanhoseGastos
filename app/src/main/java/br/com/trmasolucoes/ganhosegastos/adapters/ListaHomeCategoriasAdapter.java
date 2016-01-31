package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.database.DatabaseUtils;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGastosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GanhosDAO;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.CustomProgress;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;

/**
 * Created by tairo on 21/03/15.
 */
public class ListaHomeCategoriasAdapter extends BaseAdapter {

    private List<CategoriaGasto> list;
    private Context context;

    public ListaHomeCategoriasAdapter(Context context, List<CategoriaGasto> list) {
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
        CategoriaGasto categoriaGasto =  (CategoriaGasto)list.get(position);

         // Re-use the view if possible
        // --
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lista_home_categorias_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(context);
        GastosDAO gastosDAO = new GastosDAO(context);


        holder.title.setText(categoriaGasto.getTitle());
        holder.title.setTextColor(Color.parseColor(categoriaGasto.getCor()));

        holder.valor.setText(DateUtil.formataValorToString(gastosDAO.getTotalGastosPorCategoria(categoriaGasto.getId())));
        holder.valor.setTextColor(Color.parseColor(categoriaGasto.getCor()));

        holder.customProgress.setProgressColor(Color.parseColor(categoriaGasto.getCor()));
        holder.customProgress.setMaximumPercentage(1.0f);

        return convertView;
    }

    /** ViewHolder para ter uma melhor performace na lista de categoria*/
    private static class ViewHolder {

        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.txtTitleHomeCategoria);
            valor = (TextView) root.findViewById(R.id.txtValoreHomeCategoria);
            customProgress = (CustomProgress) root.findViewById(R.id.custonProgressHomeCategoria);
        }

        public TextView title;
        public TextView valor;
        public CustomProgress customProgress;
    }
}
