package app.netbooks.backend.files.images;

import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.errors.EmptyFile;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidImageFormat;

public interface BooksImagesStorage {
    public void storeCover(Long id, MultipartFile file) 
        throws InvalidImageFormat, InternalServerError, EmptyFile; 
    public void deleteCover(Long id)
        throws InternalServerError; 
    public void storeBanner(Long id, MultipartFile file) 
        throws InvalidImageFormat, InternalServerError, EmptyFile; 
    public void deleteBanner(Long id)
        throws InternalServerError;
};