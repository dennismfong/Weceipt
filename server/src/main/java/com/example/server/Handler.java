package com.example.server;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URL;

@Controller
public class Handler {
    //String clientRegion = "us-west-1";

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
        String bucketName = "weceiptimages";
        String objectKey = "test";

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
            return "Pre-Signed URL: " + url.toString();
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
}
