# depstar

a clj-based uberjarrer

# Usage

Install this tool to an alias in `$PROJECT/deps.edn` or `$HOME/.clojure/deps.edn`:

```clj
{
  :aliases {:depstar
              {:extra-deps
                 {com.healthfinch/depstar {:git/url "https://github.com/healthfinch/depstar.git"
                                           :sha "LATEST_SHA"}}}}
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

# License

The use and distribution terms for this software are covered by the
[Eclipse Public License 2.0](https://www.eclipse.org/org/documents/epl-2.0/EPL-2.0.html)
