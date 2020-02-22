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

public class _break  {

	@GraphQLInputParameters(names = {"test", "if"}, types = {"extends", "else"})
	@JsonProperty("case")
	@GraphQLScalar(graphQLTypeName = "extends", javaClass = _extends.class)
	_extends _case;



	public void setCase(_extends _case) {
		this._case = _case;
	}

	public _extends getCase() {
		return _case;
	}

    public String toString() {
        return "_break {"
				+ "_case: " + _case
        		+ "}";
    }
}
