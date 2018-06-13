package weceipt.ece150.com.weceipt;

import java.io.Serializable;
import java.util.ArrayList;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class PostResponsePojo {
    private double total;
    private double tax;
    private ArrayList<ReceiptItem> items;
}
