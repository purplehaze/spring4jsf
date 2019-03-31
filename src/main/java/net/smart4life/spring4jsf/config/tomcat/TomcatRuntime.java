package net.smart4life.spring4jsf.config.tomcat;

/**
 * Tomcat runtime environment.
 *
 * @author Marcelo Fernandes
 */
public enum TomcatRuntime  {
	/**
	 * Runtime jar inside IDE.
	 */
	UNPACKAGED_JAR,
	/**,
	 * Runtime jar testing or war testing.
	 */
	TEST,
	/**
	 * Runtime java -jar jar packaging.
	 */
	UBER_JAR,
	/**
	 * Runtime java -jar war packaging.
	 */
	UBER_WAR,
	/**
	 * Runtime war inside tomcat servlet container.
	 */
	UNPACKAGED_WAR
}
