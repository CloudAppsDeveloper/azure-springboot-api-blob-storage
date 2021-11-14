package com.esms.ms;

import com.azure.core.util.BinaryData;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.specialized.BlockBlobClient;
import com.azure.storage.common.StorageSharedKeyCredential;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
@Configuration

public class StorageUtil {

    @Value("${azure.storage.account-name}")
    private String accountName;

    @Value("${azure.storage.account-key}")
    private String accountKey;

    @Value("${azure.storage.blob-endpoint}")
    private String url;

    @Value("${azure.storage.container-name}")
    private String container;


    @Bean
    public BlobServiceClient getBlobServiceClient() {

    StorageSharedKeyCredential credential = new StorageSharedKeyCredential(accountName, accountKey);
    String endpoint = String.format(Locale.ROOT, url, accountName);
    BlobServiceClient storageClient = new BlobServiceClientBuilder().endpoint(endpoint).credential(credential).buildClient();
    return storageClient;
    }

    @Bean
    public BlobContainerClient getBlobContainerClient(){

        BlobContainerClient blobContainerClient = getBlobServiceClient().getBlobContainerClient(container);

        return blobContainerClient;

    }
    public String createBlobContainer (String blobContainerName) {

        BlobContainerClient blobContainerClient = getBlobServiceClient().createBlobContainer(blobContainerName);

        return  blobContainerClient.getBlobContainerUrl();
    }

    public String uploadBlob(MultipartFile file) throws IOException {

        BlockBlobClient blobClient = getBlobContainerClient().getBlobClient(file.getOriginalFilename()).getBlockBlobClient();

        File fileData=convertMultiPartToFile(file );
        InputStream dataStream = new ByteArrayInputStream(file.getBytes());

        /*
         * Create the blob with string (plain text) content.
         */
        blobClient.upload(dataStream, file.getSize());

        dataStream.close();
        String response=url+container+"/"+file.getOriginalFilename();
        return response;
    }

    public String downloadFile(String filename) throws IOException {

        BlockBlobClient blobClient = getBlobContainerClient().getBlobClient(filename).getBlockBlobClient();

        int dataSize = (int) blobClient.getProperties().getBlobSize();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(dataSize);
        blobClient.downloadStream(outputStream);
        BinaryData fileData=blobClient.downloadContent();
        String data=fileData.toString();
        outputStream.close();

        String response=url+container+"/"+filename;
        System.out.println("response:"+response);
        return data;
    }

    public void getAllBlobs(String blobContainerName){
        BlobContainerClient blobContainerClient = getBlobServiceClient().getBlobContainerClient(container);
        blobContainerClient.listBlobs()
                .forEach(blobItem -> System.out.println("Blob name: " + blobItem.getName() + ", Snapshot: " + blobItem.getSnapshot()));
    }

    public File convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        return convFile;
    }
}
