package io.noster.TravelSns.payload.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileRequest {
    private MultipartFile[] file;
    private String title;
    private String description;
    private double longtitue;
    private double latitude;

}
