package br.com.trmasolucoes.ganhosegastos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tairo on 23/03/15.
 */
public class Gasto implements Parcelable{
    private long id;
    private BigDecimal valor;
    private Date dataVencimento;
    private Date dataPagamento;
    private Date dataCadastro;
    private String comentarios;
    private Boolean pago;
    private int categoria;
    private int formaPagamento;


    public Gasto(long id, BigDecimal valor, Date dataVencimento, Date dataPagamento,Date dataCadastro, String comentarios, boolean pago, int categoria, int formaPagamento) {
        this.id = id;
        this.valor = valor;
        this.dataVencimento = dataVencimento;
        this.dataPagamento = dataPagamento;
        this.dataCadastro = dataCadastro;
        this.comentarios = comentarios;
        this.pago = pago;
        this.categoria = categoria;
        this.formaPagamento = formaPagamento;
    }

    public Gasto() {
    }

    public Gasto(Parcel parcel) {
        this.id = parcel.readLong();
        this.valor = (BigDecimal) parcel.readValue(BigDecimal.class.getClassLoader());
        this.dataVencimento = (Date) parcel.readValue(Date.class.getClassLoader());
        this.dataPagamento = (Date) parcel.readValue(Date.class.getClassLoader());
        this.dataCadastro = (Date) parcel.readValue(Date.class.getClassLoader());
        this.comentarios = parcel.readString();
        this.pago = (Boolean) parcel.readValue(Boolean.class.getClassLoader());
        this.categoria = parcel.readInt();
        this.formaPagamento = parcel.readInt();
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

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public Boolean isPago() {
        return pago;
    }

    public void setPago(Boolean pago) {
        this.pago = pago;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(int formaPagamento) {
        this.formaPagamento = formaPagamento;
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
        dest.writeValue(dataVencimento);
        dest.writeValue(dataPagamento);
        dest.writeValue(dataCadastro);
        dest.writeString(comentarios);
        dest.writeValue(pago);
        dest.writeInt(categoria);
        dest.writeInt(formaPagamento);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Parcelable.Creator<Gasto> CREATOR = new Parcelable.Creator<Gasto>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public Gasto createFromParcel(Parcel source) {
            return new Gasto(source);
        }

        @Override
        public Gasto[] newArray(int size) {
            return new Gasto[size];
        }
    };
}
