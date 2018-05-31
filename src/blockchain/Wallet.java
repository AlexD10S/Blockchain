package blockchain;

import tests.BlockChain;

import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;
    public HashMap<String,TransactionOutput> UTXOs = new HashMap<String,TransactionOutput>();

    public Wallet(){
        generateKeyPair();
    }

    public void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            keyGen.initialize(ecSpec, random);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * All the wallets have to show the balance and send funds.
     */
    public float getBalance() {
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item: BlockChain.UTXOs.entrySet()){
            TransactionOutput UTXO = item.getValue();
            if(UTXO.isMine(publicKey)) { //if output belongs to me ( if coins belong to me )
                UTXOs.put(UTXO.getId(),UTXO); //add it to our list of unspent transactions.
                total += UTXO.getValue() ;
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey _recipient,float value ) {

        if(getBalance() < value) {
            System.out.println("#Not Enough funds to send transaction. Transaction Discarded.");
            return null;
        }
        else {
            //create array list of inputs
            ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

            float total = 0;
            for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
                TransactionOutput UTXO = item.getValue();
                total += UTXO.getValue();
                inputs.add(new TransactionInput(UTXO.getId()));
                if (total > value) break;
            }

            Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
            newTransaction.generateSignature(privateKey);

            for (TransactionInput input : inputs) {
                UTXOs.remove(input.getTransactionOutputId());
            }
            return newTransaction;
        }
    }



    /*
     * Getters/Setters
     */

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
