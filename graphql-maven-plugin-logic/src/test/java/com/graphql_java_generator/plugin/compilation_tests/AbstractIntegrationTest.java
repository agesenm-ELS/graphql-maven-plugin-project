package com.graphql_java_generator.plugin.compilation_tests;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.graphql_java_generator.plugin.DocumentParser;
import com.graphql_java_generator.plugin.Generator;
import com.graphql_java_generator.plugin.conf.GraphQLConfiguration;
import com.graphql_java_generator.plugin.test.compiler.CompilationTestHelper;
import com.graphql_java_generator.plugin.test.helper.GraphqlTestHelper;
import com.graphql_java_generator.plugin.test.helper.MavenTestHelper;

abstract class AbstractIntegrationTest {

	Class<?> springConfClass;

	protected AbstractApplicationContext ctx = null;
	protected DocumentParser generateCodeDocumentParser;
	protected Generator codeGenerator;
	protected CompilationTestHelper compilationTestHelper;
	protected GraphqlTestHelper graphqlTestHelper;
	protected MavenTestHelper mavenTestHelper;
	GraphQLConfiguration pluginConfiguration;

	public AbstractIntegrationTest(Class<?> springConfClass) {
		this.springConfClass = springConfClass;
	}

	@BeforeEach
	void loadApplicationContext() throws IOException {
		ctx = new AnnotationConfigApplicationContext(springConfClass);
		generateCodeDocumentParser = ctx.getBean(DocumentParser.class);
		codeGenerator = ctx.getBean(Generator.class);
		compilationTestHelper = ctx.getBean(CompilationTestHelper.class);
		graphqlTestHelper = ctx.getBean(GraphqlTestHelper.class);
		mavenTestHelper = ctx.getBean(MavenTestHelper.class);
		pluginConfiguration = ctx.getBean(GraphQLConfiguration.class);

		generateCodeDocumentParser.parseGraphQLSchemas();
	}

	@AfterEach
	void cleanUp() {
		if (ctx != null) {
			ctx.close();
		}
	}

	/**
	 * This test will be executed for each concrete subclass of this class
	 * 
	 * @throws MojoExecutionException
	 * @throws IOException
	 */
	@Test
	void testGenerateCode() throws IOException {
		// Preparation
		mavenTestHelper.deleteDirectoryAndContentIfExists(pluginConfiguration.getTargetSourceFolder());
		mavenTestHelper.deleteDirectoryAndContentIfExists(pluginConfiguration.getTargetClassFolder());
		pluginConfiguration.logConfiguration();

		// Go, go, go
		codeGenerator.generateCode();

		// Verifications
		compilationTestHelper.checkCompleteCompilationStatus(null);
	}

}
