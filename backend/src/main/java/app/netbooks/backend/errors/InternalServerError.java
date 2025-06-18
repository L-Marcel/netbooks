package app.netbooks.backend.errors;

import java.sql.SQLException;

import org.springframework.http.HttpStatus;

import lombok.Getter;

public class InternalServerError extends HttpError {
    @Getter
    private SQLException sqlException;

    public InternalServerError() {
        super("Erro interno no servidor!", HttpStatus.INTERNAL_SERVER_ERROR);
    };

    public InternalServerError(SQLException sqlException) {
        this();
        this.sqlException = sqlException;
    };
};
