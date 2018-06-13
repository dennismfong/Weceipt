package weceipt.ece150.com.weceipt;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class PostResponsePojo {
    public PostResponsePojo() {
        this.tax = 0.0;
        this.total = 0.0;
        this.items = null;
    }

    public PostResponsePojo(double tax, double total, List items) {
        this.tax = tax;
        this.total = total;
        this.items = items;
    }
    private double total;
    private double tax;
    private List<ReceiptItem> items;
}
