package com.example.server;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ParseRequest {
  private String image;
  private String email;
}
