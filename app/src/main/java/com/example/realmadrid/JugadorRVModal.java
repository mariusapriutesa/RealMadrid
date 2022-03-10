package com.example.realmadrid;

import android.os.Parcel;
import android.os.Parcelable;

public class JugadorRVModal implements Parcelable {
    //creating variables for our different fields.
    private String jugadorName;
    private String jugadorDescription;
    private String jugadorPrice;
    private String bestPosicion;
    private String jugadorImg;
    private String jugadorLink;
    private String jugadorId;


    public String getJugadorId() {
        return jugadorId;
    }

    public void setJugadorId(String jugadorId) {
        this.jugadorId = jugadorId;
    }


    //creando un constructor vacio.
    public JugadorRVModal() {

    }

    protected JugadorRVModal(Parcel in) {
        jugadorName = in.readString();
        jugadorId = in.readString();
        jugadorDescription = in.readString();
        jugadorPrice = in.readString();
        bestPosicion = in.readString();
        jugadorImg = in.readString();
        jugadorLink = in.readString();
    }

    public static final Creator<JugadorRVModal> CREATOR = new Creator<JugadorRVModal>() {
        @Override
        public JugadorRVModal createFromParcel(Parcel in) {
            return new JugadorRVModal(in);
        }

        @Override
        public JugadorRVModal[] newArray(int size) {
            return new JugadorRVModal[size];
        }
    };

    //creating getter and setter methods.
    public String getJugadorName() {
        return jugadorName;
    }

    public void setJugadorName(String jugadorName) {
        this.jugadorName = jugadorName;
    }

    public String getJugadorDescription() {
        return jugadorDescription;
    }

    public void setJugadorDescription(String jugadorDescription) {
        this.jugadorDescription = jugadorDescription;
    }

    public String getJugadorPrice() {
        return jugadorPrice;
    }

    public void setJugadorPrice(String jugadorPrice) {
        this.jugadorPrice = jugadorPrice;
    }

    public String getBestPosicion() {
        return bestPosicion;
    }

    public void setBestPosicion(String bestPosicion) {
        this.bestPosicion = bestPosicion;
    }

    public String getJugadorImg() {
        return jugadorImg;
    }

    public void setJugadorImg(String jugadorImg) {
        this.jugadorImg = jugadorImg;
    }

    public String getJugadorLink() {
        return jugadorLink;
    }

    public void setJugadorLink(String jugadorLink) {
        this.jugadorLink = jugadorLink;
    }


    public JugadorRVModal(String jugadorId, String jugadorName, String jugadorDescription, String jugadorPrice, String bestPosicion, String jugadorImg, String jugadorLink) {
        this.jugadorName = jugadorName;
        this.jugadorId = jugadorId;
        this.jugadorDescription = jugadorDescription;
        this.jugadorPrice = jugadorPrice;
        this.bestPosicion = bestPosicion;
        this.jugadorImg = jugadorImg;
        this.jugadorLink = jugadorLink;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(jugadorName);
        dest.writeString(jugadorId);
        dest.writeString(jugadorDescription);
        dest.writeString(jugadorPrice);
        dest.writeString(bestPosicion);
        dest.writeString(jugadorImg);
        dest.writeString(jugadorLink);
    }
}
