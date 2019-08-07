# Dependency Injection

### What

We are using [Guice](https://github.com/google/guice) to achieve Dependency Injection (DI), and hence
Inversion of Control (IoC). Guice is a lightweight framework that helps remove the need of newing up
dependencies from within an Object. This separates the responsibility of creating an object, from
using it.

### How

To use Guice you first must initialise a container, which holds all the bindings for your dependencies.
Within the container you can then bind your classes/interfaces to either:

- A direct implementation
    >bind(myInterface.class).to(myConcreteImplementation.class)

- A provider, which creates a binding depending on user(s) inputs
    >bind(myInterface.class).toProvider(myImplementationProvider.class)

You are then able to inject the bound dependency into the desired constructor using the **@Inject**
annotation.

```
private BoundInterface dependency;

@Inject
public Myclass(BoundInterface dependency) {
    this.dependency = dependency
}
```

### Key Decisions

One main change in the code is that now the command line arguments and execution with said arguments
has been separated. They are now connected by a common CommandLineBase class, which is also where the
IoC container is initialised.

Guice was selected as the DI framework due to its lightweight nature and ease of implementing it to the
project. Other frameworks were investigated and discarded.