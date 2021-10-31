/**
 * 
 */
package com.graphql_java_generator.mavenplugin;

import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import com.graphql_java_generator.plugin.DocumentParser;
import com.graphql_java_generator.util.GraphqlUtils;

import graphql.ThreadSafe;

/**
 * <P>
 * The <I>generatePojo</I> goal generates all the java objects that match the provided GraphQL schema. It allows to work
 * in Java with graphQL, in a schema first approach.
 * </P>
 * This goal generates:
 * <UL>
 * <LI>One java interface for each GraphQL `union` and `interface`</LI>
 * <LI>One java class for each GraphQL `type` and `input` type, including the query, mutation and subscription (if any).
 * If the GraphQL type implements an interface, then its java class implements this same interface</LI>
 * <LI>One java enum for each GraphQL enum</LI>
 * </UL>
 * 
 * <P>
 * Every class, interface and their attributes are marked with the annotation from the <A HREF=
 * "https://graphql-maven-plugin-project.graphql-java-generator.com/graphql-java-runtime/apidocs/com/graphql_java_generator/annotation/package-summary.html">GraphQL
 * annotation</A> package. This allows to retrieve the GraphQL information for every class, interface and attribute, at
 * runtime.
 * 
 * <P>
 * It can run in two modes (see the <A HREF=
 * "https://graphql-maven-plugin-project.graphql-java-generator.com/graphql-maven-plugin/generatePojo-mojo.html#mode">mode
 * plugin parameter</A> for more information):
 * </P>
 * <UL>
 * <LI><B>server</B>: In the server mode, only the GraphQL annotation are added. You can add the JPA annotation, with
 * the <I>generateJPAAnnotation</I> plugin parameter set to true. In this mode, as with the <I>generateServerCode</I>,
 * you need to either add the <I>graphql-java-server-dependencies</I> dependencies, or set the <I>copyRuntimeSources</I>
 * plugin parameter to false and add the <I>graphql-java-runtime</I>.</LI>
 * <LI><B>client</B>: The client mode is the default one. This mode generates the same POJO as in server mode, with the
 * addition of the <A HREF="https://github.com/FasterXML/jackson">Jackson</A> annotations. These annotations allows to
 * serialize and unserialize the GraphQL POJO to and from JSON. And the <I>CustomJacksonDeserializers</I> utility class
 * is generated, that allows to deserialize custom scalars and arrays. In this mode, as with the
 * <I>generateServerCode</I>, you need to either add the <I>graphql-java-client-dependencies</I> dependencies, or set
 * the <I>copyRuntimeSources</I> plugin parameter to false and add the <I>graphql-java-runtime</I>.</LI>
 * </UL>
 * <P>
 * <U>Note:</U> in this mode, <I>copyRuntimeSources</I> must be set to false. Some dependencies must be copied for the
 * code to compile (exceptions, annotations...)
 * </P>
 * 
 * @author etienne-sf
 */
@Mojo(name = "generatePojo", defaultPhase = LifecyclePhase.GENERATE_SOURCES, requiresProject = true)
@ThreadSafe
// No need to add the @Component spring annotation: AbstractCommonMojo added this instance into the spring context, to
// use the instance which attributes has been set with the pom content
public class GeneratePojoMojo extends AbstractGeneratePojoMojo {

	// All the Mojo parameters are defined in the AbstractXxxx classes, that contains the contain the hierarchical
	// structure of the Maven goals.
	// See the explanation in the AbstractCommonMojo for more details.

	@Configuration
	@Import({ JacksonAutoConfiguration.class })
	@ComponentScan(basePackageClasses = { DocumentParser.class, GraphqlUtils.class }, //
			excludeFilters = { @Filter(type = FilterType.REGEX, pattern = ".*\\.GenerateClientCode.*"),
					@Filter(type = FilterType.REGEX, pattern = ".*\\.GenerateGraphQLSchema.*"),
					@Filter(type = FilterType.REGEX, pattern = ".*\\.GenerateServerCode.*") })
	public class SpringConfiguration {

	}

	public GeneratePojoMojo() {
		super(SpringConfiguration.class);
	}

}
