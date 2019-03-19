package auctionhouse;

public class Buyer {

    private String name;
    private String address;
    private String bankAcc;
    private String bankAuthCode;

    public Buyer( String name, String address, String bankAcc, String bankAuthCode){
        this.name = name;
        this.address = address;
        this.bankAcc = bankAcc;
        this.bankAuthCode= bankAuthCode;
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
    public String getBankAuthCode(){
        return bankAuthCode;
    }
}
