package org.example.intershop.service.impl;

import jakarta.annotation.PostConstruct;
import org.example.intershop.exception.UploadFileException;
import org.example.intershop.service.FileUploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    public String uploadImage(MultipartFile imageFile) throws IOException {
        checkImageFile(imageFile);
        String filename = generateUniqueFilename(imageFile);
        saveImageFile(imageFile, filename);

        return filename;
    }

    private void checkImageFile(MultipartFile imageFile) {
        if (imageFile.isEmpty()) {
            throw new UploadFileException("Image file is empty");
        }

        String contentType = imageFile.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new UploadFileException("File is not an image");
        }
    }

    private String generateUniqueFilename(MultipartFile imageFile) {
        String originalFilename = imageFile.getOriginalFilename();
        String extension = getExtension(originalFilename);

        return UUID.randomUUID() + extension;
    }

    private String getExtension(String originalFilename) {
        return originalFilename != null && originalFilename.contains(".")
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
    }

    private void saveImageFile(MultipartFile imageFile, String filename) throws IOException {
        Path filePath = uploadImagePath.resolve(filename);
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
    }
}
