package com.esms.ms;

import net.bytebuddy.implementation.bind.MethodDelegationBinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.WritableResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.ws.Response;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

@RestController
@RequestMapping("/storage")
public class BlobStorageController {

  //@Value("azure-blob://students/")
  //private Resource blobFile;
  @Autowired
  ResourceLoader resourceLoader;

    @Autowired
    StorageUtil storageUtil;

    @GetMapping("/readBlobFile")
    public String readBlobFile(@RequestParam String fileName) throws IOException {
        System.out.println("filename:"+fileName);
        String filepath="azure-blob://students/"+fileName;
        Resource file= resourceLoader.getResource(filepath);
        String fileLocation=StreamUtils.copyToString(
                file.getInputStream(),
                Charset.defaultCharset());

        //System.out.println("file location uri :"+file.getURI());
        System.out.println("file location url :"+file.getURL());

        return file.getURL().toString();
    }
   /*@PostMapping("/writeBlobFile")
   public String writeBlobFile(@RequestBody String data) throws IOException {
       try (OutputStream os = ((WritableResource) this.blobFile).getOutputStream()) {
           os.write(data.getBytes());
       }
       return "file was updated";
   }
*/
    @PostMapping(path="/uploadBlobFile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadBlobFile(@RequestParam MultipartFile file) throws IOException {
       //File newFile =storageUtil.convertMultiPartToFile(file);
        String fileName=file.getOriginalFilename();

        String filepath="azure-blob://students/"+fileName;
        Resource resource= resourceLoader.getResource(filepath);
        try (OutputStream os = ((WritableResource) resource).getOutputStream()) {

            System.out.println("file name:"+file.getOriginalFilename());
            os.write(file.getBytes());
        }

        System.out.println("file location url :"+resource.getURL());
        return ResponseEntity.ok().body(resource.getURL().toString());
    }



    @GetMapping("/creatBlobContainer")
    public String createBlobContainer(@RequestParam String containerName){
        String name=storageUtil.createBlobContainer(containerName);
        System.out.println("container name:"+name);
        return name;
    }

    @PostMapping(path="/uploadFile",consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> uploadFile(@RequestParam MultipartFile file) throws IOException {
        String responseUrl=storageUtil.uploadBlob(file);

        System.out.println("file location url :"+responseUrl);
        return ResponseEntity.ok().body(responseUrl);
    }

    @GetMapping("/downloadFile")
    public ResponseEntity<String> downloadFile(@RequestParam String fileName) throws IOException {
        String responseUrl=storageUtil.downloadFile(fileName);

        //System.out.println("file location url :"+responseUrl);
        return ResponseEntity.ok().body(responseUrl);
    }

    @GetMapping("/blobList")
    public String blobList(@RequestParam String containerName){
        storageUtil.getAllBlobs(containerName);
        System.out.println("container name:"+containerName);
        return "OK";
    }
}
