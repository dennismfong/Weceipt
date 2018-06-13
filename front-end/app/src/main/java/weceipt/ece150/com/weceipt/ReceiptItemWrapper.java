package weceipt.ece150.com.weceipt;

import java.io.Serializable;
import java.util.ArrayList;

public class ReceiptItemWrapper implements Serializable {
    private ArrayList<ReceiptItem> items;

    public ReceiptItemWrapper(ArrayList<ReceiptItem> data) {
        this.items = data;
    }

    public ArrayList<ReceiptItem> getItems() {
        return this.items;
    }
}
