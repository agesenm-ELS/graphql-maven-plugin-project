/**
 * 
 */
package graphql.mavenplugin.language.impl;

import java.util.List;

import graphql.mavenplugin.language.Field;
import graphql.mavenplugin.language.Type;
import lombok.Data;

/**
 * @author EtienneSF
 */
@Data
public class ScalarType implements Type {

	/** The name of the GraphQL type */
	final String name;

	/** The package where the Java type for this class is stored */
	final String packageName;

	/** The simple name for this class */
	final String classSimpleName;

	/**
	 * 
	 * @param name
	 *            The name of the GraphQL type
	 * @param packageName
	 *            The package where the Java type for this class is stored
	 * @param classSimpleName
	 *            The simple name for this class
	 */
	public ScalarType(String name, String packageName, String classSimpleName) {
		this.name = name;
		this.packageName = packageName;
		this.classSimpleName = classSimpleName;
	}

	/**
	 * Indicates whether this type is predefined (as Integer or String), or not. If not, it has a specific class
	 * defining it.
	 */
	boolean preDefined;

	public GraphQlType getGraphQlType() {
		return GraphQlType.SCALAR;
	}

	@Override
	public String getClassFullName() {
		return packageName + "." + classSimpleName;
	}

	@Override
	public String getConcreteClassSimpleName() {
		return getClassSimpleName();
	}

	@Override
	public boolean isJPAEntity() {
		return false;
	}

	/**
	 * An enum has no fields.
	 * 
	 * @return null
	 */
	@Override
	public List<Field> getFields() {
		return null;
	}

}