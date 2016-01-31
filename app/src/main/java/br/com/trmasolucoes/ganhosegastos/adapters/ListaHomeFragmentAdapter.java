package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;

/**
 * Created by tairo on 21/03/15.
 */
public class ListaHomeFragmentAdapter extends BaseAdapter {

    private List<Gasto> list;
    private Context context;

    public ListaHomeFragmentAdapter(Context context, List<Gasto> list) {
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

        Gasto gasto = (Gasto) list.get(position);
         // Re-use the view if possible
        // --
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lista_home_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }


        holder.title.setText(gasto.getComentarios() + "  " + DateUtil.formataValorToString(gasto.getValor())
                + "     " + getDateToString(gasto.getDataVencimento()));

        return convertView;
    }

    /** ViewHolder para ter uma melhor performace na lista de categoria*/
    private static class ViewHolder {

        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.txtTitle);
        }

        public TextView title;
    }

    private String getDateToString(Date date){
        if (date == null || date.equals(""))
            return null;

        DateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        return formatador.format(date);
    }
}
