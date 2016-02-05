
## Outline

In this quickstart guide, we will guide you through the task of creating shape models using Gaussian processes, and to fit this model to a mesh using scalismo's registration framework. 

## How to run the code below 

The example code below can be run in two ways:

* To run in the REPL: run "sbt console" from the project's root and copy paste the bits of code below into the REPL

* To run in a standalone application: copy the bits of code below into a file similar to [this example](https://github.com/unibas-gravis/activator-scalismo-seed/blob/master/src/main/scala/ch/unibas/cs/gravis/scalismo/ExampleApp.scala).

## Preparing the environment

We start by importing the most common packages that we will always need when working with Scalismo

```tut:silent
import scalismo.common._
import scalismo.geometry._
```
(We will do the import of more specific packages later to make it more clear to which package an object or function belongs).

Scalismo internally depends on [HDF5](http://www.hdfgroup.org/) and [VTK](http://www.vtk.org), which are native (non-jvm) libraries. In order to load these libraries properly, we need to call
```tut:silent
scalismo.initialize()
```
as a first command. 

Now we are ready to start up the visualization tool.
```tut:silent
import scalismo.ui.api.SimpleAPI
val ui = SimpleAPI.ScalismoUI()
```

## Loading and visualizing a mesh

First, we show how to build a Gaussian process shape model. 
As a first step, we load a mesh from file
```tut:silent
import scalismo.io._
import java.io.File

val referenceMesh = MeshIO.readMesh(new File("data/facemesh.stl")).get
```

The get function at the end of the line is there because ```readMesh``` could fail. By calling get, we indicate that we are not interested in handling possible errors and just want retrieve the result. If the result is not available, an exception is thrown.

To visualize the mesh that we just loaded we call
```tut:silent
ui.show(referenceMesh, "referenceMesh")
```
The second argument is the name of the object that is created in the graphical user interface. 

*You can change the visualization properties of this object by selecting the object in the scene tree of the gui. To remove or make an object invisible, right click on an object in the scene tree.*

## Building a Gaussian process shape model
Now we are ready to build a gaussian process model. We create a new Gaussian process that models 3D vector fields. We define the mean of the Gaussian process to be the zero vector field in 3D. As a covariance function we use a Gaussian kernel and choose to treat the x,y,z component of the vector field to be uncorrelated.

```tut:silent
import scalismo.statisticalmodel._
import scalismo.kernels._

val mean = VectorField(RealSpace[_3D], (_ : Point[_3D]) => Vector.zeros[_3D])
val kernel = UncorrelatedKernel[_3D](GaussianKernel(sigma = 40) * 50.0)
val gp = GaussianProcess(mean, kernel)
```

Before we can use the Gaussian process for modelling shapes, we need to perform a low-rank approximation to obtain a parametric, tractable definition.
Here, we approximate the first 200 dimensions (basis function). The approximation uses a finite sample of points on the mesh to approximate the basis function. Which points are used is determined by the sampler we provide. Here we choose to sample arbitrary points of the mesh. 
```tut:silent
import scalismo.numerics._

val sampler = RandomMeshSampler3D(
    referenceMesh, 
    numberOfPoints = 500, 
    seed = 42)

val lowRankGP = LowRankGaussianProcess.approximateGP(
    gp, 
    sampler, 
    numBasisFunctions = 200)
```

We can sample random function (i.e. vector fields) from the resulting 
```randomFun``` by calling

```tut:silent
val randomFun = lowRankGP.sample
```

```randomFun``` is a proper function, which we can evaluate on any point in 3D. For example, we can call

```tut:silent
val aRandomValue = randomFun(Point(7.0f, 15.0f, 1.0f))
``` 
to evaluate it at the point (7, 15, 1). Note that we represent all the coordinate values by Floats (and therefore we need the suffix f in the definition of points).

The resulting Gaussian process can now be used to define a shape model, by applying it to all the points of the reference mesh. 
```tut:silent
val shapeModel = StatisticalMeshModel(referenceMesh, lowRankGP)
```
To get a feeling of the shape variations that this model represent, it is best to visualize it in the graphical user interface by calling 

```tut:silent
ui.show(shapeModel, "faceModel")
```
*To visualize the shape variations, click on the instance object of the face model in the scene tree. You can sample random functions by clicking the random button, or explore the shape space more efficiently by changing the sliders. (You may want to hide or delete the reference mesh that you visualized in the previous step. To delete the object, simple select the object and delete it.)*

## Registration
We can now use this model to perform a registration of a target mesh. First, we load the target mesh and display it.
```tut:silent
val targetMesh = MeshIO.readMesh(new File("data/face-2.stl")).get
ui.show(targetMesh, "targetMesh")
```

*To visualize a registration, it is best to change the perspective in the graphical user interface to "orthogonal slices". You can find this functionality in the "tools" menu.*

The first step in performing a registration is to set up the Registration configuration:

```tut:silent
import scalismo.registration._

val evaluationSampler = RandomMeshSampler3D(
    referenceMesh,
    numberOfPoints = 1000,
    seed = 42
)

val regConfig = RegistrationConfiguration(
  optimizer = GradientDescentOptimizer(numIterations = 50, stepLength = 0.1),
  metric = MeanSquaresMetric(evaluationSampler),
  transformationSpace = GaussianProcessTransformationSpace(lowRankGP),
  regularizationWeight = 1e-8,
  regularizer = L2Regularizer
)
```
The evaluationSampler indicates where the metric will be evaluated to define the goodness of the model fit. In this case we choose a randomMeshSampler, which returns uniformely sampled points of the mesh. Combining this sampling strategy with a GradientDescentOptimizer corresponds to performing stochastic gradient descent optimization. As admissible transformations for the registration, we choose the transformations represented by our ```lowRankGP```.

Scalismo's registration method requires that its input are continuous, scalar-valued images. To perform mesh to mesh registration, we therefore need to change the representation of our meshes to an implicit representation using distance images. 
```tut:silent
import scalismo.mesh.Mesh
val fixedImage = Mesh.meshToDistanceImage(referenceMesh)
val movingImage = Mesh.meshToDistanceImage(targetMesh)
```

Note that these functions are lazy, and the distance values will only be computed for the points that will be evaluated. Hence, if we compute the metric values only on the 1000 points given by the evaluationSampler above, these values will be only evaluted for these 1000 points.

The actual registration is an iterative procedure. By calling the registration, we get an iterator which we can use to run the registration.
```tut:silent
val regIterator = Registration.iterations(regConfig)(fixedImage, movingImage)
```
Before running the registration, we change the iterator such that when we run it will print the current objective value, and updates the face model according to the current registration state. 
```tut:silent
val resultIterator = for ((it, itnum) <- regIterator.zipWithIndex) yield {
  println(s"object value in iteration $itnum is ${it.optimizerState.value}")
  ui.setCoefficientsOf("faceModel", it.registrationResult.parameters)
  it.registrationResult
}
```

The actual registration is executed, once we "consume" the last element of the iteration. This can, for example, be achieved like this:
```tut:silent
val registrationTransform = resultIterator.toSeq.last
```

You should see in the graphical user interface, how the face mesh slowly adapts to the shape of the target mesh. 

### Working with the registration result
The ```registrationTransform``` will result in a surface that approximates the target surface, by choosing the best representation in the model. 
Sometimes, we rather want to have an exact representation of the target mesh.This can be achieved by defining a projection function, which projects each point onto its closest point on the target. 
```tut:silent
val projection = (pt : Point[_3D]) => {
  targetMesh.findClosestPoint(pt).point
}
```

Composing the result of the registration with this projection, will give us a mapping that identifies for each point of the reference mesh the corresponding point of the target mesh. 
```tut:silent
val finalTransform = registrationTransform.andThen(projection)
```

To check this last point, we warp the reference mesh with the ```finalTransform``` and display it in the ui. You will observe that the target mesh coincides with the new result.
```tut:silent
val regResult = referenceMesh.transform(finalTransform)
ui.show(regResult, "projection")
```

```tut:invisible
ui.close()
```