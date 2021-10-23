package com.graphql_java_generator.plugin.compilation_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import graphql.mavenplugin_notscannedbyspring.Forum_Client_SpringConfiguration;

@Tag("forum")
class ForumClientTest extends AbstractIntegrationTest {

	public ForumClientTest() {
		super(Forum_Client_SpringConfiguration.class);
	}

	@BeforeEach
	public void setUp() {
		graphqlTestHelper.checkSchemaStringProvider("forum.graphqls");
	}

}
