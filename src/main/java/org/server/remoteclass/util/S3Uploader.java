package org.server.remoteclass.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.server.remoteclass.entity.Content;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    private final EntityManager entityManager;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String getFileUrl(String fileName) {
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    @Transactional
    public Content upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = covertFile(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("File로 변환하는데 실패했습니다."));

        String fileName = dirName + "/" + uploadFile.getName();
        String uri = uploadS3(uploadFile, fileName);
        Content content = createContentEntity(uri, fileName, multipartFile.getOriginalFilename(), multipartFile.getContentType());
        entityManager.persist(content);
        return content;
    }

    private String uploadS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        removeNewFile(uploadFile);
        return getFileUrl(fileName);
    }

    private Optional<File> covertFile(MultipartFile multipartFile) throws IOException{
        if(multipartFile.getOriginalFilename() == null) return Optional.empty();
        File file = new File(createUniqueFileName(multipartFile.getOriginalFilename()));
        if (file.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(multipartFile.getBytes());
            }
            return Optional.of(file);
        }
        return Optional.empty();
    }

    private String createUniqueFileName(String originalFileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(originalFileName));
    }

    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException(String.format("잘못된 파일 형식입니다.(%s)", fileName));
        }
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Content createContentEntity(String originalFileName, String fileName, String fileUri, @Nullable String contentType) {
        return new Content(originalFileName, fileName, fileUri, contentType);
    }
}


