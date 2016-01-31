package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;

/**
 * Created by tairo on 21/03/15.
 */
public class ListaFormaPagamentoAdapter extends BaseAdapter {

    private List<FormaPagamento> list;
    private Context context;

    public ListaFormaPagamentoAdapter(Context context, List<FormaPagamento> list) {
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
            convertView = inflater.inflate(R.layout.categorias_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        holder.title.setText(list.get(position).getTitle());
        holder.cor.setBackgroundColor(Color.parseColor(list.get(position).getCor()));

        return convertView;
    }

    /** ViewHolder para ter uma melhor performace na lista de categoria*/
    private static class ViewHolder {

        public ViewHolder(View root) {
            cor = (ImageView) root.findViewById(R.id.imgCorCategoria);
            title = (TextView) root.findViewById(R.id.txtTitleCategoria);
        }

        public ImageView cor;
        public TextView title;
    }
}
