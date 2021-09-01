package sg.edu.rp.c346.id19047433.a19047433_c302_magic;

import java.io.Serializable;

public class CardItem implements Serializable {
    private String cardId;
    private String colourId;
    private String typeId;
    private String cardName;
    private double price;
    private int quantity;

    public CardItem(String cardId, String colourId, String typeId, String cardName, double price, int quantity) {
        this.cardId = cardId;
        this.colourId = colourId;
        this.typeId = typeId;
        this.cardName = cardName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getColourId() {
        return colourId;
    }

    public void setColourId(String colourId) {
        this.colourId = colourId;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String colourId) {
        this.typeId = typeId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return cardName+"     "+String.format("%.2f", price)+"     "+quantity+" copies";
    }
}
