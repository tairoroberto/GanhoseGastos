package br.com.trmasolucoes.ganhosegastos.domain;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCore extends SQLiteOpenHelper{
	private static final String NOME_DB = "GanhoseGastos_db";
	private static final int VERSAO_DB = 1;

	public DBCore(Context context) {
		super(context, NOME_DB, null, VERSAO_DB);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

        db.execSQL("create table ganhos("
                + "_id integer primary key autoincrement,"
                + "valor text not null,"
                + "dataPagamento datetime,"
                + "dataCadastro datetime not null,"
                + "comentarios text not null,"
                + "categoria integer not null);");
		
		db.execSQL("create table gastos("
				+ "_id integer primary key autoincrement,"
                + "valor text not null,"
                + "dataVencimento datetime not null,"
                + "dataPagamento datetime,"
                + "dataCadastro datetime not null,"
                + "comentarios text not null,"
                + "pago text not null,"
                + "categoria integer not null,"
                + "formaPagamento integer );");
		
		db.execSQL("create table categoria_ganhos("
				+ "_id integer primary key autoincrement,"
				+ "title text not null,"
				+ "cor text not null);");

        db.execSQL("create table categoria_gastos("
                + "_id integer primary key autoincrement,"
                + "title text not null,"
                + "cor text not null);");

        db.execSQL("create table forma_pagamento("
                + "_id integer primary key autoincrement,"
                + "title text not null,"
                + "cor text not null);");

        db.execSQL("create table fundo_caixa("
                + "_id integer primary key autoincrement,"
                + "valorFundo text,"
                + "dataInclusao datetime);");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table ganhos");
		db.execSQL("drop table gastos");
		db.execSQL("drop table categoria_ganhos");
        db.execSQL("drop table categoria_gastos");
        db.execSQL("drop table forma_pagamento");
		onCreate(db);		
	}
	
}
