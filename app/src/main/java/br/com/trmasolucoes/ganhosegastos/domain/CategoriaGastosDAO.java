package br.com.trmasolucoes.ganhosegastos.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class CategoriaGastosDAO {

	private SQLiteDatabase db;
    private Context context;

	public CategoriaGastosDAO(Context context) {
		DBCore dbCore = new  DBCore(context);
        this.context = context;
		db = dbCore.getWritableDatabase();
	}

	public void inserir(CategoriaGasto categoriaGasto) {
		ContentValues values = new ContentValues();
		values.put("title", categoriaGasto.getTitle());
        values.put("cor", categoriaGasto.getCor());

		db.insert("categoria_gastos", null, values);
	}

    public void inserirDefault() {

        ContentValues values1 = new ContentValues();
        values1.put("title", "Educação");
        values1.put("cor", "#FF1493");

        ContentValues values2 = new ContentValues();
        values2.put("title", "Transporte");
        values2.put("cor", "#008B8B");

        ContentValues values3 = new ContentValues();
        values3.put("title", "Lazer");
        values3.put("cor", "#DC143C");

        ContentValues values4 = new ContentValues();
        values4.put("title", "Alimentação");
        values4.put("cor", "#00FF7F");

        ContentValues values5 = new ContentValues();
        values5.put("title", "Saúde");
        values5.put("cor", "#FF0000");

        ContentValues values6 = new ContentValues();
        values6.put("title", "Outros");
        values6.put("cor", "#00BFFF");

        db.insert("categoria_gastos", null, values1);
        db.insert("categoria_gastos", null, values2);
        db.insert("categoria_gastos", null, values3);
        db.insert("categoria_gastos", null, values4);
        db.insert("categoria_gastos", null, values5);
        db.insert("categoria_gastos", null, values6);
    }


	public void atualizar(CategoriaGasto categoriaGasto) {
        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(context);
        //busca a categoria de outros
        CategoriaGasto outros = categoriaGastosDAO.getCategoriaOutros();

        if (categoriaGasto.getId() == outros.getId()){
            Toast.makeText(context, "NÃO É POSSIVEL ATUALIZAR CATEGORIA", Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();
            values.put("title", categoriaGasto.getTitle());
            values.put("cor", categoriaGasto.getCor());

            db.update("categoria_gastos", values, "_id = ?", new String[]{""+categoriaGasto.getId()});
        }
	}


	public void deletar(CategoriaGasto categoriaGasto) {
        CategoriaGastosDAO categoriaGastosDAO = new CategoriaGastosDAO(context);
        GastosDAO gastosDAO = new GastosDAO(context);

        //busca a categoria de outros
        CategoriaGasto outros = categoriaGastosDAO.getCategoriaOutros();

        if (categoriaGasto.getId() == outros.getId()){
            Toast.makeText(context, "NÃO É POSSIVEL EXCLUIR CATEGORIA", Toast.LENGTH_SHORT).show();
        }else {
            //busca todos os ganhos da categoria que será deletada
            List<Gasto> gastos = gastosDAO.getGastosPorCategoria(categoriaGasto.getId());

            //tranfere os ganhos para a categoria de "Outros"
            for (int i = 0; i < gastos.size(); i++) {
                gastos.get(i).setCategoria((int) outros.getId());
                gastosDAO.atualizar(gastos.get(i));
            }
            db.delete("categoria_gastos", "_id = ?", new String[]{""+categoriaGasto.getId()});
        }
	}


    public CategoriaGasto  getCategoriaOutros() {
        CategoriaGasto categoriaGasto = new CategoriaGasto();

        String[] columns = {"_id", "title", "cor"};
        String where = "title = ? AND cor = ?";

        Cursor cursor = db.query("categoria_gastos", columns, where, new String[]{"Outros", "#00BFFF"}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            categoriaGasto.setId(cursor.getLong(0));
            categoriaGasto.setTitle(cursor.getString(1));
            categoriaGasto.setCor(cursor.getString(2));

            cursor.close();
            return categoriaGasto;
        } else {
            cursor.close();
            return categoriaGasto;
        }
    }



	public  List<CategoriaGasto> buscar() {
		List<CategoriaGasto> list = new ArrayList<CategoriaGasto>();
		String[] columns = {"_id","title","cor"};
		Cursor cursor = db.query("categoria_gastos", columns, null, null, null, null, "_id");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			do {
                CategoriaGasto categoriaGasto = new CategoriaGasto();
				categoriaGasto.setId(cursor.getLong(0));
				categoriaGasto.setTitle((cursor.getString(1)));
				categoriaGasto.setCor(cursor.getString(2));

				list.add(categoriaGasto);
			} while (cursor.moveToNext());
		}
        cursor.close();
		return(list);
	}

    public  List<CategoriaGasto> buscarComValores() {
        List<CategoriaGasto> list = new ArrayList<CategoriaGasto>();

        String[] columns = {"_id","title","cor"};
        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM categoria_gastos INNER JOIN gastos ON categoria_gastos._id = gastos.categoria " +
                "WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' GROUP BY categoria_gastos._id";

        Cursor cursor = db.rawQuery(MY_QUERY, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                CategoriaGasto categoriaGasto = new CategoriaGasto();
                categoriaGasto.setId(cursor.getLong(0));
                categoriaGasto.setTitle((cursor.getString(1)));
                categoriaGasto.setCor(cursor.getString(2));

                list.add(categoriaGasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }



    public CategoriaGasto  getCategoriaGasto(long id) {
        CategoriaGasto categoriaGasto = new CategoriaGasto();

        String[] columns = {"_id", "title", "cor"};
        String where = "_id = ?";

        Cursor cursor = db.query("categoria_gastos", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            categoriaGasto.setId(cursor.getLong(0));
            categoriaGasto.setTitle(cursor.getString(1));
            categoriaGasto.setCor(cursor.getString(2));

            cursor.close();
            return categoriaGasto;
        } else {
            cursor.close();
            return categoriaGasto;
        }
    }

}

	

