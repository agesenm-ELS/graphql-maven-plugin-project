package com.graphql_java_generator.plugin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.graphql_java_generator.plugin.test.helper.GraphqlTestHelper;

import graphql.language.FieldDefinition;
import graphql.language.InputValueDefinition;
import graphql.language.NonNullType;
import graphql.language.ObjectTypeDefinition;
import graphql.language.TypeDefinition;
import graphql.language.TypeName;
import graphql.mavenplugin_notscannedbyspring.HelloWorld_Server_SpringConfiguration;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

/**
 * @author etienne-sf
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { HelloWorld_Server_SpringConfiguration.class })
@Execution(ExecutionMode.CONCURRENT)
class GraphqlMavenPluginTest {

	@Autowired
	GraphqlTestHelper graphqlTestHelper;

	@Autowired
	protected ResourceSchemaStringProvider schemaStringProvider;

	@BeforeEach
	public void beforeAll() {
		graphqlTestHelper.checkSchemaStringProvider("helloworld.graphqls");
	}

	// @Test
	// public void checkExecute_wrongMode() throws MojoExecutionException, MojoFailureException {
	// // Preparation
	// GraphqlMavenPlugin graphqlMavenPlugin = new GraphqlMavenPlugin();
	// graphqlMavenPlugin.mode = "A wrong value";
	//
	// // Go, go, go
	// Exception exception = assertThrows(MojoExecutionException.class, () -> graphqlMavenPlugin.execute());
	//
	// // Verification
	// assertTrue(exception.getMessage().contains(graphqlMavenPlugin.mode.mode()),
	// "The wrong mode is in the error message");
	// }

	@Test
	@Execution(ExecutionMode.CONCURRENT)
	void testDocuments_helloworld() throws IOException {
		// Preparation
		SchemaParser schemaParser = new SchemaParser();
		TypeDefinitionRegistry typeDefinitionRegistry = schemaParser
				.parse(schemaStringProvider.getConcatenatedSchemaStrings());

		// Go, go, go

		// Verification
		assertNotNull(schemaStringProvider, "A schemaStringProvider should be returned");
		assertNotNull(schemaStringProvider.getConcatenatedSchemaStrings(), "The schema should not be null");
		assertNotEquals("", schemaStringProvider.getConcatenatedSchemaStrings(), "The schema should not be empty");

		TypeDefinition<?> node = typeDefinitionRegistry.getType("Query").get();
		assertTrue(node instanceof ObjectTypeDefinition, "The def is a ObjectTypeDefinition");
		ObjectTypeDefinition query = (ObjectTypeDefinition) node;
		assertEquals("Query", query.getName(), "the object type is a query");
		assertEquals(3, query.getFieldDefinitions().size(), "There are three queries");

		/////////////////////////////////////////////////////////////////////////////////////////////////////// :
		// Verifications for helloWorld

		FieldDefinition fieldDef = query.getFieldDefinitions().get(0);
		assertEquals("helloWorld", fieldDef.getName(), "the query name is helloWorld");
		assertEquals("String", ((TypeName) fieldDef.getType()).getName(), "echo is of type String");
		assertEquals(0, fieldDef.getInputValueDefinitions().size(), "echo has no input");

		/////////////////////////////////////////////////////////////////////////////////////////////////////// :
		// Verifications for echoWithName

		fieldDef = query.getFieldDefinitions().get(1);
		assertEquals("echoWithName", fieldDef.getName(), "the query name is echoWithName");
		assertEquals("String", ((TypeName) fieldDef.getType()).getName(), "echoWithName is of type String");
		assertEquals(1, fieldDef.getInputValueDefinitions().size(), "echoWithName has one input");

		InputValueDefinition inputValueDef = fieldDef.getInputValueDefinitions().get(0);
		assertTrue(inputValueDef.getType() instanceof NonNullType, "The echoWithName parameter is mandatory");
		NonNullType inputType = (NonNullType) inputValueDef.getType();
		assertEquals("String", ((TypeName) inputType.getType()).getName(), "echoWithName is of type String");

		/////////////////////////////////////////////////////////////////////////////////////////////////////// :
		// Verifications for echoWithOptionalName

		fieldDef = query.getFieldDefinitions().get(2);
		assertEquals("echoWithOptionalName", fieldDef.getName(), "the query name is echoWithOptionalName");
		assertEquals("String", ((TypeName) fieldDef.getType()).getName(), "echoWithOptionalName is of type String");
		assertEquals(1, fieldDef.getInputValueDefinitions().size(), "echoWithOptionalName has one input");

		inputValueDef = fieldDef.getInputValueDefinitions().get(0);
		assertTrue(inputValueDef.getType() instanceof TypeName, "The echoWithOptionalName parameter is optional");
		assertEquals("String", ((TypeName) inputValueDef.getType()).getName(),
				"echoWithOptionalName is of type String");
	}

}
