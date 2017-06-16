organization  := "ch.unibas.cs.gravis"

name := """minimal-scalismo-seed"""
version       := "0.5"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += Resolver.bintrayRepo("unibas-gravis", "maven")

resolvers += Opts.resolver.sonatypeSnapshots


libraryDependencies  ++= Seq(
            "ch.unibas.cs.gravis" %% "scalismo" % "0.15.+",
            "ch.unibas.cs.gravis" % "scalismo-native-all" % "3.0.+",
            "ch.unibas.cs.gravis" %% "scalismo-ui" % "0.11.+"
)

assemblyJarName in assembly := "exectuable.jar"

mainClass in assembly := Some("com.example.ExampleApp")


assemblyMergeStrategy in assembly <<= (assemblyMergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
    case PathList("META-INF", s) if s.endsWith(".SF") || s.endsWith(".DSA") || s.endsWith(".RSA") => MergeStrategy.discard
    case "reference.conf" => MergeStrategy.concat
    case _ => MergeStrategy.first
  }
}
