/** Generated by the default template from graphql-java-generator */
package com.graphql_java_generator.domain.client.forum;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.graphql_java_generator.annotation.GraphQLInputType;
import com.graphql_java_generator.annotation.GraphQLScalar;
import com.graphql_java_generator.client.GraphQLObjectMapper;
import com.graphql_java_generator.exception.GraphQLRequestExecutionException;

/**
 *
 * @author generated by graphql-java-generator
 * @see <a href=
 *      "https://github.com/graphql-java-generator/graphql-java-generator">https://github.com/graphql-java-generator/graphql-java-generator</a>
 */
@GraphQLInputType("TopicPostInput")
public class TopicPostInput {

	/**
	 * This map contains the deserialiazed values for the alias, as parsed from the json response from the GraphQL
	 * server. The key is the alias name, the value is the deserialiazed value (taking into account custom scalars,
	 * lists, ...)
	 */
	@com.graphql_java_generator.annotation.GraphQLIgnore
	Map<String, Object> aliasValues = new HashMap<>();

	public TopicPostInput() {
		// No action
	}

	@JsonProperty("authorId")
	@GraphQLScalar(fieldName = "authorId", graphQLTypeSimpleName = "ID", javaClass = String.class)
	String authorId;

	@JsonProperty("date")
	@GraphQLScalar(fieldName = "date", graphQLTypeSimpleName = "Date", javaClass = Date.class)
	@JsonSerialize(using = CustomJacksonSerializers.Date.class)
	Date date;

	@JsonProperty("publiclyAvailable")
	@GraphQLScalar(fieldName = "publiclyAvailable", graphQLTypeSimpleName = "Boolean", javaClass = Boolean.class)
	Boolean publiclyAvailable;

	@JsonProperty("title")
	@GraphQLScalar(fieldName = "title", graphQLTypeSimpleName = "String", javaClass = String.class)
	String title;

	@JsonProperty("content")
	@GraphQLScalar(fieldName = "content", graphQLTypeSimpleName = "String", javaClass = String.class)
	String content;

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	public void setPubliclyAvailable(Boolean publiclyAvailable) {
		this.publiclyAvailable = publiclyAvailable;
	}

	public Boolean getPubliclyAvailable() {
		return publiclyAvailable;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	/**
	 * This method is called during the json deserialization process, by the {@link GraphQLObjectMapper}, each time an
	 * alias value is read from the json.
	 * 
	 * @param aliasName
	 * @param aliasDeserializedValue
	 */
	public void setAliasValue(String aliasName, Object aliasDeserializedValue) {
		aliasValues.put(aliasName, aliasDeserializedValue);
	}

	/**
	 * Retrieves the value for the given alias, as it has been received for this object in the GraphQL response. <BR/>
	 * This method <B>should not be used for Custom Scalars</B>, as the parser doesn't know if this alias is a custom
	 * scalar, and which custom scalar to use at deserialization time. In most case, a value will then be provided by
	 * this method with a basis json deserialization, but this value won't be the proper custom scalar value.
	 * 
	 * @param alias
	 * @return
	 * @throws GraphQLRequestExecutionException
	 *             If the value can not be parsed
	 */
	public Object getAliasValue(String alias) throws GraphQLRequestExecutionException {
		Object value = aliasValues.get(alias);
		if (value instanceof GraphQLRequestExecutionException)
			throw (GraphQLRequestExecutionException) value;
		else
			return value;
	}

	@Override
	public String toString() {
		return "TopicPostInput {" + "authorId: " + authorId + ", " + "date: " + date + ", " + "publiclyAvailable: "
				+ publiclyAvailable + ", " + "title: " + title + ", " + "content: " + content + "}";
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder
	 */
	public static class Builder {
		private String authorId;
		private Date date;
		private Boolean publiclyAvailable;
		private String title;
		private String content;

		public Builder withAuthorId(String authorId) {
			this.authorId = authorId;
			return this;
		}

		public Builder withDate(Date date) {
			this.date = date;
			return this;
		}

		public Builder withPubliclyAvailable(Boolean publiclyAvailable) {
			this.publiclyAvailable = publiclyAvailable;
			return this;
		}

		public Builder withTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder withContent(String content) {
			this.content = content;
			return this;
		}

		public TopicPostInput build() {
			TopicPostInput _object = new TopicPostInput();
			_object.setAuthorId(authorId);
			_object.setDate(date);
			_object.setPubliclyAvailable(publiclyAvailable);
			_object.setTitle(title);
			_object.setContent(content);
			return _object;
		}
	}
}
