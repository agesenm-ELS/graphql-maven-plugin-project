package com.graphql_java_generator.plugin.test.helper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.graphql_java_generator.plugin.conf.GenerateGraphQLSchemaConfiguration;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author etienne-sf
 */
@Getter
@Setter
public class GenerateGraphQLSchemaConfigurationTestHelper implements GenerateGraphQLSchemaConfiguration {

	// All getters are generated thanks to Lombok, see the '@Getter' class annotation
	public Logger logger;

	public boolean addRelayConnections = false;
	public String packageName = "my.test.package";
	public int maxTokens = 15000;
	public File projectDir = new File("./graphql-maven-plugin-logic");
	public File schemaFileFolder = null;
	public String schemaFilePattern = null;
	// As the GraphQL schema won't change, and we always want to regenerate the schema, we won't skip it
	public boolean skipGenerationIfSchemaHasNotChanged = false;
	public String resourceEncoding = "UTF-8";
	public File targetFolder = null;
	public String targetSchemaFileName = null;
	public Map<String, String> templates = new HashMap<String, String>();

	/**
	 * @param caller
	 *            Used to retrieve the appropriate Log4j logger
	 */
	public GenerateGraphQLSchemaConfigurationTestHelper(Object caller) {
		logger = LoggerFactory.getLogger(caller.getClass());
	}

}
