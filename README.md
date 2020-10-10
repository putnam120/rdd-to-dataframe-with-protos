# rdd-to-dataframe-with-protos

To reproduce the `java.lang.NoSuchMethodError: 'scala.collection.generic.CanBuildFrom scala.collection.compat.package$.canBuildFromIterableViewMapLike():`

error

1) (This step may take a while.) Run `sbt pipeline/assembly`
2) Run `spark-submit pipeline/target/scala-2.12/pipeline.jar`