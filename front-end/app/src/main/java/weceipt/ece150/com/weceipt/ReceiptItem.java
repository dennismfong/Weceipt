package weceipt.ece150.com.weceipt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ReceiptItem {
    private String description;
    private String price;
}
