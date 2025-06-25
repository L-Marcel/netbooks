package app.netbooks.backend;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;

import app.netbooks.backend.advices.ExceptionAdviceTests;
import app.netbooks.backend.controllers.TagsControllerTests;
import app.netbooks.backend.controllers.UsersControllerTests;
import app.netbooks.backend.repositories.AuthorRepositoryTests;
import app.netbooks.backend.repositories.PlansRepositoryTests;
import app.netbooks.backend.repositories.TagsRepositoryTests;
import app.netbooks.backend.repositories.UsersRepositoryTests;
import app.netbooks.backend.validation.ValidatorTests;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class Tests {
    @Nested
    @Order(10)
    class Validator extends ValidatorTests {};

    @Nested
    @Order(100)
    class PlansRepository extends PlansRepositoryTests {};

    @Nested
    @Order(101)
    class TagsRepository extends TagsRepositoryTests {};

    @Nested
    @Order(102)
    class UsersRepository extends UsersRepositoryTests {};

    @Nested
    @Order(103)
    class AuthorRepository extends AuthorRepositoryTests {};

    @Nested
    @Order(10000)
    class UsersController extends UsersControllerTests {};

    @Nested
    @Order(10001)
    class TagsController extends TagsControllerTests {};

    @Nested
    @Order(100000)
    class ExceptionAdvice extends ExceptionAdviceTests {};
};
