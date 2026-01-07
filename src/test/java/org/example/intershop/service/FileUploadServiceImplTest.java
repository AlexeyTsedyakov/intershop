package org.example.intershop.service;

import org.example.intershop.exception.UploadFileException;
import org.example.intershop.service.impl.FileUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class FileUploadServiceImplTest {

    @TempDir
    Path tempDir;

    private FileUploadServiceImpl fileUploadService;
    private Path uploadPath;

    @BeforeEach
    void setUp() throws IOException {
        uploadPath = tempDir.resolve("images");
        fileUploadService = new FileUploadServiceImpl(uploadPath.toString());
        fileUploadService.init();
    }

    @Test
    void init_shouldCreateUploadDirectory() {
        assertThat(Files.exists(uploadPath)).isTrue();
        assertThat(Files.isDirectory(uploadPath)).isTrue();
    }

    @Test
    void uploadImage_shouldSaveFile_whenFileIsValid() throws IOException {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "some-image-content".getBytes()
        );

        String filename = fileUploadService.uploadImage(file);

        assertThat(filename).isNotNull();
        assertThat(filename).endsWith(".jpg");
        assertThat(filename).doesNotContain("test-image"); // UUID generated

        Path savedFile = uploadPath.resolve(filename);
        assertThat(Files.exists(savedFile)).isTrue();
        assertThat(Files.readAllBytes(savedFile)).isEqualTo("some-image-content".getBytes());
    }

    @Test
    void uploadImage_shouldThrowException_whenFileIsEmpty() {
        MockMultipartFile file = new MockMultipartFile(
                "image",
                "empty.jpg",
                "image/jpeg",
                new byte[0]
        );

        assertThatThrownBy(() -> fileUploadService.uploadImage(file))
                .isInstanceOf(UploadFileException.class)
                .hasMessage("Image file is empty");
    }

    @Test
    void uploadImage_shouldThrowException_whenFileIsNotImage() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "some-text".getBytes()
        );

        assertThatThrownBy(() -> fileUploadService.uploadImage(file))
                .isInstanceOf(UploadFileException.class)
                .hasMessage("File is not an image");
    }

    @Test
    void uploadImage_shouldThrowException_whenContentTypeIsNull() {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.jpg",
                null,
                "some-image-content".getBytes()
        );

        assertThatThrownBy(() -> fileUploadService.uploadImage(file))
                .isInstanceOf(UploadFileException.class)
                .hasMessage("File is not an image");
    }
}
