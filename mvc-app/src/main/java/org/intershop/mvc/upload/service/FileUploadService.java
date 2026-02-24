package org.intershop.mvc.upload.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface FileUploadService {
    Mono<String> uploadImage(FilePart imageFile);
}
