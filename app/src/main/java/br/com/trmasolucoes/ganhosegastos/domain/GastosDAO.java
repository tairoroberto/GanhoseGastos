package br.com.trmasolucoes.ganhosegastos.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.trmasolucoes.ganhosegastos.model.Gasto;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;


public class GastosDAO {

	private SQLiteDatabase db;
	
	public GastosDAO(Context context) {
		DBCore dbCore = new  DBCore(context);
		db = dbCore.getWritableDatabase();
	}
	
	public void inserir(Gasto gasto) {
		ContentValues values = new ContentValues();
		values.put("valor", String.valueOf(gasto.getValor()));
		values.put("dataVencimento", getDateToString(gasto.getDataVencimento()));
		values.put("dataPagamento", getDateToString(gasto.getDataPagamento()));
		values.put("dataCadastro", getDateToString(gasto.getDataCadastro()));
		values.put("comentarios", gasto.getComentarios());
		values.put("pago", String.valueOf(gasto.isPago()));
        values.put("categoria", gasto.getCategoria());
        values.put("formaPagamento", gasto.getFormaPagamento());

        Log.i("Script", "Valor: " + String.valueOf(gasto.getValor()));
        Log.i("Script","Data Vencimento: " + getDateToString(gasto.getDataPagamento()));
        Log.i("Script","Data pagamneto: " + getDateToString(gasto.getDataPagamento()));
        Log.i("Script","Data cadastro: " + getDateToString(gasto.getDataCadastro()));
        Log.i("Script","Descrição: " + gasto.getComentarios());
        Log.i("Script","Pago: " + String.valueOf(gasto.isPago()));
        Log.i("Script","Categoria: " + gasto.getCategoria());
        Log.i("Script","Forma de Pagamento: " + gasto.getFormaPagamento());
		
		db.insert("gastos", null, values);
	}


	public void atualizar(Gasto gasto) {
		ContentValues values = new ContentValues();
        values.put("valor", String.valueOf(gasto.getValor()));
        values.put("dataVencimento", getDateToString(gasto.getDataVencimento()));
        values.put("dataPagamento", getDateToString(gasto.getDataPagamento()));
       // values.put("dataCadastro", getDateToString(gasto.getDataCadastro()));
        values.put("comentarios", gasto.getComentarios());
        values.put("pago", String.valueOf(gasto.isPago()));
        values.put("categoria", gasto.getCategoria());
        values.put("formaPagamento", gasto.getFormaPagamento());
		
		db.update("gastos", values, "_id = ?", new String[]{""+gasto.getId()});
	}

	
	public void deletar(Gasto gasto) {
		db.delete("gastos", "_id = ?", new String[]{""+gasto.getId()});
	}
	
	

	public  List<Gasto> buscar() {
		List<Gasto> list = new ArrayList<Gasto>();
		String[] columns = {"_id","valor","dataVencimento","dataPagamento","dataCadastro","comentarios","pago","categoria","formaPagamento"};
		Cursor cursor = db.query("gastos", columns, null, null, null, null, "_id");
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			
			do {
                Gasto gasto = new Gasto();
				gasto.setId(cursor.getLong(0));
				gasto.setValor(new BigDecimal(cursor.getString(1)));
				gasto.setDataVencimento(this.formataData(cursor.getString(2)));
				gasto.setDataPagamento(this.formataData(cursor.getString(3)));
				gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
				gasto.setCategoria(cursor.getInt(7));
				gasto.setFormaPagamento(cursor.getInt(8));
				
				list.add(gasto);
			} while (cursor.moveToNext());
		}
        cursor.close();
		return(list);
	}


    public  List<Gasto> buscarMes() {
        List<Gasto> list = new ArrayList<Gasto>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' ";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getLong(0));
                gasto.setValor(new BigDecimal(cursor.getString(1)));
                gasto.setDataVencimento(this.formataData(cursor.getString(2)));
                gasto.setDataPagamento(this.formataData(cursor.getString(3)));
                gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
                gasto.setCategoria(cursor.getInt(7));
                gasto.setFormaPagamento(cursor.getInt(8));

                list.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }


    public  List<Gasto> buscarMes(Date data) {
        List<Gasto> list = new ArrayList<Gasto>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(data));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(data));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' ";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getLong(0));
                gasto.setValor(new BigDecimal(cursor.getString(1)));
                gasto.setDataVencimento(this.formataData(cursor.getString(2)));
                gasto.setDataPagamento(this.formataData(cursor.getString(3)));
                gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
                gasto.setCategoria(cursor.getInt(7));
                gasto.setFormaPagamento(cursor.getInt(8));

                list.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }



    public  List<Gasto> buscarMesPorCategoria(long categoria) {
        List<Gasto> list = new ArrayList<Gasto>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' AND gastos.categoria = "+ categoria +"";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getLong(0));
                gasto.setValor(new BigDecimal(cursor.getString(1)));
                gasto.setDataVencimento(this.formataData(cursor.getString(2)));
                gasto.setDataPagamento(this.formataData(cursor.getString(3)));
                gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
                gasto.setCategoria(cursor.getInt(7));
                gasto.setFormaPagamento(cursor.getInt(8));

                list.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }


    public  List<Gasto> buscarMesPorFormaPagamento(long formaPagamento) {
        List<Gasto> list = new ArrayList<Gasto>();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' AND gastos.formaPagamento = "+ formaPagamento +"";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getLong(0));
                gasto.setValor(new BigDecimal(cursor.getString(1)));
                gasto.setDataVencimento(this.formataData(cursor.getString(2)));
                gasto.setDataPagamento(this.formataData(cursor.getString(3)));
                gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
                gasto.setCategoria(cursor.getInt(7));
                gasto.setFormaPagamento(cursor.getInt(8));

                list.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }



	
    public Gasto getGasto(long id) {
        Gasto gasto = new Gasto();

        String[] columns = {"_id", "valor", "dataVencimento", "dataPagamento", "dataCadastro", "comentarios", "pago", "categoria", "formaPagamento"};
        String where = "_id = ?";

        Cursor cursor = db.query("gastos", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            gasto.setId(cursor.getLong(0));
            gasto.setValor(new BigDecimal(cursor.getString(1)));
            gasto.setDataVencimento(this.formataData(cursor.getString(2)));
            gasto.setDataPagamento(this.formataData(cursor.getString(3)));
            gasto.setDataCadastro(this.formataData(cursor.getString(4)));
            gasto.setComentarios(cursor.getString(5));
            gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
            gasto.setCategoria(cursor.getInt(7));
            gasto.setFormaPagamento(cursor.getInt(8));

            cursor.close();
            return gasto;
        } else {
            cursor.close();
            return gasto;
        }
    }


    public List<Gasto> getGastosPorCategoria(long categoria) {
        List<Gasto> list = new ArrayList<Gasto>();

        String[] columns = {"_id","valor","dataVencimento","dataPagamento","dataCadastro","comentarios","pago","categoria","formaPagamento"};
        String where = "categoria = ?";
        Cursor cursor = db.query("gastos", columns, where, new String[]{"" + categoria}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getLong(0));
                gasto.setValor(new BigDecimal(cursor.getString(1)));
                gasto.setDataVencimento(this.formataData(cursor.getString(2)));
                gasto.setDataPagamento(this.formataData(cursor.getString(3)));
                gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
                gasto.setCategoria(cursor.getInt(7));
                gasto.setFormaPagamento(cursor.getInt(8));

                list.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }


    public BigDecimal getTotalGastosPorCategoria(long categoria) {
        Double valorTotal = 0.0;

        String[] columns = {"_id","valor","dataVencimento","dataPagamento","dataCadastro","comentarios","pago","categoria","formaPagamento"};
        String where = "categoria = ?";
        Cursor cursor = db.query("gastos", columns, where, new String[]{"" + categoria}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                valorTotal += Double.parseDouble(cursor.getString(1));


            } while (cursor.moveToNext());
        }
        cursor.close();
        return(new BigDecimal(valorTotal));
    }




    public List<Gasto> getGastosPorFormaPagamneto(long formaPagamneto) {
        List<Gasto> list = new ArrayList<Gasto>();

        String[] columns = {"_id","valor","dataVencimento","dataPagamento","dataCadastro","comentarios","pago","categoria","formaPagamento"};
        String where = "formaPagamneto = ?";
        Cursor cursor = db.query("gastos", columns, where, new String[]{"" + formaPagamneto}, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Gasto gasto = new Gasto();
                gasto.setId(cursor.getLong(0));
                gasto.setValor(new BigDecimal(cursor.getString(1)));
                gasto.setDataVencimento(this.formataData(cursor.getString(2)));
                gasto.setDataPagamento(this.formataData(cursor.getString(3)));
                gasto.setDataCadastro(this.formataData(cursor.getString(4)));
                gasto.setComentarios(cursor.getString(5));
                gasto.setPago(Boolean.parseBoolean(cursor.getString(6)));
                gasto.setCategoria(cursor.getInt(7));
                gasto.setFormaPagamento(cursor.getInt(8));

                list.add(gasto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return(list);
    }


    public BigDecimal getTotalGastos() {
        Double valorTotal = 0.0;

        String[] columns = {"_id","valor","dataVencimento","dataPagamento","dataCadastro","comentarios","pago","categoria","formaPagamento"};
        Cursor cursor = db.query("gastos", columns, null, null, null, null, "_id");

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {

                valorTotal += Double.parseDouble(cursor.getString(1));


            } while (cursor.moveToNext());
        }
        cursor.close();
        return(new BigDecimal(valorTotal));
    }


    public BigDecimal getTotalGastosMes() {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' ";
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


    public BigDecimal getTotalGastosMes(Date data) {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(data));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(data));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' ";
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



    public BigDecimal getTotalGastosMesCategoria(long categoria) {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' AND gastos.categoria = "+ categoria +"";
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



    public BigDecimal getTotalGastosMesCategoria(long categoria, Date data) {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(data));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(data));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' AND gastos.categoria = "+ categoria +"";
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




    public BigDecimal getGastosPorFormaPagamento(long formaPagamento) {
        Double valorTotal = 0.0;

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(DateUtil.getDataHoje()));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(DateUtil.getDataHoje()));

        String MY_QUERY = "SELECT * FROM gastos WHERE gastos.dataVencimento >= '"+ inicio_mes  +"' AND " +
                "gastos.dataVencimento <= '"+ fim_mes +"' AND gastos.formaPagamento = "+ formaPagamento +"";
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

	

