package ch.unibas.cs.gravis.scalismo

import scalismo.common._
import scalismo.geometry._
import scalismo.image.{DiscreteImageDomain, DiscreteScalarImage}
import scalismo.ui.api.SimpleAPI.ScalismoUI


object ExampleApp {

  def main(args: Array[String]) {
    
    // Your application code goes here. Below is a dummy application that creates a layered image and displays it  
    scalismo.initialize()
    
    val imageDomain = DiscreteImageDomain(Point(0f, 0f, 0f), Vector(1f, 1f, 1f), IntVector(100, 100, 100))

    val values = imageDomain.points.zipWithIndex.map {
      case (p, i) =>
        if (imageDomain.index(PointId(i))(0) % 10 < 3 && imageDomain.index(PointId(i))(1) % 10 < 3) 200 else 0
    }
    val image = DiscreteScalarImage(imageDomain, ScalarArray(values.toArray))

    // create a visualization window 
    val ui = ScalismoUI()
    ui.show(image, "grid")

  }
}
