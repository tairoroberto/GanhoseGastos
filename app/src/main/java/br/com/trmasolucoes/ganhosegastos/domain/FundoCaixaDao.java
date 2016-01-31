package br.com.trmasolucoes.ganhosegastos.domain;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.trmasolucoes.ganhosegastos.model.FundoCaixa;
import br.com.trmasolucoes.ganhosegastos.model.Ganho;
import br.com.trmasolucoes.ganhosegastos.util.DateUtil;

import static br.com.trmasolucoes.ganhosegastos.util.DateUtil.getDateToString;

/**
 * Created by tairo on 31/01/16.
 */
public class FundoCaixaDao {

    private SQLiteDatabase db;

    public FundoCaixaDao(Context context) {
        DBCore dbCore = new  DBCore(context);
        db = dbCore.getWritableDatabase();
    }

    public void inserir(FundoCaixa fundoCaixa) {
        ContentValues values = new ContentValues();
        values.put("valorFundo", String.valueOf(fundoCaixa.getValorFundo()));
        values.put("dataInclusao", getDateToString(fundoCaixa.getDataIclusao()));

        db.insert("fundo_caixa", null, values);
    }

    public void atualizar(FundoCaixa fundoCaixa) {
        ContentValues values = new ContentValues();
        values.put("valor", String.valueOf(fundoCaixa.getValorFundo()));
        values.put("dataVencimento", getDateToString(fundoCaixa.getDataIclusao()));

        db.update("fundo_caixa", values, "_id = ?", new String[]{"" + fundoCaixa.getId()});
    }


    public void deletar(FundoCaixa fundoCaixa) {
        db.delete("fundo_caixa", "_id = ?", new String[]{"" + fundoCaixa.getId()});
    }

    public FundoCaixa getFundoCaixa(long id) {
        FundoCaixa fundoCaixa = new FundoCaixa();

        String[] columns = {"_id", "valorFundo", "dataInclusao"};
        String where = "_id = ?";

        Cursor cursor = db.query("fundo_caixa", columns, where, new String[]{"" + id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            fundoCaixa.setId(cursor.getLong(0));
            fundoCaixa.setValorFundo(new BigDecimal(cursor.getString(1)));
            fundoCaixa.setDataIclusao(this.formataData(cursor.getString(2)));

            cursor.close();
            return fundoCaixa;
        } else {
            cursor.close();
            return fundoCaixa;
        }
    }


    public FundoCaixa  getFundoCaixaMes(Date data) {

        Double valorTotal = 0.0;
        FundoCaixa fundoCaixa = new FundoCaixa();

        String inicio_mes = DateUtil.getDateToString(DateUtil.getFirstDayMonth(data));
        String fim_mes = DateUtil.getDateToString(DateUtil.getLastDayMonth(data));

        String MY_QUERY = "SELECT * FROM fundo_caixa WHERE fundo_caixa.dataInclusao >= '"+ inicio_mes  +"' AND " +
                "fundo_caixa.dataInclusao <= '"+ fim_mes +"' ";
        Cursor cursor = db.rawQuery(MY_QUERY, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            fundoCaixa.setId(cursor.getLong(0));
            fundoCaixa.setValorFundo(new BigDecimal(cursor.getString(1)));
            fundoCaixa.setDataIclusao(this.formataData(cursor.getString(2)));

            cursor.close();
            return fundoCaixa;
        } else {
            cursor.close();
            return fundoCaixa;
        }
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
