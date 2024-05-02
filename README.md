# nmt-native-image-performance

### Summary
This test uses a simple custom Quarkus native app that has been rigged to allocate native memory. It measures time to first repsonse, RSS (measured upon start up), image size, and runs a hyperfoil benchmark to gather response latency data. Then it produces `report_date.txt` which summarizes the results. 

In order to use this test you must first build one quarkus app with NMT and name it `getting-started-1.0.0-SNAPSHOT-runner_nmt` using `./mvnw package -Dnative -DskipTests`. Then build it again without NMT and name it `getting-started-1.0.0-SNAPSHOT-runner_no_nmt`. Unfortunately, you need to build a custom Quarkus from source or build a custom GraalVM from source to achive this because Quarkus does not accept `-Dquarkus.native.monitoring=jfr` yet. 

### Configurations Tested

1. Native Image with NMT enabled
2. Native Image without NMT in the build
3. Java with NMT enabled
4. Java without NMT enabled


All configurations are tested in a single run.

The Quarkus app has two endpoints `regular` and `work` which are meant to be used for two different benchmarks. 

The `regular` endpoint is supposed to be more similar to what a quarkus app might do under normal circumstances. For example, this scenario is useful for obtaining some rough figures describing how NMT is impacting performance in general. 

The `work` endpoint generates an unrealistic number of native allocations. This should help highlight the impact any changes to the substrateVM NMT infrastructure have on performance. For example, this scenario is useful for testing with/without new development changes before deciding to merge them.

Hyperfoil templating is used to select between the endpoints depending on the benchmark being performed.

### Requirements

- Java 17+
- GraalVM
- ps
- python3.8
- hyperfoil
- linux

If you are interested in this project, you probably already have been using most of these tools in order build native image executables. The only requirement you may be missing is [Hyperfoil](https://hyperfoil.io/).

### Usage
Before running the test, export the required environment variables:
- **JAVA_HOME**:    Path to JDK
- **GRAALVM_HOME**:   Path to GraalVM
- **HYPERFOIL_HOME**:    Path to hyperfoil

You may be prompted to enter your password since `sudo` is needed to turn off turbo boost and clear caches. 

Usage: `python3.8 performance_test.py <endpoint>`

`python3.8 performance_test.py` will run the test using the "work" endpoint.

`python3.8 performance_test.py regular` will run the test using the "regular" endpoint.