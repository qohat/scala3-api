import sbt._

object Dependencies {
    object Version {
        val zio = "2.0.0-M2"
        val betterMonadicFor = "0.3.1"
        val kindProjector    = "0.13.2"
        val logback          = "1.2.6"
        val organizeImports  = "0.5.0"
        val semanticDB       = "4.4.30"
    }

    object Libraries {
        def zioDep(artifact: String): ModuleID = "dev.zio" %% artifact % Version.zio

        val zio    = zioDep("zio")
        val zioStreams = zioDep("zio-streams")
        val organizeImports = "com.github.liancheng" %% "organize-imports" % Version.organizeImports
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