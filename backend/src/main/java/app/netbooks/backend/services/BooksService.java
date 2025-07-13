package app.netbooks.backend.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.io.RandomAccessReadBuffer;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import app.netbooks.backend.connections.transactions.Transactions;
import app.netbooks.backend.errors.BookNotFound;
import app.netbooks.backend.errors.BookPageNotFound;
import app.netbooks.backend.errors.HttpError;
import app.netbooks.backend.errors.InternalServerError;
import app.netbooks.backend.files.images.BooksImagesStorage;
import app.netbooks.backend.files.pdfs.BookPdfsStorage;
import app.netbooks.backend.models.Author;
import app.netbooks.backend.models.Benefit;
import app.netbooks.backend.models.Book;
import app.netbooks.backend.models.Publisher;
import app.netbooks.backend.models.Tag;
import app.netbooks.backend.models.User;
import app.netbooks.backend.repositories.interfaces.AuthorsRepository;
import app.netbooks.backend.repositories.interfaces.BooksAuthorsRepository;
import app.netbooks.backend.repositories.interfaces.BooksBenefitsRepository;
import app.netbooks.backend.repositories.interfaces.BooksRepository;
import app.netbooks.backend.repositories.interfaces.BooksTagsRepository;
import app.netbooks.backend.repositories.interfaces.PublishersRepository;
import app.netbooks.backend.repositories.interfaces.TagsRepository;
import app.netbooks.backend.utils.Server;
import app.netbooks.backend.validation.Validator;

@Service
public class BooksService {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired
    private PublishersRepository publishersRepository;

    @Autowired
    private TagsRepository tagsRepository;

    @Autowired
    private BooksTagsRepository booksTagsRepository;

    @Autowired
    private AuthorsRepository authorsRepository;

    @Autowired
    private BooksAuthorsRepository booksAuthorsRepository;

    @Autowired
    private BookPdfsStorage pdfsStorage;

    @Autowired
    private BooksImagesStorage imagesStorage;

    @Autowired
    private BooksBenefitsRepository booksBenefitsRepository;

    @Autowired
    private Server server;

    @Autowired
    private Transactions transactions;

    public List<Book> search(String query) {
        return this.booksRepository.search(query);
    };

    public List<Book> searchFromBookcase(String query, User user) {
        return this.booksRepository.searchFromBookcase(query, user);
    };
    
    public List<Book> findAll() {
        return this.booksRepository.findAll();
    };

    public Book findById(Long id) {
        return this.booksRepository.findById(id)
            .orElseThrow(BookNotFound::new);
    };

    public Resource findContentById(Long id) {
        Resource file = this.pdfsStorage.gerFile(id);
        
        try (
            PDDocument bookPDF = this.convertToPDF(file);
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

    private PDDocument convertToPDF(InputStreamSource file) throws IOException {
        InputStream inputStream = file.getInputStream();
        RandomAccessReadBuffer buffer = new RandomAccessReadBuffer(inputStream);
        return Loader.loadPDF(buffer);
    };

    public Resource findContentById(Long id, Integer page) {
        Resource file = this.pdfsStorage.gerFile(id);
        
        try (
            PDDocument bookPDF = this.convertToPDF(file);
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

    public void create(
        String title,
        String description,
        Long isbn,
        Date publishedIn,
        Publisher publisher,
        Tag[] tags,
        Author[] authors,
        Benefit[] requirements,
        MultipartFile cover,
        MultipartFile banner,
        MultipartFile file
    ) {
        Date currentDate = this.server.getServerCurrentDate();
        Validator validator = new Validator();

        validator.validate("title", title)
            .min(3, "Tem mais de 2 caracteres!", "Tem menos de 3 caracteres!")
            .max(120, "Tem menos de 121 caracteres!", "Tem mais de 120!");
        
        validator.validate("description", description)
            .min(3, "Tem mais de 2 caracteres!", "Tem menos de 3 caracteres")
            .max(300, "Tem menos de 301 caracteres!", "Tem mais de 300!");

        validator.validate("isbn", isbn)
            .nullable()
            .min(0, "É maior que 0!", "É menor que 0!")
            .verify((value) -> value.toString().length() == 13, "Tem 13 dígitos!", "Não tem 13 digitos!")
            .verify((value) -> !this.booksRepository.findByIsbn(value).isPresent(), "Disponível para uso!", "Já se encontra em uso!");
        
        validator.validate("publishedIn", publishedIn)
            .verify((value) -> value.compareTo(currentDate) <= 0, "Foi publicado em uma data válida!", "Foi publicado em uma data inválida!");
        
        validator.validate("publisher", publisher)
            .verify(
                (value) -> value.getName() != null && 
                    value.getName().length() > 2, 
                "Tem mais de 2 caracteres!", 
                "Tem menos de 3 caracteres!"
            ).verify(
                (value) -> value.getName() != null && 
                    value.getName().length() < 81, 
                    "Tem menos de 81 caracteres!", 
                    "Tem mais de 80 caracteres!"
            );

        validator.validate("tags", tags)
            .min(2, "Tem mais de 2 marcadores!", "Tem menos de 2 marcadores!")
            .max(10, "Tem menos de 11 marcadores!", "Tem mais de 10 marcadores!")
            .verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (tag) -> tag != null && 
                            tag.getName() != null && 
                            tag.getName().length() > 1
                    ),
                "Os marcadores tem mais de 1 caracter!", 
                "Há marcadores com menos de 2 caracteres!"
            ).verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (tag) -> tag != null && 
                            tag.getName() != null && 
                            tag.getName().length() < 41
                    ),
                "Os marcadores tem menos de 41 caracteres!", 
                "Há marcadores com mais de 40 caracteres!"
            );
        
        validator.validate("authors", authors)
            .min(1, "Tem um autor!", "Tem não tem autor!")
            .max(8, "Tem menos de 9 autores!", "Tem mais de 8 autores!")
            .verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (author) -> author != null && 
                            author.getName() != null && 
                            author.getName().length() > 2
                    ),
                "Os nomes tem mais de 2 caracteres!", 
                "Há nomes com menos de 3 caracteres!"
            ).verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (author) -> author != null && 
                            author.getName() != null && 
                            author.getName().length() < 41
                    ),
                "Os nomes tem menos de 41 caracteres!", 
                "Há nomes com mais de 40 caracteres!"
            );
        
        validator.validate("cover", cover)
            .verifyIfCatch((value) -> imagesStorage.validate(value), "Formato valido de imagem!", "Formato inválido de imagem!");
        
        validator.validate("banner", banner)
            .verifyIfCatch((value) -> imagesStorage.validate(value), "Formato valido de imagem!", "Formato inválido de imagem!");
        
        validator.validate("file", file)
            .verifyIfCatch((value) -> pdfsStorage.validate(value), "Formato valido de PDF!", "Formato inválido de PDF!")
            .verifyWithCatch((value) -> {
                try (
                    PDDocument bookPDF = this.convertToPDF(file);
                ) {
                    return bookPDF.getNumberOfPages() > 0;
                }
            }, "O arquivo tem mais de uma página!", "O arquivo não tem páginas!");
        
        validator.run();

        Integer numPages = 0;

        try (
            PDDocument bookPDF = this.convertToPDF(file);
        ) {
            numPages = bookPDF.getNumberOfPages();
        } catch (Exception e) {
            throw new InternalServerError();
        };

        Book book = new Book(
            isbn,
            title,
            description,
            numPages,
            publishedIn,
            publisher
        );

        this.transactions.run(() -> {
            Optional<Publisher> bookPublisher = this.publishersRepository.findByName(book.getPublisher().getName());
            if(bookPublisher.isEmpty()) {
                this.publishersRepository.create(book.getPublisher());
            };

            this.booksRepository.create(book);
            this.tagsRepository.createMany(List.of(tags));
            this.booksTagsRepository.createMany(List.of(tags), book.getId());
            this.authorsRepository.createMany(
                List.of(authors)
                    .stream()
                    .filter((author) -> author.getId() < 0)
                    .toList()
            );
            this.booksAuthorsRepository.createMany(List.of(authors), book.getId());
            this.booksBenefitsRepository.createMany(List.of(requirements), book.getId());
            this.imagesStorage.storeCover(book.getId(), cover);
            this.imagesStorage.storeBanner(book.getId(), banner);
            this.pdfsStorage.storeFile(book.getId(), file);
        }, () -> {
            this.imagesStorage.deleteCover(book.getId());
            this.imagesStorage.deleteBanner(book.getId());
            this.pdfsStorage.deleteFile(book.getId());
        });
    };

    public void update(
        Book book,
        String title,
        String description,
        Long isbn,
        Date publishedIn,
        Publisher publisher,
        Tag[] tags,
        Author[] authors,
        Benefit[] requirements,
        MultipartFile cover,
        MultipartFile banner,
        MultipartFile file
    ) {
        Date currentDate = this.server.getServerCurrentDate();
        Validator validator = new Validator();

        validator.validate("title", title)
            .min(3, "Tem mais de 2 caracteres!", "Tem menos de 3 caracteres!")
            .max(120, "Tem menos de 121 caracteres!", "Tem mais de 120!");
        
        validator.validate("description", description)
            .min(3, "Tem mais de 2 caracteres!", "Tem menos de 3 caracteres")
            .max(300, "Tem menos de 301 caracteres!", "Tem mais de 300!");

        Optional<Book> candidate = this.booksRepository.findByIsbn(isbn);
        validator.validate("isbn", isbn)
            .nullable()
            .min(0, "É maior que 0!", "É menor que 0!")
            .verify((value) -> value.toString().length() == 13, "Tem 13 dígitos!", "Não tem 13 digitos!")
            .verify((value) -> !candidate.isPresent() || candidate.get().getId().equals(book.getId()), "Disponível para uso!", "Já se encontra em uso!");
        
        validator.validate("publishedIn", publishedIn)
            .verify((value) -> value.compareTo(currentDate) <= 0, "Foi publicado em uma data válida!", "Foi publicado em uma data inválida!");
        
        validator.validate("publisher", publisher)
            .verify(
                (value) -> value.getName() != null && 
                    value.getName().length() > 2, 
                "Tem mais de 2 caracteres!", 
                "Tem menos de 3 caracteres!"
            ).verify(
                (value) -> value.getName() != null && 
                    value.getName().length() < 81, 
                    "Tem menos de 81 caracteres!", 
                    "Tem mais de 80 caracteres!"
            );

        validator.validate("tags", tags)
            .min(2, "Tem mais de 2 marcadores!", "Tem menos de 2 marcadores!")
            .max(10, "Tem menos de 11 marcadores!", "Tem mais de 10 marcadores!")
            .verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (tag) -> tag != null && 
                            tag.getName() != null && 
                            tag.getName().length() > 1
                    ),
                "Os marcadores tem mais de 1 caracter!", 
                "Há marcadores com menos de 2 caracteres!"
            ).verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (tag) -> tag != null && 
                            tag.getName() != null && 
                            tag.getName().length() < 41
                    ),
                "Os marcadores tem menos de 41 caracteres!", 
                "Há marcadores com mais de 40 caracteres!"
            );
        
        validator.validate("authors", authors)
            .min(1, "Tem um autor!", "Tem não tem autor!")
            .max(8, "Tem menos de 9 autores!", "Tem mais de 8 autores!")
            .verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (author) -> author != null && 
                            author.getName() != null && 
                            author.getName().length() > 2
                    ),
                "Os nomes tem mais de 2 caracteres!", 
                "Há nomes com menos de 3 caracteres!"
            ).verify(
                (value) -> List.of(value)
                    .stream()
                    .allMatch(
                        (author) -> author != null && 
                            author.getName() != null && 
                            author.getName().length() < 41
                    ),
                "Os nomes tem menos de 41 caracteres!", 
                "Há nomes com mais de 40 caracteres!"
            );
        
        validator.validate("cover", cover)
            .verifyIfCatch((value) -> imagesStorage.validate(value), "Formato valido de imagem!", "Formato inválido de imagem!");
        
        validator.validate("banner", banner)
            .verifyIfCatch((value) -> imagesStorage.validate(value), "Formato valido de imagem!", "Formato inválido de imagem!");
        
        validator.validate("file", file)
            .verifyIfCatch((value) -> pdfsStorage.validate(value), "Formato valido de PDF!", "Formato inválido de PDF!")
            .verifyWithCatch((value) -> {
                try (
                    PDDocument bookPDF = this.convertToPDF(file);
                ) {
                    return bookPDF.getNumberOfPages() > 0;
                }
            }, "O arquivo tem mais de uma página!", "O arquivo não tem páginas!");
        
        validator.run();

        Integer numPages = 0;

        try (
            PDDocument bookPDF = this.convertToPDF(file);
        ) {
            numPages = bookPDF.getNumberOfPages();
        } catch (Exception e) {
            throw new InternalServerError();
        };

        Publisher oldPublisher = book.getPublisher();
        book.setIsbn(isbn);
        book.setTitle(title);
        book.setDescription(description);
        book.setNumPages(numPages);
        book.setPublishedIn(publishedIn);
        book.setPublisher(publisher);

        this.transactions.run(() -> {
            Optional<Publisher> bookPublisher = this.publishersRepository.findByName(book.getPublisher().getName());
            if(bookPublisher.isEmpty()) {
                this.publishersRepository.create(book.getPublisher());
            };

            this.booksRepository.update(book);
            this.publishersRepository.deleteIfNotUsedByName(oldPublisher.getName());
            
            this.tagsRepository.createMany(List.of(tags));
            this.booksTagsRepository.deleteByBook(book.getId());
            this.booksTagsRepository.createMany(List.of(tags), book.getId());
            this.tagsRepository.deleteManyIfNotUsedByName(
                List.of(tags).stream()
                    .map((tag) -> tag.getName())
                    .collect(Collectors.toList())
            );

            this.booksAuthorsRepository.deleteByBook(book.getId());
            this.authorsRepository.createMany(
                List.of(authors)
                    .stream()
                    .filter((author) -> author.getId() < 0)
                    .toList()
            );
            this.booksAuthorsRepository.createMany(List.of(authors), book.getId());
            this.authorsRepository.deleteManyIfNotUsedById(
                List.of(authors).stream()
                    .map((author) -> author.getId())
                    .collect(Collectors.toList())
            );

            this.booksBenefitsRepository.deleteByBook(book.getId());
            this.booksBenefitsRepository.createMany(List.of(requirements), book.getId());
            
            this.imagesStorage.storeCover(book.getId(), cover);
            this.imagesStorage.storeBanner(book.getId(), banner);
            this.pdfsStorage.storeFile(book.getId(), file);
        });
    };

    public void deleteById(
        Long id
    ) {
        Book book = this.findById(id);
        Publisher oldPublisher = book.getPublisher();

        this.transactions.run(() -> {
            List<Tag> tags = this.booksTagsRepository.findAllByBook(book.getId());
            List<Author> authors = this.booksAuthorsRepository.findAllByBook(book.getId());
            
            this.booksRepository.deleteById(book.getId());
            this.publishersRepository.deleteIfNotUsedByName(oldPublisher.getName());
            this.tagsRepository.deleteManyIfNotUsedByName(
                tags.stream()
                    .map((tag) -> tag.getName())
                    .collect(Collectors.toList())
            );
            this.authorsRepository.deleteManyIfNotUsedById(
                authors.stream()
                    .map((author) -> author.getId())
                    .collect(Collectors.toList())
            );

            this.pdfsStorage.deleteFile(book.getId());
            this.imagesStorage.deleteBanner(book.getId());
            this.imagesStorage.deleteCover(book.getId());
        });
    };
};