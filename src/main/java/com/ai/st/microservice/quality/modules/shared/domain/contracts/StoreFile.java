package com.ai.st.microservice.quality.modules.shared.domain.contracts;

public interface StoreFile {

    String storeFilePermanently(byte[] bytes, String extensionFile, String namespace);

    String storeFileTemporarily(byte[] bytes, String extensionFile);

    void deleteFile(String pathFile);

}
