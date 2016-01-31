package br.com.trmasolucoes.ganhosegastos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class CategoriaGanho implements Parcelable{


    private long id;
	private String title;
    private String cor;

    public CategoriaGanho(long id, String title, String cor) {
        this.id = id;
        this.title = title;
        this.cor = cor;
    }

    public CategoriaGanho(Parcel parcel) {
        this.id = parcel.readLong();
        this.title = parcel.readString();
        this.cor = parcel.readString();
    }

    public CategoriaGanho() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    /**
     * Comparable interface implementation
     *
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(CategoriaGanho other) {
        return (int) (id - other.getId());
    }



    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(cor);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Creator<CategoriaGanho> CREATOR = new Creator<CategoriaGanho>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public CategoriaGanho createFromParcel(Parcel source) {
            return new CategoriaGanho(source);
        }

        @Override
        public CategoriaGanho[] newArray(int size) {
            return new CategoriaGanho[size];
        }
    };
}
