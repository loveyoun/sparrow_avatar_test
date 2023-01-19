package Contoller;

import Service.S3UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
public class UploadController {

    private final S3UploadService s3UploadService;

    @PostMapping("upload")
    private ResponseEntity<String> uploadUserImage(@RequestParam("image") MultipartFile multipartFile) {
        try{
            s3UploadService.uploadFiles(multipartFile, "static");
        } catch(Exception e) { return new ResponseEntity(HttpStatus.BAD_REQUEST); }

        return new ResponseEntity(HttpStatus.NO_CONTENT);

        //return ResponseEntity.ok(s3UploadService.uploadFiles(multipartFile));
    }

}
