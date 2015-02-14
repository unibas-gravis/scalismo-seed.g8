package ch.unibas.cs.gravis.scalismo

import scalismo.ui.Scene
import scalismo.ui.swing.ScalismoApp
import scalismo.ui.swing.ScalismoFrame
import scalismo.ui.util.SimpleAPI._
import scalismo.geometry._
import scalismo.image.DiscreteImageDomain
import scalismo.image.DiscreteScalarImage

class ExampleApp(scene: Scene) extends ScalismoFrame(scene) {

  implicit val s = scene

  // Your application code goes here. Below is a dummy application that creates a layered image and displays it  
  
  val imageDomain = DiscreteImageDomain(Point(0f, 0f, 0f), Vector(1f, 1f, 1f), Index(100, 100, 100))
 
  val values = imageDomain.points.zipWithIndex.map { case (p, i) =>
    if (imageDomain.index(i)(0) % 10 < 3 && imageDomain.index(i)(1) % 10 < 3) 200 else 0
  }
  
  val image = DiscreteScalarImage(imageDomain, values.toArray)
  show(image, "grid")
}

object ExampleApp {

  def main(args: Array[String]) {
    ScalismoApp(args, frame = { s: Scene => new ExampleApp(s) })
  }
}