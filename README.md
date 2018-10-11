# depstar

<img src="./depstar_logo.png" />

a clj-based uberjarrer

# Usage

Install this tool to an alias in `$PROJECT/deps.edn` or `$HOME/.clojure/deps.edn`:

```clj
{
  :aliases {:depstar
              {:extra-deps
                 {com.healthfinch/depstar {:git/url "https://github.com/healthfinch/depstar.git"
                                           :sha "4aa7b35189693feebc7d7e4a180b8af0326c9164"}}}}
}
```

Create an uberjar by invoking `depstar` with the desired jar name:

```bash
clj -A:depstar -m hf.depstar.uberjar MyProject.jar
```

By default, the jar file does not include a manifest and cannot be run
directly. An optional, second argument can be provided to specify a main
namespace to be used as `Main-Class` in a minimal manifest. `-m` can be
used as a shorthand for `clojure.main`:

```bash
clj -A:depstar -m hf.depstar.uberjar MyProject.jar -m
java -jar MyProject.jar
```

This will run `clojure.main/-main` and start a REPL. All of the usual
`clojure.main` options will work here. For example:

```bash
java -jar MyProject.jar -m my.project
```

This will run `my.project/-main`. You could specify that as the main
namespace when building the jar file:

```bash
clj -A:depstar -m hf.depstar.uberjar MyProject.jar my.project
java -jar MyProject.jar
```

This will run `my.project/-main` as the jar file's main entry point.

`depstar` uses the classpath computed by `clj`.
For example, add web assets into an uberjar by including an alias in your `deps.edn`:

```clj
{:paths ["src"]
 :aliases {:webassets {:extra-paths ["public-html"]}}}
```

Then invoke `depstar` with the chosen aliases:

```bash
clj -A:depstar:webassets -m hf.depstar.uberjar MyProject.jar
```

# License

The use and distribution terms for this software are covered by the
[Eclipse Public License 2.0](https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
