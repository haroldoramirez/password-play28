lazy val root = (project in file("."))
  .enablePlugins(PlayJava, PlayEbean)
  .settings(
    name := "password-play28",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.13.1",
    libraryDependencies ++= Seq(
      guice,
      jdbc,
      evolutions,
      "com.h2database" % "h2" % "1.4.199",
      "org.awaitility" % "awaitility" % "3.1.6" % Test,
      "org.assertj" % "assertj-core" % "3.12.2" % Test,
      "org.mockito" % "mockito-core" % "3.0.0" % Test,
      // To provide an implementation of JAXB-API, which is required by Ebean.
      "javax.xml.bind" % "jaxb-api" % "2.3.1",
      "javax.activation" % "activation" % "1.1.1",
      "org.glassfish.jaxb" % "jaxb-runtime" % "2.3.2",
      //Customs encrypt passwords
      "org.mindrot" % "jbcrypt" % "0.4",
      "org.postgresql" % "postgresql" % "42.2.12"

    ),
    testOptions in Test += Tests.Argument(TestFrameworks.JUnit, "-a", "-v"),
    javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")
  )
