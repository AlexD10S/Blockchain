package blockchain;

import utils.StringUtil;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
    private PublicKey receiver;
    private float value;
    private String parentTransactionId;


    public TransactionOutput(PublicKey reciepient, float value, String parentTransactionId) {
        this.receiver = reciepient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = StringUtil.applySha256(StringUtil.getStringFromKey(reciepient)
                + Float.toString(value)+parentTransactionId);
    }

    public boolean isMine(PublicKey publicKey) {
        return (publicKey == receiver);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public void setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
    }

    public PublicKey getReceiver(){
        return receiver;
    }
}
