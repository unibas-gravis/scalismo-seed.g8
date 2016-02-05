package ch.unibas.cs.gravis.scalismo

import scalismo.image.{DiscreteImageDomain, DiscreteScalarImage}
import scalismo.io.MeshIO
import scalismo.ui.api.SimpleAPI.ScalismoUI
import java.io.File

object ExampleApp {

  def main(args: Array[String]) {
    
    // required to initialize native libraries (VTK, HDF5 ..)
    scalismo.initialize()
    
    // Your application code goes below here. Below is a dummy application that reads a mesh and displays it

    // create a visualization window
    val ui = ScalismoUI()

    // read a mesh from file
    val mesh = MeshIO.readMesh(new File("data/facemesh.stl")).get

    // display it
    ui.show(mesh, "face")

  }
}
