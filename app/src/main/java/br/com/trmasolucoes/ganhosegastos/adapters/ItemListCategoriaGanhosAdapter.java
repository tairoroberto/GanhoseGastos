/**
 * 
 */
package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.franlopez.flipcheckbox.FlipCheckBox;

import java.util.List;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.widget.CheckableRelativeLayout;
import br.com.trmasolucoes.ganhosegastos.widget.InertCheckBox;

/**
 * Adapter that allows us to render a list of items
 * 
 * @author Tairo
 */
public class ItemListCategoriaGanhosAdapter extends ArrayAdapter<CategoriaGanho> {

    private LayoutInflater li;

	/**
	 * Constructor from a list of items
	 */
	public ItemListCategoriaGanhosAdapter(Context context, List<CategoriaGanho> items) {
		super(context, 0, items);
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// The item we want to get the view for
		// --
		final CategoriaGanho item = getItem(position);

		// Re-use the view if possible
		// --
		ViewHolder holder;
		if (convertView == null) {
			convertView = li.inflate(R.layout.lista_categoria_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(R.id.holder, holder);
		} else {
			holder = (ViewHolder) convertView.getTag(R.id.holder);
		}

		// Set some view properties
		//holder.id.setText("#" + item.getId());
		holder.title.setText(item.getTitle());

		// Restore the checked state properly
		final ListView lv = (ListView) parent;

        //pega o click do layput
		holder.layout.setChecked(lv.isItemChecked(position));

        holder.flipCheckBox.setEnabled(false);
        //holder.flipCheckBox.setBackgroundColor(Color.BLUE);
        holder.flipCheckBox.setFrontColor(Color.parseColor(item.getCor()));

        //seta o flipCheckBox conforme o click do laypout
        holder.flipCheckBox.setChecked(holder.layout.isChecked());

        //seta o Radio button conforme o click do laypout
        holder.itemCheckBox.setChecked(holder.layout.isChecked());

		return convertView;
	}

	@Override
	public long getItemId(int position) {
		return position;//getItem(position).getId();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}


    /** ViewHolder para ter uma melhor performace na lista de categoria*/
	private static class ViewHolder {

		public ViewHolder(View root) {
			title = (TextView) root.findViewById(R.id.title);
            flipCheckBox = (FlipCheckBox) root.findViewById(R.id.flipCard);
            itemCheckBox = (InertCheckBox) root.findViewById(R.id.itemCheckBox);
			layout = (CheckableRelativeLayout) root.findViewById(R.id.layout);
		}

		public TextView title;
        public FlipCheckBox flipCheckBox;
        public InertCheckBox itemCheckBox;
		public CheckableRelativeLayout layout;
	}

}
