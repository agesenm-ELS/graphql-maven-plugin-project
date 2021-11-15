package com.graphql_java_generator.plugin.generate_code;

import com.graphql_java_generator.plugin.conf.CustomScalarDefinition;
import com.graphql_java_generator.util.GraphqlUtils;

import graphql.schema.GraphQLScalarType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * This class contains the data that allows to generate a custom Jackson serializer. These serializers are used to
 * serialize the parameters in the outgoing requests. The serializers exist only for what's not JSON standard, that is:
 * GraphQL Custom Scalars. In the generated code, there is one serializer for each list depth level for this type, that
 * has been encountered in the GraphQL schema.<BR/>
 * For instance <code>Date</code> is not a list, and thus has a depth level of 0, whereas <code>[[Date]]</code> is a
 * list of lists, and thus has a depth level of 2.
 * 
 * @author etienne-sf
 */
@Data
@AllArgsConstructor
public class CustomSerializer {

	/** The name of the GraphQL type is used to name the java class for the serialization */
	private String graphQLTypeName;

	/**
	 * The Java type (for instance java.lang.String), that represent the GraphQL type.<BR/>
	 * For instance, for a GraphQLType <I>[[String]]</I>, the <I>javaClassFullName</I> would contain
	 * <I>List<List<java.lang.String>></I>
	 */
	private String itemJavaClassFullName;

	/**
	 * The {@link CustomScalarDefinition} that contains the information to declare the {@link GraphQLScalarType}.
	 */
	private CustomScalarDefinition customScalarDefinition;

	/**
	 * Indicates at which level of nested array this custom deserializer is. To deserialize a value (for custom scalar),
	 * the <I>listLevel</I> is 0. To deserialize a <I>[[Character]]</I>, the <I>listLevel</I> is 2.
	 */
	private int listDepth;

	/**
	 * Returns the simple name for the serializer class.
	 * 
	 * @return The simple name looks like this: <I>ListListDate</I>, where:
	 *         <UL>
	 *         <LI>ListList shows that this custom scalar is a list serializer. It reads items that are at level 2 of
	 *         nested GraphQL arrays (= Java list).</LI>
	 *         <LI>Date is the GraphQL type name that is managed by this serializer</LI>
	 *         </UL>
	 */
	public String getClassSimpleName() {
		return getCustomSerializerClassSimpleName(listDepth, GraphqlUtils.graphqlUtils.getJavaName(graphQLTypeName));
	}

	/**
	 * Returns The Java type (for instance java.lang.String), that represent the GraphQL type.<BR/>
	 * For instance, for a GraphQLType <I>[[String]]</I>, the <I>javaClassFullName</I> would contain
	 * <I>List<List<java.lang.String>></I>
	 * 
	 * @return
	 */
	public String getJavaClassFullName() {
		String ret = itemJavaClassFullName;
		for (int i = 1; i <= listDepth; i += 1) {
			ret = "List<" + ret + ">";
		}
		return ret;
	}

	/**
	 * Standard utility to calculate a Custom Serializer name. Used in this class, and to define the Jackson annotation
	 * on the field
	 * 
	 * @param listLevel
	 *            Indicates at which level of nested array this custom serializer is. To serialize a value (for custom
	 *            scalar), the <I>listLevel</I> is 0. To serialize a <I>[[Character]]</I>, the <I>listLevel</I> is 2.
	 * @param itemClassSimpleName
	 *            The class simple name of the item of the list. For instance, for a field that is
	 *            <I>List&lt;List&lt;Date&gt;&gt;</I>, the <I>itemClassSimpleName</I> would be <I>Date</I>
	 * @return
	 */
	public static String getCustomSerializerClassSimpleName(int listLevel, String graphQLTypeName) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < listLevel; i += 1) {
			sb.append("List");
		}
		sb.append(graphQLTypeName);
		return sb.toString();

	}
}
