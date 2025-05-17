package app.netbooks.backend.images;

import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.errors.InvalidImageFormat;

public interface BookImagesStorage {
    public void storeCover(int id, String base64) 
        throws InvalidImageFormat, InternalServerError; 
    public void deleteCover(int id)
        throws InternalServerError; 
    public void storeBanner(int id, String base64) 
        throws InvalidImageFormat, InternalServerError; 
    public void deleteBanner(int id)
        throws InternalServerError;
};