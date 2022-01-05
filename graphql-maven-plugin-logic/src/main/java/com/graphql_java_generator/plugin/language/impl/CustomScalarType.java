/**
 * 
 */
package com.graphql_java_generator.plugin.language.impl;

import com.graphql_java_generator.plugin.DocumentParser;
import com.graphql_java_generator.plugin.conf.CommonConfiguration;
import com.graphql_java_generator.plugin.conf.CustomScalarDefinition;
import com.graphql_java_generator.plugin.generate_code.GenerateCodePluginExecutor;
import com.graphql_java_generator.plugin.language.CustomScalar;
import com.graphql_java_generator.util.GraphqlUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author etienne-sf
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CustomScalarType extends ScalarType implements CustomScalar {

	final CustomScalarDefinition customScalarDefinition;

	final GraphqlUtils graphqlUtils = GraphqlUtils.graphqlUtils;

	/**
	 * 
	 * @param name
	 *            The name of the GraphQL type
	 * @param configuration
	 *            The current plugin configuration, which is accessible through an interface that extends
	 *            {@link CommonConfiguration}
	 * @param documentParser
	 *            The {@link DocumentParser} that has parsed the schema, and so that contains the whole schema
	 *            definition
	 * @see CustomScalarDefinition
	 */
	public CustomScalarType(CustomScalarDefinition customScalarDefinition, CommonConfiguration configuration,
			DocumentParser documentParser) {
		super(customScalarDefinition.getGraphQLTypeName(),
				GraphqlUtils.graphqlUtils.getPackageName(customScalarDefinition.getJavaType()),
				GraphqlUtils.graphqlUtils.getClassSimpleName(customScalarDefinition.getJavaType()), configuration,
				documentParser);
		this.customScalarDefinition = customScalarDefinition;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isCustomScalar() {
		return true;
	}

	/** Get the filename where this type must be created. Default is to return the name for the Type */
	@Override
	public String getTargetFileName(String fileType) {
		if (GenerateCodePluginExecutor.FILE_TYPE_JACKSON_DESERIALIZER.equals(fileType)) {
			return "CustomScalarDeserializer" + getName();
		} else {
			throw new RuntimeException("Unknown file type: '" + fileType + "'");
		}

	}
}
