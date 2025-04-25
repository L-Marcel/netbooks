package app.netbooks.backend;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

import app.netbooks.backend.repositories.PlansRepositoryTest;

@Suite
@SuiteDisplayName("Repositories")
@SelectClasses({PlansRepositoryTest.class})
public class RepositoriesTestSuite {};