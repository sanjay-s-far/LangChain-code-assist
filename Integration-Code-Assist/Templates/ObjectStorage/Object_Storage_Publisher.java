import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

import io.grpc.netty.shaded.io.netty.handler.codec.http.HttpResponseStatus;
import lombok.extern.slf4j.Slf4j;


/**
 * The Class StoragePublisherService.
 */
@Component
@Slf4j
public class StoragePublisherService {

	
	@Value("${shipment.create.audit.topic}")
	private String shipmentAuditTopic;

	/** The credentials provider. */
	@Autowired
	CredentialsProvider credentialsProvider;

	@Autowired
    Environment env;
	
	/**
	 * Upload object.
	 *
	 * @param content the content
	 * @param blobName the blob name
	 */
	public void uploadObject(String content, String blobName,int warehouseId) {

	
		Storage storage = null;
		try {
			storage = StorageOptions.newBuilder().setProjectId(projectId).setCredentials(credentialsProvider.getCredentials()).build()
					.getService();
		} catch (IOException e) {
			log.info("Unexpected error with credentials occured: ", e);
			throw new RuntimeException(e);
		}
		BlobId blobId = BlobId.of(bucketName, blobName);
		BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("text/xml").build();

		
		log.info("publishing XML {} in to Storage Bucket Started  ",content);
		
		try {
			log.info("publishing Bucket with Blob {} is Started  ",blobName);
			storage.create(blobInfo, content.getBytes());
			log.info("publishing Bucket with Blob {} is Completed  ",blobName);
			
				
		} catch (StorageException e) {
			if (e.getCode() != HttpResponseStatus.FORBIDDEN.code() && !e.getMessage().contains("delete access"))
				throw e;
			else
				
			log.info("The file {} already exists.  ",blobName);
		}
	}
}
