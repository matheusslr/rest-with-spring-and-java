package br.com.matheusslr.restwithspringandjava.service;

import br.com.matheusslr.restwithspringandjava.config.FileStorageConfig;
import br.com.matheusslr.restwithspringandjava.exceptions.FileStorageException;
import br.com.matheusslr.restwithspringandjava.exceptions.FileStorageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStoragePath;

    @Autowired
    public FileStorageService(FileStorageConfig fileStorageConfig) {
        Path path = Paths.get(fileStorageConfig.getUploadDir())
                .toAbsolutePath().normalize();

        this.fileStoragePath = path;

        try {
            Files.createDirectories(this.fileStoragePath);
        } catch (Exception e) {
            throw new FileStorageException("Could not create the directory where the files will be stored", e);
        }
    }

    public String storeFile(MultipartFile file) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            // filename..txt
            if (filename.contains(".."))
                throw new FileStorageException("Could not storage file. Filename contains invalid path sequence {..}");
            Path targetLocation = this.fileStoragePath.resolve(filename);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return filename;
        } catch (Exception e) {
            throw new FileStorageException("Could not store file" + filename + ". Please try again");
        }
    }

    public Resource loadFileAsResource(String filename) {
        try {
            Path filePath = this.fileStoragePath.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) return resource;
            else throw new FileStorageException("File not found");
        } catch (Exception e) {
            throw new FileStorageNotFoundException("File not found " + filename);
        }
    }
}
