package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.domain.CategoriaGanhosDAO;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;

/**
 * Created by tairo on 21/03/15.
 */
public class ListaHistoricoGanhosMesAdapter extends BaseAdapter {

    private List<Ganho> list;
    private Context context;

    public ListaHistoricoGanhosMesAdapter(Context context, List<Ganho> list) {
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
            convertView = inflater.inflate(R.layout.lista_historico_ganhos_mes_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        CategoriaGanhosDAO categoriaGanhosDAO = new CategoriaGanhosDAO(context);

        CategoriaGanho categoriaGanho = categoriaGanhosDAO.getCategoriaGanho(list.get(position).getCategoria());



        holder.title.setText(list.get(position).getComentarios());
        holder.data.setText(DateUtil.getDateToStringShort(list.get(position).getDataPagamento()));

        holder.title.setTextColor(Color.parseColor(categoriaGanho.getCor()));
        holder.data.setTextColor(Color.parseColor(categoriaGanho.getCor()));

        return convertView;
    }

    /** ViewHolder para ter uma melhor performace na lista de categoria*/
    private static class ViewHolder {

        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.txtTitleGanhoHistorico);
            data = (TextView) root.findViewById(R.id.txtDataGanhoHistorico);
        }

        public TextView title;
        public TextView data;
    }
}
