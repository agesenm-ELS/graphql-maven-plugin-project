# GraphQL Java Generator

The GraphQL Java Generator makes it easy to work in Java with graphQL in a schema first approach.

This project is an accelerator to develop __GraphQL clients__ and __GraphQL servers__ in java. 

That is: graphql-java-generator generates the boilerplate code, and lets you concentrate on what's specific to your use case. Then, the running code __doesn't depend on any dependencies from graphql-java-generator__. So you can get rid of graphql-java-generator at any time: just put the generated code in your SCM, and that's it. 

* In __client mode__ : graphql-java-generator generates a class for each query and mutation type (subscriptions are not managed yet). These classes contain the methods to call the queries and mutations. That is, to call the GraphQL server, you just call one of this method.
    * graphql-java-generator also generates the POJOs from the GraphQL schema. The __GraphQL response is stored in these POJOs__, for easy and standard use in Java.
* In __server mode__ : graphql-java-generator generates the whole heart of the GraphQL server. The developer has only to develop request to the data. That is :
    * graphql-java-generator generates the main method (in a jar project) or the main servler (in a war project), and all the Spring wiring, based on [graphql-java-spring](https://github.com/graphql-java/graphql-java-spring), itself being build on top of [graphql-java](https://www.graphql-java.com/).
    * It generates the POJOs with the standard JPA annotations, to make it easy to link with a database. But of course, you can also implement your GraphQL server based on REST resources, or any other kind of data storage.  
    * graphql-java-generator also generates interfaces, named DataFetchersDelegate. It expects a Spring Bean to be defined.
    * The developer just has to implement each DataFetchersDelegate, and the GraphQL server is ready to go!

Please, take a look at the projects that are within the graphql-maven-plugin-samples: they show various ways to implement a GraphQL server, based on the graphql-java library. 

__The interesting part is that graphql-java-generator is just an accelerator: you don't depend on any library from graphql-java-generator__. So, it just helps you to build application based on [graphql-java](https://www.graphql-java.com) . 
If the generated code doesn't fully suit your needs, you can take what's generated as a full sample for graphql-java usage, based on your use case. You can then update the generated code, where it's not compliant for you. And that's it. The only thing, there, is that we would like to know what was not correct for your use case, so that we can embed it into next versions. Or perhaps, if it's just a matter of documentation, to better explain how to use it... 

The generator is currently available as a maven plugin. A Gradle plugin will come soon.


## Aim of this project

The aim of this project is to:

* Hide all the GraphQL technical stuff and boilerplate code
* Let the developer concentrate on his/her specific use case
* Make it __very easy__ to create a GaphQL client, based on the generated POJOs. The calls to the GraphQL server are hidden. The client code just call a generated Java method, with Java parameters.
* Make it __easy__ to create a GraphQL server. The plugin generated the server boilerplate code and the POJOs. But it's still up to the developer to map the GraphQL schema to the database schema. See the provided samples for different ways to do this. The generated POJOs integrate the JPA schema, making the database access easy, thanks to the Spring Data Repositories.
* Let the generated code work as a standalone code. That is: your project, when it runs, doesn't depend on any dependency from graphql-java-generator.

## How to use it

### Samples

You'll find these samples in the project. For all of these samples, there are two projects: the client and the server.

* Basic
    * The simplest samples. Start from them, when you want to start a new project
* StarWars
    * The server is packaged as a war
    * The GraphQL server exposes https
    * The data model is directly compatible with the generated code  
*  Forum
    * The server is packaged as a Spring Boot application
    * The GraphQL server exposes http
    * The server uses the schema personalization, to overcome the default code generation

Note: The client project for the StarWars and Forum samples contains integration tests. They are part of the global build. As such, the client projects contains integration tests that allow to check the graphql-maven-plugin for both the client and the server for these two projects. 

### Client mode

When in _client_ mode, you can query the server with just one line of code.

For instance :

```Java
Human human = queryType.human("{id name appearsIn homePlanet friends{name}}", "180");
```

Where human is a POJO, that you can manipulate as any java object. This POJOs and the QueryType(s) are generated at build time, from the GraphQL schema, by the Maven Plugin. 

A _good idea_ is to prepare the queries at startup time. Doing this executes all the GraphQL query checks during the application startup. And
then, the ObjectResponse for these queries is built only once, instead of each query execution.

To prepare the queries during startup, it can a be a Spring Bean (like below), or a standard class initialization (like in the samples, see the Forum client and StarWars client)

```Java
	@Bean
	ObjectResponse heroResponse(QueryType queryType) {
		return queryType.getHeroResponseBuilder().build();
	}
```
Doing this allows you to use this ObjectResponse, later on, like this: 

```Java
	Character hero = queryType.hero(heroResponse, Episode.NEWHOPE);
```
 
When you want to also query sub-objects of the queried object, you'll have to describe what's to be returned. This is done by calling additional methods to the builder.

In the sample below, the _withQueryResponseDef_ allows you to define the _Character_'s field (as _hero_ query returns a _Character_), and to define all subobjects
that will be queried. In one method call, you can define several level of subobjects. Here, you go down to the friends of the friends of the hero's friends.

With _withQueryResponseDef_, you only defined the content of the response. GraphQL Java Generator will add the query name, and it(s) parameter(s), properly encoded before sending the request to the GraphQL server.

```Java
		// ObjectResponse (preparation of the query). 
		// This will throw a GraphQLRequestPreparationException, if the request is not valid.
		ObjectResponse objectResponse = queryType.getHeroResponseBuilder().
				.withQueryResponseDef(
						"{id appearsIn name friends {name friends {friends{id name appearsIn}}} primaryFunction }")
				.build();

		// Execution of the query. We get the result back in a POJO
		// This will throw a GraphQLExecutionException if an error occurs during execution
		Character hero = queryType.hero(objectResponse, Episode.NEWHOPE);
```
 
You can also use a Builder to generate the query, by adding field by field, and subobject by subobject, like below. Of course, it's more verbose.

```Java
		// ObjectResponse
		ObjectResponse objectResponse = queryType.getHeroResponseBuilder().withField("id").withField("name")
				.withField("appearsIn")
				.withSubObject("friends", ObjectResponse.newSubObjectBuilder(Character.class).withField("name").build())
				.build();

		// Execution of the query. We get the result back in a POJO
		Character hero = queryType.hero(objectResponse, Episode.NEWHOPE);
```
 
_Note: In all cases, an ObjectResponse is built. This will help to add future functionnalities_. For instance, in the near future, when you access a field, the generated code will check that this field has actually been queried.


### Server mode

In Server mode, the maven packaging can be:

* jar. In this case, the generated code is a Spring Boot Application
* war. In this case, the generated code is a war that can be deployed on any modern application server.

See below for more details:


#### Spring Boot application (server mode)

When the package type of the maven module (or project) is _jar_, then this mode is used.

The server mode is more complex, as its subject is to link the GraphQL schema to the underlying data structure. This data structure can vary a lot (relational database with lots of possible physical schema, document database like MongoDB, underlying JSON services...). Of course, the generated code can not guess what's your data structure, and how to access it.

So the GraphQL Java Generator generates:

* The GraphQLServer class, which is the executable Java class. It's actually a Spring Boot application.
* The code expected by the graphql-java library. It depends on no other graphql dependency
* The POJOs, so that you can manipulate your data.
* JPA annotations on the POJO's fields, so that you can work with Spring Data (which uses Hibernate by default) with a minimum of additional work. 
    * If these annotations don't fit your needs, you can add or replace annotations for the JPA entities and their field, through a configuration file. You can find a sample for that in the Forum server sample. Take a look at the src/main/GraphQL/forum_personalization.json file.
    * In the future, we'll open the capability to define your own templates, so that the generated code fits exactly in your needs
* The DataFetchersDelegate interfaces. This is entry point, where you'll define how to access to the underlying data structure. There is a DataFetchersDelegate interface for each object in the schema, that is: for each query, mutation and subscription, and also for each regular object (to fetch the non scalar fields of this object). To find all the DataFetchersDelegate you have to implement, you can either navigate to the generated code, and find all the XxxDataFetchersDelegate java files. Or do it in test and learn: when the GraphQL server starts, it searches for the a spring component for each XxxDataFetchersDelegate generated interface. So it will complains for the missing DataFetchersDelegate (see below).        

Then do a first build :

```
mvn clean install
```

The build will complain about the Data Fetchers Delegate you need to define. 

Two important hints for these Data Fetchers Delegates :

* It must by annotated with _@Component_ (org.springframework.stereotype.Component)
* It must be in the same package or a sub-package of the package where the generated code is (com.generated.graphql by default, or the one you defined). This is necessary, so that Spring find it.

For instance, the _Basic_ server needs this one:

```Java
@Component
public class QueryDataFetchersDelegateImpl implements QueryDataFetchersDelegate {
	@Override
	public String hello(DataFetchingEnvironment dataFetchingEnvironment, String name) {
		return "Hello" + ((name == null) ? "" : " " + name);
	}
}
```



A Data Fetcher Delegate is a class that implements a XxxDataFetcherDelegate. It is responsible to return the data, as specified in the GraphQL schema. Of course, for mutation, it needs to do some complementary work.

__Important:__ a DataFetcherDelegate is a Spring component. That is, in order to Spring to find it, it must implement the @Component Spring stereotype annotation.

The Data Fetcher Delegates are the heart of the Server part.

The short story is this one:

* The code generated by the GraphQL maven plugin directly maps to the entity, thanks to [Spring Data JPA](https://spring.io/projects/spring-data-jpa)'s magic.
* The developer still needs to develop the DataFetchers, to manage the relation between objects (see the samples and below, to see how to do this).

A longer story is this one:

* The GraphQL maven plugin expects a database model that is the same as what's in the GraphQL schema. That is: for each GraphQL object, there is a 
table with the same name, and for each field of each of these GraphQL objects, there is a column with the same name and type. This insure the JPA
access to the database. 
* In GraphQL request, a critical factor for performance is the way to query relations between objects. The [graphql-java site](https://www.graphql-java.com/documentation/v12/batching/)
is very clear on this subject. So the maven plugin mark every relational field as transient, and let the developer manage these relations.

So you have to create the Data Fetchers. Please, take a look at the projets that are within the graphql-maven-plugin-samples: they show various ways to implement a GraphQL server, based on the graphql-java library. The interesting part is that graphql-java-generator is just an accelerator: you don't depend on additional library.  

Then... you're app is ready to go!
:)

#### WAR packaging (server mode)

The StarWars server is a war. You can refer to it, as a working sample.

To package in war mode, it's almost as the previous way, with these differences:
* Of course, you'll have to set the maven package type as _war_
* You'll have to provide the tomcat dependencies, to ensure that the embedded servlet container does not interfere with the servlet container to which the war file is deployed. To do so, add this dependency:

```XML
<dependencies>
	<!-- … -->
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
	</dependency>
	<!-- … -->
</dependencies>
```

## Access to the demo GraphQL server

Once you started the GraphQL server (either Forum, StarWars...), you can access to it with this URL: [http://localhost:8180/graphiql](http://localhost:8180/graphiql). With the graphiql page, you'll be able to test your server, by typing any GraphQL query. You'll have some help to type correct values.

You can access to the H2 Console with this URL: [http://localhost:8180/h2-console/](http://localhost:8180/h2-console/). The jdbc URL to connect to the database is: jdbc:h2:mem:testdb. login: sa, no password.


## State of the project

# Main evolutions for the near future

You'll find below the main changes, that are foreseen in the near future
- Manage Subscriptions. Currently, GraphQL Java Generator manages queries and mutations.
- Add a gradle plugin (work in progress)
- Manage properties which name are java keyword, like: public, private, class... Currently, it would generate a compilation error.
- Manage field parameters. Currently, GraphQL Java Generator accepts parameters out of the query level (that is on object fields), only with Direct Queries (which is nice enough to begin)
- Comments should be reported in the generated code, especially the POJOs and the queries, mutations and subscriptions
- Define specific Scalars (for instance Date, DateTime, Time)
- Fragment in graphql queries
- The plugin currently manages only one schema. It would be nice to allow several graphqls files, with a pattern like /*.graphqls


# Note for contributors

This project is a maven plugin project. 

If you want to compile it, you'll have to add the lombok.jar file in your IDE. Please see the relevant section, in the Install menu of the [https://projectlombok.org/](https://projectlombok.org/) home page. This very nice tools generates all java boiler plate code, like setters, getters, constructors from fields...

If you use eclipse, please use the __code formatter__ given with the project (file _graphql-java-generator (eclipse code formatter).xml_ at the root of the project). This allows to have the sample code formatting: the code is then homogeneous, and the comparison between versions is simpler. To do this, go to the eclipse preferences, select Java/Code Style/Formatter, and import this file. Then, in the Java/Editor/Save Actions, check the "Perform the selected action on save", "Format source code", "Format all lines", "Organize imports" and "Additional actions" which its default content


# License

`graphql-java-generator` is licensed under the MIT License. See [LICENSE](LICENSE.md) for details.
