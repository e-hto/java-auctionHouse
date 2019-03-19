package auctionhouse;

public class Seller {

    private String name;
    private String address;
    private String bankAcc;

    public Seller( String name, String address, String bankAcc){
        this.name = name;
        this.address = address;
        this.bankAcc = bankAcc;
    }
    public String getName(){
        return name;
    }
    public String getAddress(){
        return address;
    }
    public String getBankAcc(){
        return bankAcc;
    }
}
