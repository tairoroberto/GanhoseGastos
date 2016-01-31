package br.com.trmasolucoes.ganhosegastos.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class GanhosDAO {

	private SQLiteDatabase db;

	public GanhosDAO(Context context) {
		DBCore dbCore = new  DBCore(context);
		db = dbCore.getWritableDatabase();
	}

	public void inserir(Ganho ganho) {
		ContentValues values = new ContentValues();
		values.put("valor", String.valueOf(ganho.getValor()));
		values.put("dataPagamento", getDateToString(ganho.getDataPagamento()));
		values.put("dataCadastro", getDateToString(ganho.getDataCadastro()));
		values.put("comentarios", ganho.getComentarios());
        values.put("categoria", ganho.getCategoria());

		db.insert("ganhos", null, values);
	}


	public void atualizar(Ganho ganho) {
		ContentValues values = new ContentValues();
        values.put("valor", String.valueOf(ganho.getValor()));
        values.put("dataPagamento", getDateToString(ganho.getDataPagamento()));
        values.put("comentarios", ganho.getComentarios());
        values.put("categoria", ganho.getCategoria());

		db.update("ganhos", values, "_id = ?", new String[]{"" + ganho.getId()});
	}


	public void deletar(Ganho ganho) {
		db.delete("ganhos", "_id = ?", new String[]{"" + ganho.getId()});
	}



	public  List<Ganho> buscar() {
		List<Ganho> list = new ArrayList<Ganho>();
		String[] columns = {"_id","valor","dataPagamento","dataCadastro","comentarios","categoria"};
		Cursor cursor = db.query("ganhos", columns, null, null, null, null, "_id");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();

			do {
                Ganho ganho = new Ganho();
				ganho.setId(cursor.getLong(0));
				ganho.setValor(new BigDecimal(cursor.getString(1)));
				ganho.setDataPagamento(this.formataData(cursor.getString(2)));
				ganho.setDataCadastro(this.formataData(cursor.getString(3)));
                ganho.setComentarios(cursor.getString(4));
				ganho.setCategoria(cursor.getInt(5));

				list.add(ganho);
			} while (cursor.moveToNext());
		}
        cursor.close();
		return(list);
	}


    public  List<Ganho> buscarMes() {
        List<Ganho> list = new ArrayList<Ganho>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM ganhos WHERE ganhos.dataPagamento >= '"+ inicio_mes  +"' AND " +
                "ganhos.dataPagamento <= '"+ fim_mes +"' ";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Ganho ganho = new Ganho();
                ganho.setId(cursor.getLong(0));
                ganho.setValor(new BigDecimal(cursor.getString(1)));
                ganho.setDataPagamento(this.formataData(cursor.getString(2)));
                ganho.setDataCadastro(this.formataData(cursor.getString(3)));
                ganho.setComentarios(cursor.getString(4));
                ganho.setCategoria(cursor.getInt(5));

                list.add(ganho);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }

    public  List<Ganho> buscarMesPorCategoria(long categoria) {
        List<Ganho> list = new ArrayList<Ganho>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM ganhos WHERE ganhos.dataPagamento >= '"+ inicio_mes  +"' AND " +
                "ganhos.dataPagamento <= '"+ fim_mes +"' AND ganhos.categoria = "+ categoria +"";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Ganho ganho = new Ganho();
                ganho.setId(cursor.getLong(0));
                ganho.setValor(new BigDecimal(cursor.getString(1)));
                ganho.setDataPagamento(this.formataData(cursor.getString(2)));
                ganho.setDataCadastro(this.formataData(cursor.getString(3)));
                ganho.setComentarios(cursor.getString(4));
                ganho.setCategoria(cursor.getInt(5));

                list.add(ganho);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }




    public Ganho  getGanho(long id) {
        Ganho ganho = new Ganho();

        String[] columns = {"_id", "valor", "dataPagamento", "dataCadastro", "comentarios", "categoria"};
        String where = "_id = ?";

        Cursor cursor = db.query("ganhos", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            ganho.setId(cursor.getLong(0));
            ganho.setValor(new BigDecimal(cursor.getString(1)));
            ganho.setDataPagamento(this.formataData(cursor.getString(2)));
            ganho.setDataCadastro(this.formataData(cursor.getString(3)));
            ganho.setComentarios(cursor.getString(4));
            ganho.setCategoria(cursor.getInt(5));

            cursor.close();
            return ganho;
        } else {
            cursor.close();
            return ganho;
        }
    }


    public  List<Ganho> getGanhosPorCategoria(long categoria) {
        List<Ganho> list = new ArrayList<Ganho>();

        String[] columns = {"_id", "valor", "dataPagamento", "dataCadastro", "comentarios", "categoria"};
        String where = "categoria = ?";
        Cursor cursor = db.query("ganhos", columns, where, new String[]{"" + categoria}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Ganho ganho = new Ganho();
                ganho.setId(cursor.getLong(0));
                ganho.setValor(new BigDecimal(cursor.getString(1)));
                ganho.setDataPagamento(this.formataData(cursor.getString(2)));
                ganho.setDataCadastro(this.formataData(cursor.getString(3)));
                ganho.setComentarios(cursor.getString(4));
                ganho.setCategoria(cursor.getInt(5));

                list.add(ganho);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }


    public BigDecimal getTotalGanhos() {
        Double valorTotal = 0.0;

        String[] columns = {"_id","valor","dataPagamento","dataCadastro","comentarios","categoria"};
        Cursor cursor = db.query("ganhos", columns, null, null, null, null, "_id");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                valorTotal += Double.parseDouble(cursor.getString(1));


            } while (cursor.moveToNext());
        }
        cursor.close();
        return(new BigDecimal(valorTotal));
    }

    public BigDecimal getTotalGanhosMes() {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM ganhos WHERE ganhos.dataPagamento >= '"+ inicio_mes  +"' AND " +
                "ganhos.dataPagamento <= '"+ fim_mes +"' ";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                valorTotal += Double.parseDouble(cursor.getString(1));


            } while (cursor.moveToNext());
        }
        cursor.close();
        return(new BigDecimal(valorTotal));
    }

    public BigDecimal getTotalGanhosMes(Date date) {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(date));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(date));

        String MY_QUERY = "SELECT * FROM ganhos WHERE ganhos.dataPagamento >= '"+ inicio_mes  +"' AND " +
                "ganhos.dataPagamento <= '"+ fim_mes +"' ";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                valorTotal += Double.parseDouble(cursor.getString(1));


            } while (cursor.moveToNext());
        }
        cursor.close();
        return(new BigDecimal(valorTotal));
    }


    public BigDecimal getTotalGanhosMesCategoria(long categoria) {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM ganhos WHERE ganhos.dataPagamento >= '"+ inicio_mes  +"' AND " +
                "ganhos.dataPagamento <= '"+ fim_mes +"' AND ganhos.categoria = "+ categoria + "";

        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                valorTotal += Double.parseDouble(cursor.getString(1));


            } while (cursor.moveToNext());
        }
        cursor.close();
        return(new BigDecimal(valorTotal));
    }


    private Date formataData(String data) {
        if (data == null || data.equals(""))
            return null;

        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            date = (Date) formatter.parse(data);
        } catch (ParseException e) {
             e.printStackTrace();
        }
        return date;
    }

    private String getDateToString(Date date){
        if (date == null || date.equals(""))
            return null;

        DateFormat formatador = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//new SimpleDateFormat("dd/MM/yyyy");
        return formatador.format(date);
    }

}

	

