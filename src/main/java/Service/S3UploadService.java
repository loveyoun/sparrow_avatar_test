package Service;

import com.amazonaws.services.s3.AmazonS3;
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

    private final AmazonS3 amazons3;

    public String upload(MultipartFile multipartFile) throws IOException{

        String s3filename = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

        //메타데이터 커스터마이징. 파일의 사이즈를 S3에 알려줌.
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(multipartFile.getInputStream().available());

        //S3 API method.
        amazons3.putObject(new PutObjectRequest(bucket, s3filename, multipartFile.getInputStream(), objMeta));

        return amazons3.getUrl(bucket, s3filename).toString();
    }

//    private Optional<File> convert(MultipartFile multifile) throws IOException{
//        File convertfile = new File(multifile.getOriginalFilename());
//
//        if(convertfile.createNewFile()){
//            try(FileOutputStream fos = new FileOutputStream(convertfile)){
//                fos.write(multifile.getBytes());
//            }
//            return Optional.of(convertfile);
//        }
//
//        return Optional.empty();
//    }

}
