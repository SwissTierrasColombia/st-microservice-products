package com.ai.st.microservice.quality.modules.shared.infrastructure.compressor;

import com.ai.st.microservice.quality.modules.shared.domain.contracts.CompressorFile;
import com.ai.st.microservice.quality.modules.shared.domain.exceptions.CompressError;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

@Service
public final class ZIPCompressorFile implements CompressorFile {

    @Override
    public int countEntries(String filePath) throws CompressError {
        int count = 0;
        try {
            ZipFile zipFile = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                entries.nextElement();
                count++;
            }
            zipFile.close();
        } catch (IOException e) {
            throw new CompressError("Ha ocurrido un error leyendo el archivo zip.");
        }
        return count;
    }

    @Override
    public boolean checkIfFileIsPresent(String filePath, String extension) throws CompressError {
        try {
            ZipFile zipFile = new ZipFile(filePath);
            Enumeration<? extends ZipEntry> entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (FilenameUtils.getExtension(entry.getName()).equalsIgnoreCase(extension)) {
                    return true;
                }
            }
            zipFile.close();
        } catch (IOException e) {
            throw new CompressError("Ha ocurrido un error leyendo el archivo zip.");
        }
        return false;
    }

    @Override
    public String compress(File file, String namespace, String zipName) throws CompressError {

        try {

            boolean directoryCreatedSuccessful = new File(namespace).mkdirs();
            if (!directoryCreatedSuccessful) {
                throw new CompressError("Ha ocurrido un error creando el directorio del zip.");
            }

            String path = namespace + File.separatorChar + zipName + ".zip";

            File fileZip = new File(path);
            if (fileZip.exists()) {
                boolean fileRemovedSuccessful = fileZip.delete();
                if (!fileRemovedSuccessful) {
                    throw new CompressError("Ha ocurrido un error eliminando el archivo zip existente.");
                }
            }

            byte[] buffer = new byte[1024];

            FileOutputStream fileOutputStream = new FileOutputStream(fileZip);
            ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream);

            ZipEntry entry = new ZipEntry(file.getName());
            zipOutputStream.putNextEntry(entry);

            FileInputStream fileInputStream = new FileInputStream(file.getAbsolutePath());
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
            }
            fileInputStream.close();

            zipOutputStream.closeEntry();
            zipOutputStream.close();
            fileOutputStream.close();

            return path;

        } catch (CompressError e) {
            throw new CompressError(e.errorMessage());
        } catch (IOException e) {
            throw new CompressError("Ha ocurrido un error creando el archivo zip.");
        }
    }

}
