package com.example.server;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Controller
public class Handler {
  private S3Wrapper s3Wrapper = new S3Wrapper();
  private VisionApiWrapper visionApiWrapper = new VisionApiWrapper();


  @RequestMapping("/")
	@ResponseBody
	String home() {
		return "Hello World!";
	}

	// Still need OAUTH
  @RequestMapping(value="/presigned_url", method= RequestMethod.GET)
  @ResponseBody
  public String gen_s3_url() {
      /**
       * Limit ~10 requests per user per hour.
       * If user requests a new link, delete old url and generate new one? What about just return the one already generated.
       * Store state information in database? i.e. how many tokens, which token corresponds to which user.
       * OAUTH authentication. Needs to have an account to request.
       */
      boolean verified = true;
      if (!verified)
          return "Oauth failed";

      // AWS Cred. stored in Heroku config variables. For local testing, Heroku recommends adding env. variables to bashrc.
      //String bucketName = System.getenv("S3_BUCKET");
      //String objectKey = System.getenv("S3_OBJ_KEY");

      String clientRegion = "us-west-1";
      String bucketName = "weceiptuploads";
      UUID uuid = UUID.randomUUID();
      String objectKey = uuid.toString();

      try {
          AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                  .withRegion(clientRegion)
                  .withCredentials(new EnvironmentVariableCredentialsProvider())
                  .build();

          // Set the presigned URL to expire after one hour.
          java.util.Date expiration = new java.util.Date();
          long expTimeMillis = expiration.getTime();
          expTimeMillis += 1000 * 60 * 60;
          expiration.setTime(expTimeMillis);

          // Generate the presigned URL.
          System.out.println("Generating pre-signed URL.");
          // make sure withmethod = post
          GeneratePresignedUrlRequest generatePresignedUrlRequest =
                  new GeneratePresignedUrlRequest(bucketName, objectKey)
                          .withMethod(HttpMethod.PUT)
                          .withExpiration(expiration);
          URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
          return url.toString();
      }
      catch(AmazonServiceException e) {
          // The call was transmitted successfully, but Amazon S3 couldn't process
          // it, so it returned an error response.
          e.printStackTrace();
          return "Amazon S3 threw an error.";
      }
      catch(SdkClientException e) {
          // Amazon S3 couldn't be contacted for a response, or the client
          // couldn't parse the response from Amazon S3.
          e.printStackTrace();
          return "Amazon S3 timed out.";
      }
  };

  @RequestMapping(value="/save_image", method=RequestMethod.GET)
  @ResponseBody
  public String save_image() {
      /**
       * User present token.
       * We verify token is valid, i.e. it has presigned url associated with it.
       * Attempt to grab file from pre-signed url.
       * if success return success.
       * else return failed
       */

      boolean verified = true;
      if (!verified)
          return "fml";
      return "Grabbing image";
  }

  @RequestMapping(value="scan_image", method=RequestMethod.GET)
  @ResponseBody
  public String scan_image() {
      return "scanned.\n";
  }

  @RequestMapping(value="/upload", method=RequestMethod.POST, consumes="application/json")
  @ResponseBody
  public UploadResponse uploadImage(@RequestBody UploadRequest req) {
    String s3FilePath = s3Wrapper.uploadToS3(req.getImage());
    //String s3FilePath = "https://s3-us-west-1.amazonaws.com/weceiptuploads/close.jpg";
    List<ReceiptItem> items = visionApiWrapper.getReceiptItems(s3FilePath);
    double total = visionApiWrapper.getTotalAmount(s3FilePath);
    double tax = visionApiWrapper.getTaxAmount(s3FilePath);


    return UploadResponse.builder()
            .items(items)
            .total(total)
            .tax(tax)
            .build();
  }
}
