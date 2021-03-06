import sbt._

object Dependencies {

    val slf4jVersion = "1.7.20"
    val logbackVersion = "1.1.7"
    val jacksonVersion = "2.9.9"
    val betterFilesVersion = "3.8.0"
    val dartCommonsVersion = "3.0.27"
    val jsonSchemaVersion = "1.5.1"

    val logging = Seq( "org.slf4j" % "slf4j-api" % slf4jVersion,
                       "ch.qos.logback" % "logback-classic" % logbackVersion )

    val jackson = Seq( "com.fasterxml.jackson.module" %% "jackson-module-scala" % jacksonVersion,
                       "com.fasterxml.jackson.datatype" % "jackson-datatype-jsr310" % jacksonVersion )

    val betterFiles = Seq( "com.github.pathikrit" %% "better-files" % betterFilesVersion )

    val jsonValidator = Seq( "org.everit.json" % "org.everit.json.schema" % jsonSchemaVersion )

    val dartCommons = Seq( "com.twosixlabs.dart" %% "dart-utils" % dartCommonsVersion,
                           "com.twosixlabs.dart" %% "dart-exceptions" % dartCommonsVersion,
                           "com.twosixlabs.dart" %% "dart-test-base" % dartCommonsVersion % Test )

    val dartCommonsJson = Seq( "com.twosixlabs.dart" %% "dart-json" % dartCommonsVersion )

}
