package com.graphql_java_generator.client.domain.allGraphQLCases;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;


/**
 * @author generated by graphql-java-generator
 * @see <a href="https://github.com/graphql-java-generator/graphql-java-generator">https://github.com/graphql-java-generator/graphql-java-generator</a>
 */
public class MyQueryTypeWithoutParameters {

	@JsonDeserialize(contentAs = CharacterImpl.class)
	@JsonProperty("withoutParameters")
	List<Character> withoutParameters;

	public void setWithoutParameters(List<Character> withoutParameters) {
		this.withoutParameters = withoutParameters;
	}

	public List<Character> getWithoutParameters() {
		return withoutParameters;
	}
	
    public String toString() {
        return "MyQueryTypeWithoutParameters {withoutParameters: " + withoutParameters + "}";
    }
}
