package com.example.my.service;

import com.example.my.domain.dto.BoardDTO;
import com.google.cloud.storage.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class GcsService {

    private final Storage storage;

    public void createFile(BoardDTO boardDTO) {


        Bucket bucket = storage.create(BucketInfo.of("my-app-storage-bucket"));

        storage.create(
                BlobInfo.newBuilder("my-app-storage-bucket", "subdirectory/my-file").build(),
                "file contents".getBytes()
        );
    }

}
