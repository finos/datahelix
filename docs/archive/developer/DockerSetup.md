# Build and run the generator using Docker

The instructions below explain how to download the source code, and then build and run it using Docker.  This generates a self-contained executable Docker image which can then run the generator without needing to install a JRE.  If you would like to download and build the source code in order to contribute to development, we recommend you [build and run the generator using an IDE](../user/gettingStarted/BuildAndRun.md) instead.

## Get Code

Clone the repository to your local development folder.

```
git clone https://github.com/finos/datahelix.git
```

## Installation requirements

* Docker EE or CE

## Building the generator using Docker

A Data Helix generator docker image can be built by running the following command in the root source code directory:

```
docker build . --tag datahelix
```

If you are on Linux, or any other system with a `sh`-compatible shell available, the following command is equivalent:

```
./docker-build.sh
```

## Running the generator using Docker

Once built, you can run the image with:

```
docker run -ti -v mydir:/data datahelix [parameters]
```

Note that the `-v` option specifies how to map your local filesystem into the Docker image, so that the DataHelix generator can access the profile file that you pass to it, and can write its output to a location you can access.  For example, if you run the image inside the profile directory, on a system with Unix-style environment variables, you can run the following command:

```
docker run -ti -v $PWD:/data datahelix generate --profile-file=/data/examples/actor-names/profile.json
```

This will map your current working directory (using the `$PWD` environment variable) to the `/data` directory in the Docker image's virtual filesystem, and uses this mapping to tell the generator to use the file `./examples/actor-names/profile.json` as its profile input.  With this example, the generator output will be output to the console, but you can write the output data to a mapped directory in the same way.