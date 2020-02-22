package com.graphql_java_generator.client.domain.allGraphQLCases;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import com.graphql_java_generator.annotation.GraphQLInputParameters;
import com.graphql_java_generator.annotation.GraphQLInputType;
import com.graphql_java_generator.annotation.GraphQLNonScalar;
import com.graphql_java_generator.annotation.GraphQLScalar;

import java.util.Date;

/**
 * @author generated by graphql-java-generator
 * @see <a href="https://github.com/graphql-java-generator/graphql-java-generator">https://github.com/graphql-java-generator/graphql-java-generator</a>
 */
@GraphQLInputType
public class AllFieldCasesWithoutIdSubtypeInput  {

	@JsonProperty("name")
	@GraphQLScalar(graphQLTypeName = "String", javaClass = String.class)
	String name;



	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

    public String toString() {
        return "AllFieldCasesWithoutIdSubtypeInput {"
				+ "name: " + name
        		+ "}";
    }
}
