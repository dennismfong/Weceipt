package weceipt.ece150.com.weceipt;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder

public class ReceiptItem implements Serializable {
    public ReceiptItem() {
        this.description = "";
        this.price = 0.0;
    }
    public ReceiptItem(String description, double price) {
        this.description = description;
        this.price = price;
    }
    private String description;
    private double price;

    @Override
    public String toString() {
        String description = this.getDescription().substring(0, this.getDescription().lastIndexOf(" "));
        return description + '\n' + String.valueOf(this.getPrice());
    }
}
