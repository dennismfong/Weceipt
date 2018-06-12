package com.example.server;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder

public class UploadResponse {
  private double total;
  private double tax;
  private List<ReceiptItem> items;
}
