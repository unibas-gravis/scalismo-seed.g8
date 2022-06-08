import sbt.Resolver

organization  := "ch.unibas.cs.gravis"

name := "$name$"

version       := "0.1"

scalaVersion  := "3.1.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers ++= Seq(
  Opts.resolver.sonatypeSnapshots
)


libraryDependencies  ++= Seq(
            "ch.unibas.cs.gravis" %% "scalismo-ui" % "0.91.0"
)

assembly/assemblyJarName := "$name$.jar"

assembly/mainClass  := Some("example.ExampleApp")

assembly/assemblyMergeStrategy :=  {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", s) if s.endsWith(".SF") || s.endsWith(".DSA") || s.endsWith(".RSA") => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
}
