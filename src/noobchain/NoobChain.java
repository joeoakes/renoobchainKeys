package noobchain;
import java.util.ArrayList;
import com.google.gson.Gson;

public class NoobChain {

    //arraylist for storing created blocks in the blockchain
    public static ArrayList<Block> blockchain = new ArrayList<Block>();
    public static int difficulty = 5;

    public static void main(String[] args) {
        final String secretKey = "this is a password";
        //add our blocks to the blockchain ArrayList:

        System.out.println("Trying to Mine block 1... ");
        //call the addBlock method and pass a new block object with an AES encrypted POJO as the data for the block
        //the previous hash value passed into the new block object is zero because this is the genesis block

        BankingRecord bankRecord = new BankingRecord();
        bankRecord.setAccountNumber("1234");
        bankRecord.setAccountType("Savings");
        bankRecord.setTransactionType("Deposit");
        bankRecord.setTransactionAmount("100");
        bankRecord.setBalance("200");

        Gson gson = new Gson();
        String jsonBank = gson.toJson(bankRecord);

        String encryptedBankRecord = AES.encrypt(jsonBank, secretKey) ;

        addBlock(new Block(encryptedBankRecord, "0"));

        System.out.println("Trying to Mine block 2... ");
        //call the addBlock method and pass a new block object with an AES encrypted POJO as the data for the block
        //get the hash of the previous block in the arraylist and pass it into the block constructor

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setDoctorName("Dr. Joseph Oakes");
        medicalRecord.setPatientID("54469");
        medicalRecord.setPatientName("Joseph Sliwka");
        medicalRecord.setProcedureCode("582");
        medicalRecord.setVisitDate("2/21/2019");

        String jsonMedical = gson.toJson(medicalRecord);

        String encryptedMedicalRecord = AES.encrypt(jsonMedical, secretKey);

        addBlock(new Block(encryptedMedicalRecord,blockchain.get(blockchain.size()-1).hash));

        System.out.println("Trying to Mine block 3... ");
        //call the addBlock method and pass a new block object with an AES encrypted POJO as the data for the block
        //get the hash of the previous block in the arraylist and pass it into the block constructor

        CreditCard creditCardTransaction = new CreditCard();
        creditCardTransaction.setBusinessName("Penn State University - Abington");
        creditCardTransaction.setCardholderName("Joseph Sliwka");
        creditCardTransaction.setDate("2/21/2019");
        creditCardTransaction.setStatus("Pending");
        creditCardTransaction.setTransactionType("PURCHASE");

        String jsonCreditCardTransaction = gson.toJson(creditCardTransaction);

        String jsonCreditCardTransactionEncrypted = AES.encrypt(jsonCreditCardTransaction, secretKey);

        addBlock(new Block(jsonCreditCardTransactionEncrypted,blockchain.get(blockchain.size()-1).hash));

        System.out.println("Trying to Mine block 4... ");
        //call the addBlock method and pass a new block object with an AES encrypted POJO as the data for the block
        //get the hash of the previous block in the arraylist and pass it into the block constructor
        Student student1 = new Student();
        student1.setStudentID("981493786");
        student1.setStudentName("Joseph Sliwka");
        student1.setStudentMajor("IST");

        String jsonStudent = gson.toJson(student1);

        String jsonStudentEncrypted = AES.encrypt(jsonStudent, secretKey);

        addBlock(new Block(jsonStudentEncrypted,blockchain.get(blockchain.size()-1).hash));


        //call the isChainValid() method to check the validity of the block hashes
        System.out.println("\nBlockchain is Valid: " + isChainValid());

        String blockchainJson = StringUtil.getJson(blockchain);
        System.out.println("\nThe block chain: ");
        System.out.println(blockchainJson);

        for(int i = 0; i < blockchain.size(); i++){
            System.out.println("\nDecrypted block data for block #" + (i+1) + ": " + AES.decrypt(blockchain.get(i).getData(), secretKey));
        }
    }

    //method for checking the integriy of the blockchain
    public static Boolean isChainValid() {
        //initialization of local variables for the method
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        //loop through the entire blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            //set the local variables to reference the current block in the chain and the previous block in the chain
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //calculate the hash of the current block again and compare it to the current block's hash that was calculated before. If the hash changed in the time
            // since the block was initially created, the entire chain is invalid and return false
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //calculate the hash of the previous block again and compare it to the previous block's hash that was calculated before. If the hash changed in the time
            // since the block was initially created, the entire chain is invalid and return false
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
            //If the current block hasn't been processed through the mining method, assume the chain is invalid and return false.
            if(!currentBlock.hash.substring( 0, difficulty).equals(hashTarget)) {
                System.out.println("This block hasn't been mined");
                return false;
            }

        }
        //If everything looks ok, return true
        return true;
    }

    //method for adding a new block into the blockchain, accepts a Block object as a parameter
    public static void addBlock(Block newBlock) {
        //make the computer do work by mining the passed block before adding the block to the blockchain arraylist.
        // Pass in the difficulty of mining the block that was defined above.
        //This makes it harder or easier for the computer to mine the block.
        newBlock.mineBlock(difficulty);
        blockchain.add(newBlock);
    }
}