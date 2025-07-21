import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.gax.core.CredentialsProvider;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import com.google.cloud.storage.StorageOptions;

/**
 * The Class StoragePublisherService.
 */
@Component
public class StorageReadService {

	/** The Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(StorageReadService.class);

	

	@Value("${warehouse-number}")
	private String warehouse;
	/** The credentials provider. */
	@Autowired
	CredentialsProvider credentialsProvider;
	
	@Autowired
	Environment env;

	public String getObject(String blobName) throws UnsupportedEncodingException {

		String bucketName = env.getProperty("bucket-name.".concat(warehouse));

		
		Storage storage = null;
		String value = "";
		try {
			storage = StorageOptions.newBuilder().setCredentials(credentialsProvider.getCredentials()).build()
					.getService();
		} catch (IOException e) {
			logger.error("INFO:Unexpected error with credentials occured: ", e);
			throw new RuntimeException(e);
		}

		BlobId blobId = BlobId.of(bucketName,blobName);

		try {

			Blob blob = storage.get(blobId);
			value = new String(blob.getContent());

				logger.info("INFO:File Content for the file - " + blobName + "   " + value.replaceAll("\n\t*|\t", " "));

		} catch (StorageException e) {
			logger.error("INFO:Unexpected error: ", e);
			logger.warn("INFO:The file {} already exists.", blobName);
		}

		return value;
	}
	
	public boolean deleteObject(String blobName) throws UnsupportedEncodingException {
		
		String bucketName = env.getProperty("bucket-name.".concat(warehouse));

		boolean deleted=false;
		Storage storage = null;
		String value = "";
		try {
			storage = StorageOptions.newBuilder().setCredentials(credentialsProvider.getCredentials()).build()
					.getService();
		} catch (IOException e) {
			logger.error("INFO:Unexpected error with credentials occured: ", e);
			throw new RuntimeException(e);
		}

		BlobId blobId = BlobId.of(bucketName,blobName);

		try {
			
			logger.info("INFO:File {}  is being removed from bucket {}  " , blobName ,bucketName);

			/*
			 * Blob blob = storage.get(blobId); value = new String(blob.getContent());
			 */
			
			deleted=storage.delete(blobId);
			
			logger.info("INFO:File {}  is being removed from bucket {}  " , blobName ,bucketName);

			return deleted;
		} catch (StorageException e) {
			logger.error("INFO:Unexpected error: ", e);
			logger.warn("INFO:The file {} already exists.", blobName);
		}

		return deleted;
	}

}
