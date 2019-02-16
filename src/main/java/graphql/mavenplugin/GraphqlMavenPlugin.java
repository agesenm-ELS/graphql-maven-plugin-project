/**
 * 
 */
package graphql.mavenplugin;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.AbstractApplicationContext;

import graphql.mavenplugin.generation.DocumentParser;

/**
 * @author EtienneSF
 */
@Mojo(name = "graphql")
@Configuration
@Import(GraphqlMavenPlugin.class)
public class GraphqlMavenPlugin extends AbstractMojo {

	@Parameter(property = "graphql.outputDirectory", defaultValue = "target/generated-sources/graphql-client")
	private File outputDirectory;

	@Parameter(property = "graphql.basePackage", defaultValue = "com.generated.graphql")
	private String basePackage;

	@Parameter(property = "graphql.encoding", defaultValue = "UTF-8")
	private String encoding;

	/** The Spring IoC container */
	AbstractApplicationContext ctx;

	public void execute() throws MojoExecutionException, MojoFailureException {
		try {

			getLog().info("Starting generation of java classes from graphqls files");

			// We'll use Spring IoC
			ctx = new AnnotationConfigApplicationContext(getClass());
			DocumentParser documentParser = ctx.getBean(DocumentParser.class);
			int nbClasses = documentParser.parseDocuments();
			ctx.close();

			getLog().info(nbClasses + "java classes have been generated from graphqls files");

		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Let's give access to all the maven parameters as spring bean

	@Bean
	String basePackage() {
		return basePackage;
	}

	@Bean
	String encoding() {
		return encoding;
	}

	@Bean
	Log log() {
		return getLog();
	}

	@Bean
	File outputDirectory() {
		return outputDirectory;
	}
}
