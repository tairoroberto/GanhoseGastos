package br.com.trmasolucoes.ganhosegastos.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.trmasolucoes.ganhosegastos.R;
import br.com.trmasolucoes.ganhosegastos.model.ColorItem;

/**
 * Created by tairo on 21/03/15.
 */
public class ColorSpinnerAdapter extends BaseAdapter {
    private ArrayList<ColorItem> colors;
    private  Context context;


    public ColorSpinnerAdapter(Context context,ArrayList<ColorItem> colors) {
        this.context = context;
        this.colors = colors;
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int position) {
        return colors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ColorItem colorItem = colors.get(position);

        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_cor_spinner_categoria, null);
        }
        ImageView image = (ImageView) view.findViewById(R.id.imageCor);
        image.setBackgroundColor(Color.parseColor(colorItem.getName()));
        return view;
    }
}
