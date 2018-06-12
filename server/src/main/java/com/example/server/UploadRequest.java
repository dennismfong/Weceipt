package com.example.server;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class UploadRequest {
  private String image;
  private String email;
}
