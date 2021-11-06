import sbt._

object Dependencies {
    object Version {
        val cats = "3.2.9"
        val http4s = "0.23.6"
        val circe = "0.14.1"
        val derevo        = "0.12.6"
        val newtype       = "0.4.4"
        val refined       = "0.9.27"
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
        def derevo(artifact: String): ModuleID = "tf.tofu"    %% s"derevo-$artifact" % Version.derevo

        val catsEffect = cats("cats-effect") 
        
        val http4sDsl = http4s("http4s-dsl")
        val http4sBlaze = http4s("http4s-blaze-server")
        val http4sCirce = http4s("http4s-circe")

        val circeCore = circe("circe-core")
        val circeGeneric = circe("circe-generic")
        val circeParser = circe("circe-parser")

        val log4cats = "org.typelevel" %% "log4cats-slf4j" % Version.log4cats
        val organizeImports = "com.github.liancheng" %% "organize-imports" % Version.organizeImports

        val derevoCore  = derevo("core")
        val derevoCats  = derevo("cats")
        val derevoCirce = derevo("circe-magnolia")

        val refinedCore = "eu.timepit" %% "refined"      % Version.refined
        val refinedCats = "eu.timepit" %% "refined-cats" % Version.refined
        val newtype  = "io.estatico"   %% "newtype"        % Version.newtype

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