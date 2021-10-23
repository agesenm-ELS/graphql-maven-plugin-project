package com.graphql_java_generator.plugin.compilation_tests;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;

import graphql.mavenplugin_notscannedbyspring.StarWars_Server_SpringConfiguration;

@Tag("starwars")
class StarWarsServerTest extends AbstractIntegrationTest {

	public StarWarsServerTest() {
		super(StarWars_Server_SpringConfiguration.class);
	}

	@BeforeEach
	public void setUp() {
		graphqlTestHelper.checkSchemaStringProvider("starWarsSchema.graphqls");
	}

}
