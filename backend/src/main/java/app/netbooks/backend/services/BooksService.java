package app.netbooks.backend.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;


import app.netbooks.backend.errors.BookNotFound;
import app.netbooks.backend.errors.BookPageNotFound;
import app.netbooks.backend.errors.HttpError;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.files.pdfs.BookPdfsStorage;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.repositories.interfaces.BooksRepository;

@Service
public class BooksService {
    @Autowired
    private BooksRepository repository;

    @Autowired
    private BookPdfsStorage pdfsStorage;
    
    public List<Book> findAll() {
        return this.repository.findAll();
    };

    public Book findById(Long id) {
        return this.repository.findById(id)
            .orElseThrow(BookNotFound::new);
    };

       public Resource findContentById(Long id) {
        Resource file = this.pdfsStorage.gerFile(id);
        
        try (
            InputStream inputStream = file.getInputStream();
            RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(inputStream);
            PDDocument bookPDF = Loader.loadPDF(buffer);
        ) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bookPDF.save(byteArrayOutputStream);
            
            return new ByteArrayResource(
                byteArrayOutputStream.toByteArray()
            );
        } catch (HttpError e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };

    public Resource findContentById(Long id, Integer page) {
        Resource file = this.pdfsStorage.gerFile(id);
        
        try (
            InputStream inputStream = file.getInputStream();
            RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(inputStream);
            PDDocument bookPDF = Loader.loadPDF(buffer);
            PDDocument singlePageBookPDF = new PDDocument();
        ) {
            if (page < 0 || page >= bookPDF.getNumberOfPages())
                throw new BookPageNotFound();
            singlePageBookPDF.addPage(bookPDF.getPage(page));

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            singlePageBookPDF.save(byteArrayOutputStream);
            
            return new ByteArrayResource(
                byteArrayOutputStream.toByteArray()
            );
        } catch (HttpError e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerError();
        }
    };
};