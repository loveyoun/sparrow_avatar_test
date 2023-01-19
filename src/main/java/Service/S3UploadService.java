package Service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3UploadService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;


    public String uploadFiles(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File failed"));

        return upload(uploadFile, dirName);
    }

    //업로드 로직
    public String upload(File uploadFile, String filePath){
        String fileName = filePath + "/" + UUID.randomUUID() + uploadFile.getName();
        String uploadImageUrl = putS3(uploadFile, fileName);

        return uploadImageUrl;
    }

    //S3 업로드 코드
    private String putS3(File uploadFile, String fileName){
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

        return amazonS3Client.getUrl(bucket, fileName).toString();
    }


    private Optional<File> convert(MultipartFile file) throws IOException{
        File convertFile = new File(System.getProperty("user.dir") + "/" + file.getOriginalFilename());

        if(convertFile.createNewFile()){
            try(FileOutputStream fos = new FileOutputStream(convertFile)){
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }


//    public String upload(MultipartFile multipartFile) throws IOException{
//
//        String s3filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();
//
//        //메타데이터 커스터마이징. 파일의 사이즈를 S3에 알려줌.
//        ObjectMetadata objMeta = new ObjectMetadata();
//        objMeta.setContentLength(multipartFile.getInputStream().available());
//
//        //S3 API method.
//        amazonS3Client.putObject(new PutObjectRequest(bucket, s3filename, multipartFile.getInputStream(), objMeta));
//
//        return amazonS3Client.getUrl(bucket, s3filename).toString();
//    }

}
