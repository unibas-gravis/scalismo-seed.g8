import sbt.Resolver

organization  := "ch.unibas.cs.gravis"

name := "$name$"

version       := "0.1"

scalaVersion  := "3.3.0"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  Opts.resolver.sonatypeSnapshots
)

libraryDependencies  ++= Seq(
            "ch.unibas.cs.gravis" %% "scalismo-ui" % "0.92.0"
)

assembly/assemblyJarName := "$name$.jar"
assembly/mainClass  := Some("example.ExampleApp")
assembly / assemblyMergeStrategy := {
      case PathList("META-INF", xs @ _*) =>
        (xs map { _.toLowerCase }) match {
          case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) =>
            MergeStrategy.discard
          case ps @ (x :: xs) if ps.last.endsWith(".sf") || ps.last.endsWith(".dsa") =>
            MergeStrategy.discard
          case "services" :: xs =>
            MergeStrategy.filterDistinctLines
          case _ => MergeStrategy.first
        }
      case _ => MergeStrategy.first
    }
