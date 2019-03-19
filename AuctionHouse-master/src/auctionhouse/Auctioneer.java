package auctionhouse;

public class Auctioneer {
    private String name;
    private String address;
    private Money heldBid;
    private String hightestBidder;
    private Parameters parameters;
    public Auctioneer(String name,String address) {
        this.name = name;
        this.address = address;
        this.hightestBidder = null;
        this.heldBid = null;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public Money getHeldBid(){
        return heldBid;
    }
    public String getHightestBidder(){
        return hightestBidder;
    }

    public void setHightestBidder(String hightestBidder){
        this.hightestBidder = hightestBidder;
    }

    public void setHeldBid(Money bid){
        this.heldBid = bid;
    }

}
