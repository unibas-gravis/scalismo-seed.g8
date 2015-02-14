organization  := "ch.unibas.cs.gravis"

name := """minimal-scalismo-seed"""
version       := "0.1"

scalaVersion  := "2.10.4"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Statismo (public)" at "http://shapemodelling.cs.unibas.ch/repository/public"  

resolvers += Opts.resolver.sonatypeSnapshots 


libraryDependencies  ++= Seq(
            "ch.unibas.cs.gravis" %% "scalismo" % "0.7.0",
            "ch.unibas.cs.gravis" % "scalismo-native-all" % "2.0.+",
            "ch.unibas.cs.gravis" %% "scalismo-ui" % "develop-SNAPSHOT" 
)


seq(Revolver.settings: _*)
