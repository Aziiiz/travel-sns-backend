package io.noster.TravelSns.service;

import io.noster.TravelSns.payload.request.FileRequest;
import io.noster.common.bbs.BasicListResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface FileService {

    BasicListResponse uploadFiles(FileRequest fileRequest);
}
