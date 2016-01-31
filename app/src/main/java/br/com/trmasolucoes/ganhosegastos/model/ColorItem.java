package br.com.trmasolucoes.ganhosegastos.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tairo on 21/03/15.
 */
public class ColorItem implements Parcelable{
    private long id;
    private String name;


    public ColorItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public ColorItem(Parcel parcel) {
        this.id = parcel.readLong();
        this.name = parcel.readString();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Escreve os valores que serão transportados
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
    }

    //Obs: Obrigatorio usar o CREATOR para recuperar os dados do parcelable
    public static final Parcelable.Creator<ColorItem> CREATOR = new Parcelable.Creator<ColorItem>() {

        //Pega o nosso parcel e instacia para podermos utilizar o nosso objeto "No caso o Studant"
        @Override
        public ColorItem createFromParcel(Parcel source) {
            return new ColorItem(source);
        }

        @Override
        public ColorItem[] newArray(int size) {
            return new ColorItem[size];
        }
    };
}
