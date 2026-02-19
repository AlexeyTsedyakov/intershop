package org.example.intershop.service;

import org.example.intershop.exception.UploadFileException;
import org.example.intershop.service.impl.FileUploadServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
    void uploadImage_shouldSaveFile_whenFileIsValid() {
        FilePart file = mock(FilePart.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        when(file.headers()).thenReturn(headers);
        when(file.filename()).thenReturn("test-image.jpg");
        when(file.transferTo(any(Path.class))).thenReturn(Mono.empty());

        StepVerifier.create(fileUploadService.uploadImage(file))
                .assertNext(filename -> {
                    assertThat(filename).isNotNull();
                    assertThat(filename).endsWith(".jpg");
                    assertThat(filename).doesNotContain("test-image");
                })
                .verifyComplete();

        verify(file).transferTo(any(Path.class));
    }

    @Test
    void uploadImage_shouldThrowException_whenFileIsNotImage() {
        FilePart file = mock(FilePart.class);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);

        when(file.headers()).thenReturn(headers);

        StepVerifier.create(fileUploadService.uploadImage(file))
                .expectErrorMatches(throwable -> throwable instanceof UploadFileException &&
                        throwable.getMessage().equals("File is not an image"))
                .verify();
    }

    @Test
    void uploadImage_shouldThrowException_whenContentTypeIsNull() {
        FilePart file = mock(FilePart.class);
        HttpHeaders headers = new HttpHeaders();

        when(file.headers()).thenReturn(headers);

        StepVerifier.create(fileUploadService.uploadImage(file))
                .expectErrorMatches(throwable -> throwable instanceof UploadFileException &&
                        throwable.getMessage().equals("File is not an image"))
                .verify();
    }
}
