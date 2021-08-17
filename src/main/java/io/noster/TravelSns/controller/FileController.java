package io.noster.TravelSns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.noster.TravelSns.payload.request.FileRequest;
import io.noster.TravelSns.service.FileService;
import io.noster.common.basic.BasicController;
import io.noster.common.basic.ObjectMapperInstance;
import io.noster.common.bbs.BasicListResponse;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@Api(tags={"FILE API"})
@RequestMapping("/api/place")
public class FileController extends BasicController {

    @Autowired
    private FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@ModelAttribute FileRequest fileRequest) {
        long startTime = System.currentTimeMillis();
        int retSize = 0;

        String resString = "{}";
        HttpStatus resStatus = HttpStatus.OK;
        ObjectMapper mapper = ObjectMapperInstance.getInstance().getMapper();
        try {
            log.info("[" + startTime+ "]............ Start(");
            BasicListResponse res = fileService.uploadFiles(fileRequest);
            resString = mapper.writeValueAsString(res);
        } catch (Exception e) {
            resStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            resString = e.getMessage();
        } finally {
            log.info("[" + (System.currentTimeMillis() - startTime)+ "]............ End)");

        }
        return new ResponseEntity<>(resString, resStatus);
    }

}
