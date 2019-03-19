package auctionhouse;

public class Lot {
    int number;
    String sellerName;
    Money reservePrice;
    String description;
    LotStatus status;
    Money soldPrice;

    public Lot(int number, String sellerName, String description,Money reserveprice){
        this.number = number;
        this.sellerName = sellerName;
        this.description = description;
        this.reservePrice = reserveprice;
        this.status = LotStatus.UNSOLD;
    }

}
