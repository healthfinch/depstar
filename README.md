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

Note that the resulting uberjar does not contain a manifest file so it cannot be run directly. `depstar` does no AOT compilation
either, so it's likely the only `-main` you'll have in the uberjar is `clojure.main/-main` -- but you can use that to run your own
code's `-main`. Create a file, `main.mf`, containing:

```
Main-Class: clojure.main
```

Now you can add this to the `MyProject.jar` you created above:

```bash
jar ufm MyProject.jar main.mf
```

If you now run the uberjar without command-line arguments, you'll get `clojure.main`'s REPL:

```bash
java -jar MyProject.jar
...
user=>
```

If your main namespace is `project.core`, you can run that as follows:

```bash
java -jar MyProject.jar -m project.core
```

# License

The use and distribution terms for this software are covered by the
[Eclipse Public License 2.0](https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
