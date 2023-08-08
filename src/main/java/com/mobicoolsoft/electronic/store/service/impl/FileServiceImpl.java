package com.mobicoolsoft.electronic.store.service.impl;

import com.mobicoolsoft.electronic.store.config.AppConstants;
import com.mobicoolsoft.electronic.store.exception.BadApiRequestException;
import com.mobicoolsoft.electronic.store.service.FileServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * @author Sandip Kolhekar
 * @implNote file services implementation class
 */

@Service
public class FileServiceImpl implements FileServiceI {

    private final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * @implNote upload image
     * @param path
     * @param file
     * @return uploaded file name
     * @throws IOException
     */
    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        logger.info("uploadImage service started on path {}", path);
        String originalFilename = file.getOriginalFilename();
        if (!originalFilename.isEmpty()) {
            logger.info("originalFilename set by user is {}", originalFilename);
            String fileName = UUID.randomUUID().toString();
            logger.info("random fileName generated {}", fileName);
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase(".jpg")) {
                String fileNameWithExtension = fileName + extension;
                logger.info("new fileNameWithExtension {}", fileNameWithExtension);
                String filePath = path + fileNameWithExtension;
                File folder = new File(path);
                if (!folder.exists()) {
                    logger.info("created new directory (if not exists) with path {}", path);
                    folder.mkdirs();
                }
                Files.copy(file.getInputStream(), Paths.get(filePath));
                logger.info("uploadImage service ended");
                return fileNameWithExtension;
            } else {
                logger.info("BadApiRequestException encounter to the extension (other than .jpg, .jpeg, .png) {}", extension);
                throw new BadApiRequestException(AppConstants.EXTENSION_MSG);
            }
        } else {
            logger.info("BadApiRequestException(StringIndexOutOfBoundsException) encounter for empty file with index -1");
            throw new BadApiRequestException(AppConstants.EMPTY_IMAGE_MSG);
        }
    }

    /**
     * @implNote get image by id
     * @param path
     * @param fileName
     * @return image
     * @throws FileNotFoundException
     */
    @Override
    public InputStream serveImage(String path, String fileName) throws FileNotFoundException {
        String fullPath = path + File.separator + fileName;
        InputStream inputStream = new FileInputStream(fullPath);
        return inputStream;
    }
}