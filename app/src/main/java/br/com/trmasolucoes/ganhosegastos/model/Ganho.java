package br.com.trmasolucoes.ganhosegastos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tairo on 23/03/15.
 */
public class Ganho implements Parcelable{
    private long id;
    private BigDecimal valor;
    private Date dataPagamento;
    private Date dataCadastro;
    private String comentarios;
    private int categoria;


    public Ganho(long id, BigDecimal valor, Date dataPagamento, Date dataCadastro, String comentarios, int categoria) {
        this.id = id;
        this.valor = valor;
        this.dataPagamento = dataPagamento;
        this.dataCadastro = dataCadastro;
        this.comentarios = comentarios;
        this.categoria = categoria;
    }

    public Ganho() {
    }

    public Ganho(Parcel parcel) {
        this.id = parcel.readLong();
        this.valor = (BigDecimal) parcel.readValue(BigDecimal.class.getClassLoader());
        this.dataPagamento = (Date) parcel.readValue(Date.class.getClassLoader());
        this.dataCadastro = (Date) parcel.readValue(Date.class.getClassLoader());
        this.comentarios = parcel.readString();
        this.categoria = parcel.readInt();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Date getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(Date dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeValue(valor);
        dest.writeValue(dataPagamento);
        dest.writeValue(dataCadastro);
        dest.writeString(comentarios);
        dest.writeInt(categoria);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<Ganho> CREATOR = new Creator<Ganho>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public Ganho createFromParcel(Parcel source) {
            return new Ganho(source);
        }

        @Override
        public Ganho[] newArray(int size) {
            return new Ganho[size];
        }
    };
}
