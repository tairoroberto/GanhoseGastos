package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.AwesomeTextView;
import com.beardedhen.androidbootstrap.font.FontAwesome;

import br.com.trmasolucoes.ganhosegastos.R;

/**
 * Created by tairo on 21/03/15.
 */
public class ListaOpcoesAdapter extends BaseAdapter {

    private String[] list;
    private Context context;

    public ListaOpcoesAdapter(Context context,String[] list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object getItem(int position) {
        return list[position];
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
            convertView = inflater.inflate(R.layout.lista_opcao_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(R.id.holder, holder);
        } else {
            holder = (ViewHolder) convertView.getTag(R.id.holder);
        }

        holder.title.setText(list[position]);

        if (position == 0){
            holder.icone.setFontAwesomeIcon(FontAwesome.FA_HOME);

        }else if (position == 1){
            holder.icone.setFontAwesomeIcon(FontAwesome.FA_BAR_CHART_O);

        }else if (position == 2){
            holder.icone.setFontAwesomeIcon(FontAwesome.FA_MONEY);

        }else if (position == 3){
            holder.icone.setFontAwesomeIcon(FontAwesome.FA_FROWN_O);

        }else if (position == 4){
            holder.icone.setFontAwesomeIcon(FontAwesome.FA_EURO);
        }else if (position == 5){
            holder.icone.setFontAwesomeIcon(FontAwesome.FA_SAVE);
        }

        return convertView;
    }

    /** ViewHolder para ter uma melhor performace na lista de categoria*/
    private static class ViewHolder {

        public ViewHolder(View root) {
            title = (TextView) root.findViewById(R.id.txtTitleOpcao);
            //icone = (ImageView) root.findViewById(R.id.imageIcone);
            icone = (AwesomeTextView) root.findViewById(R.id.imageIcone);
        }


        //public ImageView icone;
        public AwesomeTextView icone;
        public TextView title;
    }
}
