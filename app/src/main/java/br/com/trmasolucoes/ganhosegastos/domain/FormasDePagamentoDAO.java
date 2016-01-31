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
import br.com.trmasolucoes.ganhosegastos.model.FormaPagamento;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;


public class FormasDePagamentoDAO {

	private SQLiteDatabase db;
    private Context context;

	public FormasDePagamentoDAO(Context context) {
		DBCore dbCore = new  DBCore(context);
        this.context = context;
		db = dbCore.getWritableDatabase();
	}

	public void inserir(FormaPagamento formaPagamento) {
		ContentValues values = new ContentValues();
		values.put("title", formaPagamento.getTitle());
        values.put("cor", formaPagamento.getCor());

		db.insert("forma_pagamento", null, values);
	}


    public void inserirDefault() {
        ContentValues values1 = new ContentValues();
        values1.put("title", "Dinheiro");
        values1.put("cor", "#FF1493");

        ContentValues values2 = new ContentValues();
        values2.put("title", "Cartão de Crédito");
        values2.put("cor", "#00BFFF");

        ContentValues values3 = new ContentValues();
        values3.put("title", "Cartão de Débito");
        values3.put("cor", "#DC143C");

        ContentValues values4 = new ContentValues();
        values4.put("title", "Vale Alimentação");
        values4.put("cor", "#00FF7F");

        ContentValues values5 = new ContentValues();
        values5.put("title", "Vale Refeição");
        values5.put("cor", "#FF0000");

        ContentValues values6 = new ContentValues();
        values6.put("title", "Outros");
        values6.put("cor", "#00BFFF");

        db.insert("forma_pagamento", null, values1);
        db.insert("forma_pagamento", null, values2);
        db.insert("forma_pagamento", null, values3);
        db.insert("forma_pagamento", null, values4);
        db.insert("forma_pagamento", null, values5);
        db.insert("forma_pagamento", null, values6);
    }


	public void atualizar(FormaPagamento formaPagamento) {
        FormasDePagamentoDAO formasDePagamentoDAO = new FormasDePagamentoDAO(context);
        //busca a categoria de outros
        FormaPagamento outros = formasDePagamentoDAO.getCategoriaOutros();

        if (formaPagamento.getId() == outros.getId()){
            Toast.makeText(context, "NÃO É POSSIVEL ATUALIZAR FORMA DE PAGAMENTO", Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();
            values.put("title", formaPagamento.getTitle());
            values.put("cor", formaPagamento.getCor());

            db.update("forma_pagamento", values, "_id = ?", new String[]{""+formaPagamento.getId()});
        }
	}


	public void deletar(FormaPagamento formaPagamento) {
        FormasDePagamentoDAO formasDePagamentoDAO = new FormasDePagamentoDAO(context);
        GastosDAO gastosDAO = new GastosDAO(context);

        //busca a categoria de outros
        FormaPagamento outros = formasDePagamentoDAO.getCategoriaOutros();

        if (formaPagamento.getId() == outros.getId()){
            Toast.makeText(context, "NÃO É POSSIVEL EXCLUIR FORMA DE PAGAMENTO", Toast.LENGTH_SHORT).show();
        }else {
            //busca todos os ganhos da categoria que será deletada
            List<Gasto> gastos = gastosDAO.buscarMesPorFormaPagamento(formaPagamento.getId());

            //tranfere os ganhos para a categoria de "Outros"
            for (int i = 0; i < gastos.size(); i++) {
                gastos.get(i).setFormaPagamento((int) outros.getId());
                gastosDAO.atualizar(gastos.get(i));
            }

            db.delete("forma_pagamento", "_id = ?", new String[]{""+formaPagamento.getId()});
        }
	}


    public FormaPagamento  getCategoriaOutros() {
        FormaPagamento formaPagamento = new FormaPagamento();

        String[] columns = {"_id", "title", "cor"};
        String where = "title = ? AND cor = ?";

        Cursor cursor = db.query("forma_pagamento", columns, where, new String[]{"Outros", "#00BFFF"}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            formaPagamento.setId(cursor.getLong(0));
            formaPagamento.setTitle(cursor.getString(1));
            formaPagamento.setCor(cursor.getString(2));

            cursor.close();
            return formaPagamento;
        } else {
            cursor.close();
            return formaPagamento;
        }
    }



	public  List<FormaPagamento> buscar() {
		List<FormaPagamento> list = new ArrayList<FormaPagamento>();
		String[] columns = {"_id","title","cor"};
		Cursor cursor = db.query("forma_pagamento", columns, null, null, null, null, "_id");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			do {
                FormaPagamento formaPagamento = new FormaPagamento();
				formaPagamento.setId(cursor.getLong(0));
				formaPagamento.setTitle((cursor.getString(1)));
				formaPagamento.setCor(cursor.getString(2));

				list.add(formaPagamento);
			} while (cursor.moveToNext());
		}
        cursor.close();
		return(list);
	}



    public FormaPagamento  getFormaPagamento(long id) {
        FormaPagamento formaPagamento = new FormaPagamento();

        String[] columns = {"_id", "title", "cor"};
        String where = "_id = ?";

        Cursor cursor = db.query("forma_pagamento", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            formaPagamento.setId(cursor.getLong(0));
            formaPagamento.setTitle(cursor.getString(1));
            formaPagamento.setCor(cursor.getString(2));

            cursor.close();
            return formaPagamento;
        } else {
            cursor.close();
            return formaPagamento;
        }
    }
}

	

