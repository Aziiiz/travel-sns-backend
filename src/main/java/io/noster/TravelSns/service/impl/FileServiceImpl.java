package io.noster.TravelSns.service.impl;

import io.noster.TravelSns.model.FileInfo;
import io.noster.TravelSns.payload.request.FileRequest;
import io.noster.TravelSns.service.FileService;
import io.noster.common.bbs.BasicListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    private final Path root = Paths.get("/home/aziz/aziz/TravelSns/upload/");
    @Override
    public BasicListResponse uploadFiles(FileRequest fileRequest) {
        BasicListResponse res = new BasicListResponse();
        try {
            logger.info("[ uploadFiles ]--" + fileRequest.getTitle()+ "\n" +
                    + fileRequest.getFile().length + "] ");
            List<String> fileNames = new ArrayList<>();
            Arrays.asList(fileRequest.getFile()).stream().forEach(file-> {
                save(file);
                fileNames.add(file.getOriginalFilename());
            });
            res.setList(fileNames);
        } catch (Exception e) {
            res.setState(BasicListResponse.STATE_ERROR);
            res.setStateMessage(e.getMessage());
        }
        return res;
    }


    private void save(MultipartFile file) {
        try {
            logger.info("[SAVE IMAGE  PATH] ---"+ root + " imagname "+ file.getOriginalFilename()+ "---");
            Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        }catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: "+ e.getMessage());
        }
    }
}
