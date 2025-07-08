package app.netbooks.backend.files.pdfs;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.errors.BookFileNotFound;
import app.netbooks.backend.errors.EmptyFile;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidPdfFormat;

public interface BookPdfsStorage {
    public void storeFile(Long id, MultipartFile file) 
        throws InternalServerError, EmptyFile; 
    public void deleteFile(Long id)
        throws InternalServerError;
    public Resource gerFile(Long id)
        throws BookFileNotFound;
    public void validate(MultipartFile file) 
        throws InvalidPdfFormat, EmptyFile;
};