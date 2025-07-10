package app.netbooks.backend.files.pdfs;

import app.netbooks.backend.errors.EmptyFile;
import app.netbooks.backend.errors.InvalidPdfFormat;
import app.netbooks.backend.files.FilesStorage;

import org.springframework.web.multipart.MultipartFile;

public abstract class PdfsStorage extends FilesStorage {
    public PdfsStorage() {
        super("pdf");
    };
    
    @Override
    public void validate(
        MultipartFile file
    ) throws InvalidPdfFormat, EmptyFile {
        super.validate(file);

        if(file.isEmpty() || file.getContentType() == null) 
            throw new InvalidPdfFormat();

        String type = file.getContentType();
        Boolean isValid = type.contains("pdf");
        
        if(!isValid) throw new InvalidPdfFormat();
    };
};
