package br.com.trmasolucoes.ganhosegastos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by tairo on 31/01/16.
 */
public class FundoCaixa  implements Parcelable{
    private long id;
    private BigDecimal valorFundo;
    private Date dataIclusao;

    public FundoCaixa() {
    }

    public FundoCaixa(long id, BigDecimal valorFundo, Date dataIclusao) {
        this.id = id;
        this.valorFundo = valorFundo;
        this.dataIclusao = dataIclusao;
    }

    protected FundoCaixa(Parcel parcel) {
        this.id = parcel.readLong();
        this.valorFundo = (BigDecimal) parcel.readValue(BigDecimal.class.getClassLoader());
        this.dataIclusao = (Date) parcel.readValue(Date.class.getClassLoader());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getValorFundo() {
        return valorFundo;
    }

    public void setValorFundo(BigDecimal valorFundo) {
        this.valorFundo = valorFundo;
    }

    public Date getDataIclusao() {
        return dataIclusao;
    }

    public void setDataIclusao(Date dataIclusao) {
        this.dataIclusao = dataIclusao;
    }

    public static final Creator<FundoCaixa> CREATOR = new Creator<FundoCaixa>() {
        @Override
        public FundoCaixa createFromParcel(Parcel in) {
            return new FundoCaixa(in);
        }

        @Override
        public FundoCaixa[] newArray(int size) {
            return new FundoCaixa[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeValue(valorFundo);
        dest.writeValue(dataIclusao);
    }
}
