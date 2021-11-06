import sbt._

object Dependencies {
    object Version {
        val cats = "3.2.9"
        val http4s = "0.23.6"
        val circe = "0.14.1"
        val log4cats = "2.1.1"
        val betterMonadicFor = "0.3.1"
        val kindProjector    = "0.13.2"
        val logback          = "1.2.6"
        val organizeImports  = "0.5.0"
        val semanticDB       = "4.4.30"
    }

    object Libraries {
        def cats(artifact: String): ModuleID = "org.typelevel" %% artifact % Version.cats withSources() withJavadoc()
        def http4s(artifact: String): ModuleID = "org.http4s" %% artifact % Version.http4s
        def circe(artifact: String): ModuleID = "io.circe" %% artifact % Version.circe

        val catsEffect = cats("cats-effect") 
        val http4sDsl = http4s("http4s-dsl")
        val http4sBlaze = http4s("http4s-blaze-server")
        val http4sCirce = http4s("http4s-circe")
        val circeCore = circe("circe-core")
        val circeGeneric = circe("circe-generic")
        val circeParser = circe("circe-parser")

        val log4cats = "org.typelevel" %% "log4cats-slf4j" % Version.log4cats
        val organizeImports = "com.github.liancheng" %% "organize-imports" % Version.organizeImports

        // Runtime
        val logback = "ch.qos.logback" % "logback-classic" % Version.logback
    }

    object CompilerPlugin {
        val betterMonadicFor = compilerPlugin(
        "com.olegpy" %% "better-monadic-for" % Version.betterMonadicFor
        )
        val kindProjector = compilerPlugin(
        "org.typelevel" % "kind-projector" % Version.kindProjector cross CrossVersion.full
        )
        val semanticDB = compilerPlugin(
        "org.scalameta" % "semanticdb-scalac" % Version.semanticDB cross CrossVersion.full
        )
    }

}