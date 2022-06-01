package com.ai.st.microservice.quality.modules.shared.infrastructure.store;

import com.ai.st.microservice.quality.modules.shared.domain.contracts.StoreFile;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.StoreFileError;
import com.ai.st.microservice.quality.modules.shared.infrastructure.microservices.HTTPWorkspaceMicroservice;
import com.ai.st.microservice.quality.modules.shared.infrastructure.tracing.SCMTracing;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

@Service
public final class LocalStoreFile implements StoreFile {

    private final Logger log = LoggerFactory.getLogger(LocalStoreFile.class);

    @Value("${st.temporalDirectory}")
    private String stTemporalDirectory;

    @Value("${st.filesDirectory}")
    private String stFilesDirectory;

    @Override
    public String storeFilePermanently(byte[] bytes, String extensionFile, String namespace) {

        try {

            String fileName = RandomStringUtils.random(20, true, false) + "." + extensionFile;

            namespace = stFilesDirectory + namespace;

            String pathFile = namespace + File.separatorChar + StringUtils.cleanPath(fileName);

            FileUtils.writeByteArrayToFile(new File(pathFile), bytes);

            return pathFile;

        } catch (IOException e) {
            String messageError = String.format("Error guardando permanentemente el archivo: %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new StoreFileError();
        }

    }

    @Override
    public String storeFileTemporarily(byte[] bytes, String extensionFile) {

        try {

            String fileName = RandomStringUtils.random(14, true, false) + "." + extensionFile;

            String temporalFile = stTemporalDirectory + File.separatorChar + StringUtils.cleanPath(fileName);

            FileUtils.writeByteArrayToFile(new File(temporalFile), bytes);

            return temporalFile;

        } catch (IOException e) {
            String messageError = String.format("Error guardando temporalmente el archivo: %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
            throw new StoreFileError();
        }
    }

    @Override
    public void deleteFile(String pathFile) {
        try {
            FileUtils.deleteQuietly(new File(pathFile));
        } catch (Exception e) {
            String messageError = String.format("Error eliminando el archivo: %s", e.getMessage());
            SCMTracing.sendError(messageError);
            log.error(messageError);
        }
    }

}
