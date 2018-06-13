package weceipt.ece150.com.weceipt;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ReceiptItem implements Serializable {
    private String description;
    private String price;
}
