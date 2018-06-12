package com.example.server;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ReceiptItem {
  private String description;
  private double price;
}
