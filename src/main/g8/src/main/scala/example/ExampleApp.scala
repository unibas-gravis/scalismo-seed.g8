package example

import java.awt.Color
import java.io.File

import scalismo.io.MeshIO
import scalismo.ui.api.ScalismoUI
import scalismo.utils.Random.FixedSeed

object ExampleApp extends App:

  // required to initialize native libraries (VTK, HDF5 ..)
  scalismo.initialize()

  // Your application code goes below here. Below is a dummy application that reads a mesh and displays it

  // create a visualization window
  val ui = ScalismoUI()

  // read a mesh from file
  val mesh = MeshIO.readMesh(new File("data/facemesh.ply")).get

  // display it
  val meshView = ui.show(mesh, "face")

  // change its color
  meshView.color = Color.PINK
  
