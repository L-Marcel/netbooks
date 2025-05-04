package app.netbooks.backend;

import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestClassOrder;

import app.netbooks.backend.controllers.UsersControllerTests;
import app.netbooks.backend.repositories.PlansRepositoryTests;
import app.netbooks.backend.repositories.TagsRepositoryTests;
import app.netbooks.backend.repositories.UsersRepositoryTests;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class Tests {
    @Nested
    @Order(1)
    class PlansRepository extends PlansRepositoryTests {};

    @Nested
    @Order(2)
    class TagsRepository extends TagsRepositoryTests {};

    @Nested
    @Order(3)
    class UsersRepository extends UsersRepositoryTests {};

    @Nested
    @Order(4)
    class UsersController extends UsersControllerTests {};
};
