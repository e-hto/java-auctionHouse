/**
 * 
 */
package auctionhouse;

import java.util.*;
import java.util.logging.Logger;

/**
 * @author pbj
 *
 */
public class AuctionHouseImp implements AuctionHouse {

    private static Logger logger = Logger.getLogger("auctionhouse");
    private static final String LS = System.lineSeparator();


    /// https://stackoverflow.com/questions/18329311/reason-for-list-list-new-arraylist
    /// oop principle shit
    private HashMap<String,Seller> sellerList  = new HashMap<String,Seller>();
    private HashMap<String,Buyer> buyerList  = new HashMap<String,Buyer>();
    private HashMap<Integer,Lot> lotList  = new HashMap<Integer, Lot>();
    private HashMap<String,Auctioneer> auctioneerList = new HashMap<String,Auctioneer>();
    private HashMap<Integer, HashSet<String>> interestList = new HashMap<Integer, HashSet<String>>();
    private HashMap<Integer,String> lotAuctioneerList = new HashMap<Integer,String>();


    // hashmap auctioneer name / address

    // hashmap lots / bid / bidder

    private String startBanner(String messageName) {
        return  LS 
          + "-------------------------------------------------------------" + LS
          + "MESSAGE IN: " + messageName + LS
          + "-------------------------------------------------------------";
    }

    Parameters parameters;

    public AuctionHouseImp(Parameters parameters) {
        this.parameters = parameters;
    }

    public Status registerBuyer(
            String name,
            String address,
            String bankAccount,
            String bankAuthCode) {

        logger.fine(startBanner("registerBuyer " + name));

        logger.info("Check for duplicate name.");

        for (HashMap.Entry<String, Buyer> entry : buyerList.entrySet()) {
            String entryName = entry.getKey();
            boolean duplicateName = entryName == name;
            if (duplicateName) {
                logger.info("A Buyer with the name of " + name + " already registerd.");
                return Status.error("Could not proceed: A buyer already registerd with this name.");
            }
        }

        logger.info("No duplicate found, registration of the Buyer.");

        Buyer createdBuyer = new Buyer(name,address,bankAccount,bankAuthCode);

        buyerList.put(name,createdBuyer);

        logger.fine(name + " registration successful.");
        return Status.OK();
    }

    public Status registerSeller(
            String name,
            String address,
            String bankAccount) {

        logger.fine(startBanner("registerSeller " + name));

        logger.info("Check for duplicate name.");

        for (HashMap.Entry<String, Seller> entry : sellerList.entrySet()) {
            String entryName = entry.getKey();
            boolean duplicateName = entryName == name;
            if (duplicateName) {
                logger.fine("Registration unsuccessful: A Seller with the name of " + name + " already registerd.");
                return Status.error("Could not proceed: A seller already registerd with this name.");

            }
        }

        logger.info("No duplicate found, registration of the Seller.");

        Seller createdSeller = new Seller(name,address,bankAccount);

        sellerList.put(name,createdSeller);

        logger.fine(name + " registration successful.");
        return Status.OK();      
    }

    public Status addLot(
            String sellerName,
            int number,
            String description,
            Money reservePrice) {
        logger.fine(startBanner("addLot " + sellerName + " " + number));

        logger.info("Check if the Seller exists.");

        if(!sellerList.containsKey(sellerName)){
            logger.fine("A seller with name: " + sellerName + " is not registered.");
            return Status.error("No seller with name:" + sellerName + "registered.");
        }

        logger.info("A Seller with name: " + sellerName + " exists.");

        logger.info("Check for duplicate number.");

        for (HashMap.Entry<Integer, Lot> entry : lotList.entrySet()) {
            int entryName = entry.getKey();

            boolean duplicateName = entryName == number;

            if (duplicateName) {
                logger.fine("Lot unsuccessfully added.");
                return Status.error("Could not proceed: A lot already was already added with this number.");

            }
        }

        logger.info("The number was never used, proceeding to add the lot.");

        Lot newLot = new Lot(number,sellerName,description,reservePrice);

        lotList.put(number,newLot);

        logger.fine("Lot successfully added.");
        return Status.OK();    
    }

    public List<CatalogueEntry> viewCatalogue() {
        logger.fine(startBanner("viewCatalog"));

        logger.info("Creating an empty list of catalogue entries.");
        List<CatalogueEntry> catalogue = new ArrayList<CatalogueEntry>();

        logger.info("Starting the process of adding creating and adding each catalogue entry.");

        Iterator<HashMap.Entry<Integer, Lot>> entrySet = lotList.entrySet().iterator();


        while (entrySet.hasNext()) {

            HashMap.Entry<Integer, Lot> entry = entrySet.next();

            int number = entry.getKey();
            String description = entry.getValue().description;
            LotStatus status = entry.getValue().status;

            CatalogueEntry lotEntry = new CatalogueEntry(number, description, status);

            catalogue.add(lotEntry);

            logger.info("Lot number: " + number + " with description: " + description
                    + " and with current status: " + status + " was added to the catalogue.");




        }
        logger.info("Process of adding creating and adding each catalogue entry finished.");


        logger.fine("Catalogue: " + catalogue.toString());
        return catalogue;
    }

    public Status noteInterest(
            String buyerName,
            int lotNumber) {
        logger.fine(startBanner("noteInterest " + buyerName + " " + lotNumber));

        logger.info("Checking if the lot exists.");

        if(!lotList.containsKey(lotNumber)){
            logger.fine("The lot does not exist.");
            return Status.error("The lot does not exist.");
        }

        logger.info("The lot exists,");

        logger.info("Checking if the lot is sold.");

        if(lotList.get(lotNumber).status== LotStatus.SOLD|lotList.get(lotNumber).status
                ==LotStatus.SOLD_PENDING_PAYMENT){
            logger.fine("Cannot note interest in a sold lot.");

            return Status.error("The lot is already sold");
        }
        logger.info("The lot is not sold.");

        logger.info("Checking if the buyer is the first to note interest.");

        HashSet intBidSet = interestList.get(lotNumber);

        if(intBidSet == null){
            logger.info("The buyer is the first to note interest, a list of interested buyers is created.");
            intBidSet = new HashSet<String>();
            interestList.put(lotNumber,intBidSet);
        }

        logger.info("The lot already has interested buyers.");

        logger.info("Checking if the buyer has already noted interest.");

        if(intBidSet.contains(buyerName)){

            logger.fine("The buyer already noted interest.");
            return Status.error("Interest already noted in the lot.");
        }

        logger.fine("The buyer did not note interest in the lot yet, he is included in the list.");

        intBidSet.add(buyerName);

        return Status.OK();
    }

    public Status openAuction(
            String auctioneerName,
            String auctioneerAddress,
            int lotNumber) {
        logger.fine(startBanner("openAuction " + auctioneerName + " " + lotNumber));

        logger.info("Checking if the lot exists.");

        if(!lotList.containsKey(lotNumber)){
            logger.info("The lot does not exist.");
            return Status.error("There is not such lot.");
        }

        logger.info("The lot exists.");

        logger.info("Checking of the lot has interest buyers.");

        if(!interestList.containsKey(lotNumber)){
            logger.fine("No buyers interested found.");
            return Status.error("The lot has no interested buyer.");
        }

        logger.info("The lot has interest buyers.");

        logger.info("Checking if the lot is not in auction or sold.");

        if(lotList.get(lotNumber).status!=LotStatus.UNSOLD){
            logger.fine("The lot is either in auction or sold and thus cannot be auctioned..");
            return Status.error("The lot can't be put in auction.");
        }

        logger.info("The lost is unsold.");

        logger.info("Gathering information.");

        MessagingService messg = parameters.messagingService;
        String sellerName = lotList.get(lotNumber).sellerName;
        String sellerAddress = sellerList.get(sellerName).getAddress();

        messg.auctionOpened(sellerAddress,lotNumber);
        logger.info("Seller " + sellerName + " of the lot informed of the auction opening.");

        Iterator<String> intBidEntries = interestList.get(lotNumber).iterator();

        if(intBidEntries!=null){
            while(intBidEntries.hasNext()){
                String currBuyerName = intBidEntries.next();
                String currBuyerAddress = buyerList.get(currBuyerName).getAddress();
                messg.auctionOpened(currBuyerAddress,lotNumber);
                logger.info("Interested buyer " + currBuyerName + " informed of the auction opening.");

            }
        }


        lotList.get(lotNumber).status = LotStatus.IN_AUCTION;

        logger.info("Status of the lot changed to: " + LotStatus.IN_AUCTION.toString());



        Auctioneer lotAuctioneer = new Auctioneer(auctioneerName,auctioneerAddress);
        auctioneerList.put(auctioneerName,lotAuctioneer);

        logger.info("Auctioneer information stored.");

        lotAuctioneerList.put(lotNumber,auctioneerName);

        logger.info("Link between a lot and the auctioneer keeping its auction stored.");

        return Status.OK();
    }

    public Status makeBid(
            String buyerName,
            int lotNumber,
            Money bid) {
        logger.fine(startBanner("makeBid " + buyerName + " " + lotNumber + " " + bid));


        logger.info("Checking if the lot exists.");
        if(!lotList.containsKey(lotNumber)){
            logger.info("Lot: " + lotNumber + " not found.");
            return Status.error("There is no lot registered with this number.");
        }
        logger.info("The lot exists.");

        logger.info("Checking if buyer: " + buyerName + " noted interest in the lot.");

        if(!interestList.get(lotNumber).contains(buyerName)){
            logger.info("The buyer did not note interest in the lot before bidding.");
            return Status.error("Cannot bid on a lot without noting interest first.");
        }

        logger.info("The buyer noted interest.");

        logger.info("Checking if the lot is in auction.");

        if(lotList.get(lotNumber).status!=LotStatus.IN_AUCTION){
            return Status.error("The lot has to be in auction to bid.");
        }

        logger.fine("The lot is in auction.");

        logger.info("Gathering information to determine if a bid has been made already.");
        String auctioneerName = lotAuctioneerList.get(lotNumber);

        Auctioneer auctioneer = auctioneerList.get(auctioneerName);

        Boolean firstBidder = auctioneer.getHightestBidder()==null;

        Boolean sufficientBid = true;

        if(!firstBidder){

            logger.info("Gathering information on the highest bid and its author.");

            Money highestBid = auctioneer.getHeldBid();

            Money increment = parameters.increment;

            Money minimumBid = highestBid.add(increment);

            sufficientBid = !bid.lessEqual(minimumBid);

            logger.info("As buyer: " + buyerName + " is not the first, its bid neet to be sufficient.");

            if(!sufficientBid){
                logger.fine("The bid: " + bid + " is inferior to the current highest bid and added increment: " + minimumBid);
                return Status.error("The bid amount is not sufficient.");
            }

        }
        else{
            logger.info(buyerName + " is the first to bid.");
        }

        logger.info(buyerName + "'s bid is accepted.");

        logger.info("Creation of the list of addresses to message.");

        HashSet<String> messAddresses = new HashSet<String>();

        Iterator<String> buyerNameEntry = interestList.get(lotNumber).iterator();
        while (buyerNameEntry.hasNext()) {
            //get address from name
            String buyerAddress = buyerList.get(buyerNameEntry.next()).getAddress();
            messAddresses.add(buyerAddress);
            logger.info(buyerNameEntry + "'s address has been added to the list.");
        }




        String sellerName = lotList.get(lotNumber).sellerName;
        String sellerAddress = sellerList.get(sellerName).getAddress();
        String auctioneerAddress = auctioneer.getAddress();


        String addressOfBidder= buyerList.get(buyerName).getAddress();
        messAddresses.remove(addressOfBidder);

        messAddresses.add(sellerAddress);
        messAddresses.add(auctioneerAddress);

        logger.info("Seller and auctioneer address added to the list, bidder address removed.");

        Iterator<String> addressEntry = messAddresses.iterator();

        if(firstBidder||sufficientBid){
            auctioneer.setHeldBid(bid);
            auctioneer.setHightestBidder(buyerName);

            //messaging

            while(addressEntry.hasNext()) {
                parameters.messagingService.bidAccepted(addressEntry.next(), lotNumber, bid);
                logger.info("Bid amount sent to: " + addressEntry);
            }
        }

        logger.fine(buyerName + " submitted a successful bid of " + bid + " on the lot " + lotNumber);
        return Status.OK();    
    }

    public Status closeAuction(
            String auctioneerName,
            int lotNumber) {
        logger.fine(startBanner("closeAuction " + auctioneerName + " " + lotNumber));

        logger.info("Checking if the auctioneer closing the auction is the one who opened it.");

        if(lotAuctioneerList.get(lotNumber)!=auctioneerName){
            logger.fine("The auctioneer closing the auction is not the one who opened it.");
            return Status.error("Wrong auctioneer closing the auction.");
        }

        logger.info("The auctioneer closing the auction corresponds to the one who opened it.");


        Auctioneer auctioneer = auctioneerList.get(auctioneerName);
        Lot lot = lotList.get(lotNumber);
        Money reservePrice = lot.reservePrice;

        logger.info("Check if a bid was made.");

        if(auctioneer.getHeldBid()==null){
            logger.fine("No bid was made during the auction," +
                    " the auctioneer information is discarded and the lot put back as unsold.");

            lotAuctioneerList.remove(lotNumber);
            auctioneerList.remove(auctioneerName);
            lot.status = LotStatus.UNSOLD;
            Status noSale = new Status(Status.Kind.NO_SALE);
            return noSale;
        }

        logger.info("A highest bid is found, checking if it is superior to the reserve price.");

        Money lastBid = auctioneer.getHeldBid();


        if(!lastBid.lessEqual(reservePrice)){
            lot.soldPrice = auctioneer.getHeldBid();
            lot.status = LotStatus.SOLD_PENDING_PAYMENT;
            logger.info("The bid being superior to the reserve price, the lot will be sold at this price.");
        }
        else if(lastBid.lessEqual(reservePrice)){
            lot.status = LotStatus.UNSOLD;
            Status noSale = new Status(Status.Kind.NO_SALE);
            lotAuctioneerList.remove(lotNumber);
            auctioneerList.remove(auctioneerName);
            logger.fine("The highest bid is inferior to the reserve price," +
                    " the auctioneer information is discarded and the lot put back as unsold.");
            return noSale;
        }

        logger.info("The highest bid is sufficient, the actors will be messaged and the transfer processed.");

        logger.info("Creation of the list of addresses to message.");
        HashSet<String> messAddresses = new HashSet<String>();
        Iterator<String> buyerNameEntry = interestList.get(lotNumber).iterator();
        while (buyerNameEntry.hasNext()) {
            //get address from name
            String buyerAddress = buyerList.get(buyerNameEntry.next()).getAddress();
            messAddresses.add(buyerAddress);
            logger.info(buyerNameEntry + "'s address was added to the list.");

        }
        String sellerName = lotList.get(lotNumber).sellerName;
        String sellerAddress = sellerList.get(sellerName).getAddress();

        messAddresses.add(sellerAddress);

        logger.info("Seller address added to the list.");

        //messaging iterator

        /*
        Iterator<String> addressEntry = messAddresses.iterator();
        while(addressEntry.hasNext()) {
            parameters.messagingService.lotSold(addressEntry.next(),lotNumber);
            logger.info(addressEntry + " received the auction closure information.");
        }
        */

        String buyerName = auctioneer.getHightestBidder();
        String buyerAcc = buyerList.get(buyerName).getBankAcc();
        String buyerAuthCode = buyerList.get(buyerName).getBankAuthCode();
        String houseAcc = parameters.houseBankAccount;
        String houseAuthCode = parameters.houseBankAuthCode;
        String sellerAcc = sellerList.get(sellerName).getBankAcc();

        logger.info("Account and authentification code gathered.");

        lotAuctioneerList.remove(lotNumber);
        auctioneerList.remove(auctioneerName);

        logger.info("Auctioneer information cleared and lot-auctioneer relation erased.");


        Money buyerCollect = lastBid.addPercent(parameters.buyerPremium);
        Money sellerPayment = lastBid.addPercent(-parameters.commission);


        logger.info("Attempting Money transfer.");
        Status fromBuyerTransfer = parameters.bankingService.transfer(buyerAcc,buyerAuthCode,houseAcc,buyerCollect);
        Status toSellerTransfer = parameters.bankingService.transfer(houseAcc,houseAuthCode,sellerAcc,sellerPayment);



        if(fromBuyerTransfer.kind==Status.Kind.OK&&toSellerTransfer.kind==Status.Kind.OK) {
            lot.status = LotStatus.SOLD;
            Status sale = new Status(Status.Kind.SALE);

            logger.fine("Successful transfer from Buyer to Seller through the AuctionHouse.");
            return sale;
        }
        else if (fromBuyerTransfer!=Status.OK()||toSellerTransfer!=Status.OK()){
            lot.status = LotStatus.SOLD_PENDING_PAYMENT;
            Status salePending = new Status(Status.Kind.SALE_PENDING_PAYMENT);

            if(fromBuyerTransfer!=Status.OK()) {
                logger.fine("Unsuccessful transfer from Buyer to Seller.");
            }
            if(fromBuyerTransfer==Status.OK()&&toSellerTransfer!=Status.OK()){
                logger.fine("Unsuccessful transfer from AuctionHouse to Seller.");
            }
            return salePending;
        }

        return Status.OK();

    }
}
