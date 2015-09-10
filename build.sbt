organization  := "ch.unibas.cs.gravis"

name := """minimal-scalismo-seed"""
version       := "0.3"

scalaVersion  := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

resolvers += "Statismo (public)" at "http://shapemodelling.cs.unibas.ch/repository/public"  

resolvers += Opts.resolver.sonatypeSnapshots 


libraryDependencies  ++= Seq(
            "ch.unibas.cs.gravis" %% "scalismo" % "0.9.+",
            "ch.unibas.cs.gravis" % "scalismo-native-all" % "3.0.+",
            "ch.unibas.cs.gravis" %% "scalismo-ui" % "0.5.+" 
)


