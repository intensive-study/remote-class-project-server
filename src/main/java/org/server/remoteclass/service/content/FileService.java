package org.server.remoteclass.service.content;

import org.server.remoteclass.entity.Content;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileService {
    Content upload(MultipartFile file, String dirName) throws IOException;
    String getFileUrl(String fileName);
}
