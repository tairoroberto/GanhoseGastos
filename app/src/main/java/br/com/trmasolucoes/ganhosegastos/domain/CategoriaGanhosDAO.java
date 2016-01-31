package br.com.trmasolucoes.ganhosegastos.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.model.CategoriaGanho;
import br.com.trmasolucoes.ganhosegastos.model.CategoriaGasto;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class CategoriaGanhosDAO {

	private SQLiteDatabase db;
    private Context context;

	public CategoriaGanhosDAO(Context context) {
		DBCore dbCore = new  DBCore(context);
        this.context = context;
		db = dbCore.getWritableDatabase();
	}

	public void inserir(CategoriaGanho categoriaGanho) {
		ContentValues values = new ContentValues();
		values.put("title", categoriaGanho.getTitle());
        values.put("cor", categoriaGanho.getCor());

		db.insert("categoria_ganhos", null, values);
	}


	public void atualizar(CategoriaGanho categoriaGanho) {
        CategoriaGanhosDAO categoriaGanhosDAO = new CategoriaGanhosDAO(context);
        //busca a categoria de outros
        CategoriaGanho outros = categoriaGanhosDAO.getCategoriaOutros();

        if (categoriaGanho.getId() == outros.getId()){
            Toast.makeText(context,"NÃO É POSSIVEL ATUALIZAR CATEGORIA",Toast.LENGTH_SHORT).show();
        }else{
            ContentValues values = new ContentValues();
            values.put("title", categoriaGanho.getTitle());
            values.put("cor", categoriaGanho.getCor());

            db.update("categoria_ganhos", values, "_id = ?", new String[]{""+categoriaGanho.getId()});
        }
	}


    public void inserirDefault() {

        ContentValues values1 = new ContentValues();
        values1.put("title", "Salário");
        values1.put("cor", "#FF1493");

        ContentValues values2 = new ContentValues();
        values2.put("title", "Outros");
        values2.put("cor", "#00BFFF");

        db.insert("categoria_ganhos", null, values1);
        db.insert("categoria_ganhos", null, values2);
    }


	public void deletar(CategoriaGanho categoriaGanho) {

        CategoriaGanhosDAO categoriaGanhosDAO = new CategoriaGanhosDAO(context);
        GanhosDAO ganhosDAO = new GanhosDAO(context);

        //busca a categoria de outros
        CategoriaGanho outros = categoriaGanhosDAO.getCategoriaOutros();

        if (categoriaGanho.getId() == outros.getId()){
            Toast.makeText(context,"NÃO É POSSIVEL EXCLUIR CATEGORIA",Toast.LENGTH_SHORT).show();
        }else{
            //busca todos os ganhos da categoria que será deletada
            List<Ganho> ganhos = ganhosDAO.getGanhosPorCategoria(categoriaGanho.getId());

            //tranfere os ganhos para a categoria de "Outros"
            for (int i = 0; i < ganhos.size(); i++) {
                ganhos.get(i).setCategoria((int) outros.getId());
                ganhosDAO.atualizar(ganhos.get(i));
            }

            //deleta a categoria
            db.delete("categoria_ganhos", "_id = ?", new String[]{""+categoriaGanho.getId()});
        }
	}



    public CategoriaGanho  getCategoriaOutros() {
        CategoriaGanho categoriaGanho = new CategoriaGanho();

        String[] columns = {"_id", "title", "cor"};
        String where = "title = ? AND cor = ?";

        Cursor cursor = db.query("categoria_ganhos", columns, where, new String[]{"Outros", "#00BFFF"}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            categoriaGanho.setId(cursor.getLong(0));
            categoriaGanho.setTitle(cursor.getString(1));
            categoriaGanho.setCor(cursor.getString(2));

            cursor.close();
            return categoriaGanho;
        } else {
            cursor.close();
            return categoriaGanho;
        }
    }



	public  List<CategoriaGanho> buscar() {
		List<CategoriaGanho> list = new ArrayList<CategoriaGanho>();
		String[] columns = {"_id","title","cor"};
		Cursor cursor = db.query("categoria_ganhos", columns, null, null, null, null, "_id");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			do {
                CategoriaGanho categoriaGanho = new CategoriaGanho();
				categoriaGanho.setId(cursor.getLong(0));
				categoriaGanho.setTitle((cursor.getString(1)));
				categoriaGanho.setCor(cursor.getString(2));

				list.add(categoriaGanho);
			} while (cursor.moveToNext());
		}
        cursor.close();
		return(list);
	}

    public CategoriaGanho  getCategoriaGanho(long id) {
        CategoriaGanho categoriaGanho = new CategoriaGanho();

        String[] columns = {"_id", "title", "cor"};
        String where = "_id = ?";

        Cursor cursor = db.query("categoria_ganhos", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            categoriaGanho.setId(cursor.getLong(0));
            categoriaGanho.setTitle(cursor.getString(1));
            categoriaGanho.setCor(cursor.getString(2));

            cursor.close();
            return categoriaGanho;
        } else {
            cursor.close();
            return categoriaGanho;
        }
    }

    public  List<CategoriaGanho> buscarComValores() {
        List<CategoriaGanho> list = new ArrayList<CategoriaGanho>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM categoria_ganhos INNER JOIN gastos ON categoria_ganhos._id = ganhos.categoria " +
                "WHERE ganhos.dataPagamento >= '"+ inicio_mes  +"' AND " +
                "ganhos.dataPagamento <= '"+ fim_mes +"' GROUP BY categoria_ganhos._id";

        Cursor cursor = db.rawQuery(MY_QUERY, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                CategoriaGanho categoriaGanho = new CategoriaGanho();
                categoriaGanho.setId(cursor.getLong(0));
                categoriaGanho.setTitle((cursor.getString(1)));
                categoriaGanho.setCor(cursor.getString(2));

                list.add(categoriaGanho);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }
}

	

