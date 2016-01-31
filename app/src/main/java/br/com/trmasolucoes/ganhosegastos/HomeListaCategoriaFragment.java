package br.com.trmasolucoes.ganhosegastos;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.adapters.ListaHomeFragmentAdapter;
import br.com.trmasolucoes.ganhosegastos.domain.GastosDAO;
import br.com.trmasolucoes.ganhosegastos.editar.EditarGastoFragmentActivity;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.CustomProgress;

/**
 * Created by tairo on 27/03/15.
 */
public class HomeListaCategoriaFragment extends Fragment {
    private NumberFormat numberFormat = new DecimalFormat("#,##0.00", new DecimalFormatSymbols());
    private String symbol = numberFormat.getCurrency().getSymbol();
    private CategoriaGasto categoriaGasto = new CategoriaGasto();

    private View view;
    private ListView listFragment;
    private TextView  categoriaTitle;
    private TextView valorTotal;
    private CustomProgress customProgress;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Atribui o xml ao fragment
        view = inflater.inflate(R.layout.lista_home_categorias_fragment_item, null);

        listFragment = (ListView)view.findViewById(R.id.listaCategoriaGastoFragment);
        categoriaTitle = (TextView)view.findViewById(R.id.txtTitleHomeCategoriaFragment);
        valorTotal = (TextView)view.findViewById(R.id.txtValorHomeCategoriaFragment);
        customProgress = (CustomProgress) view.findViewById(R.id.custonProgressHomeCategoriaFragment);


        categoriaGasto = getArguments().getParcelable("categoriaGasto");

        GastosDAO gastosDAO = new GastosDAO(getActivity().getApplicationContext());
        final List<Gasto> gastos = gastosDAO.getGastosPorCategoria(categoriaGasto.getId());

        Log.i("Script","Valor total :" + symbol + numberFormat.format(gastosDAO.getTotalGastosPorCategoria(categoriaGasto.getId())));


        categoriaTitle.setText(categoriaGasto.getTitle());
        categoriaTitle.setTextColor(Color.parseColor(categoriaGasto.getCor()));

        valorTotal.setText(symbol + numberFormat.format(gastosDAO.getTotalGastosPorCategoria(categoriaGasto.getId())));
        valorTotal.setTextColor(Color.parseColor(categoriaGasto.getCor()));

        customProgress.setProgressColor(Color.parseColor(categoriaGasto.getCor()));
        customProgress.setMaximumPercentage(1.0f);



        ListaHomeFragmentAdapter adapter = new ListaHomeFragmentAdapter(getActivity().getApplicationContext(),gastos);
        listFragment.setAdapter(adapter);
        listFragment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),EditarGastoFragmentActivity.class);
                intent.putExtra("gasto",gastos.get(position));
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return view;
    }


}
