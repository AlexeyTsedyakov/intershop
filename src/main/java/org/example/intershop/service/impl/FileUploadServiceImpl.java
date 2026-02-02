package org.example.intershop.service.impl;

import jakarta.annotation.PostConstruct;
import org.example.intershop.exception.UploadFileException;
import org.example.intershop.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {
    private final Path uploadImagePath;

    public FileUploadServiceImpl(@Value("${upload.image.path}") String uploadImagePath) {
        this.uploadImagePath = Path.of(uploadImagePath);
    }

    @PostConstruct
    public void init() throws IOException {
        if (!Files.exists(uploadImagePath)) {
            Files.createDirectories(uploadImagePath);
        }
    }

    @Override
    public Mono<String> uploadImage(FilePart imageFile) {
        return Mono.defer(() -> {
            checkImageFile(imageFile);
            String filename = generateUniqueFilename(imageFile);
            return saveImageFile(imageFile, filename)
                    .thenReturn(filename);
        });
    }

    private void checkImageFile(FilePart imageFile) {
        MediaType contentType = imageFile.headers().getContentType();
        if (contentType == null || !contentType.toString().startsWith("image/")) {
            throw new UploadFileException("File is not an image");
        }
    }

    private String generateUniqueFilename(FilePart imageFile) {
        String originalFilename = imageFile.filename();
        String extension = getExtension(originalFilename);

        return UUID.randomUUID() + extension;
    }

    private String getExtension(String originalFilename) {
        return originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
    }

    private Mono<Void> saveImageFile(FilePart imageFile, String filename) {
        Path filePath = uploadImagePath.resolve(filename);
        return imageFile.transferTo(filePath);
    }
}
