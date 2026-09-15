# Architecture

How the pieces compose — what no single symbol can say and what would be expensive to overturn:
the DSL surface, the module wiring, the test harness, and the flows a newcomer needs.
**Normative: the code conforms.** Change this file first, then the code. Not here: why
(`decisions.md` — cite the `D_`), one symbol's behaviour (its doc comment), the module map
(`AGENTS.md`), the user-facing tutorial (`karibu-dsl/README.md`). Cap 12 KB — over it,
doc-comment content has crept in.

---

## Wiring

- Dependencies point one way: `example` → `karibu-dsl-v23` → `karibu-dsl` → `karibu-tools` → Vaadin. Nothing under `karibu-dsl` names a `v23` symbol; the `v23` module only ever adds.
- Vaadin itself is `compileOnly` in both library modules (`D_compile_only_vaadin`). Only the three testrun modules and `example` put a real Vaadin on a runtime classpath, and each pins which one.
- The whole DSL surface is two `init()` overloads in `v10/VaadinDsl.kt`. Every builder file — `Layouts.kt`, `VaadinComponents.kt`, `Grid.kt`, the v23 files — is that one pattern applied to one Vaadin component, so a new DSL is a new function, never a new mechanism.
- `@VaadinDsl` is a `@DslMarker`, written on the receiver *and* the block type so a nested block cannot reach past its parent to a grandparent's builder. Leave it off and the function still compiles and runs — the scope control is what quietly goes missing.
- `KComposite` is the seam for a user's own component: `ui { }` runs a DSL block against a `DummyHasComponents` that accepts exactly one child, and keeps it as the composite's root. `getContent()` and `getElement()` are `final` — overriding them breaks Karibu-Testing.

## Flows

**Building a component tree** — `verticalLayout { button("Click") {} }`:

1. `HasComponents.verticalLayout(…)` in `Layouts.kt` constructs a `VerticalLayout`, applies its own arguments (`padding`, `spacing`, `classNames`) to it, then calls `init(layout, block)`.
2. `init()` calls `add(component)` on the receiver *first* — the child is attached before the block runs, so `getParent()` resolves inside it.
3. `component.block()` runs with the new component as receiver; the builders called inside it see it as their `HasComponents`, which is how source nesting becomes tree nesting.
4. `init()` returns the component itself, typed, so a call site can keep the reference: `val nameField = textField("Your name") {}`.

**One test run against one Vaadin version** (`D_abstract_testsuite`):

1. `./gradlew :karibu-dsl:karibu-dsl-testrun-vaadin14:test` — the module's `build.gradle.kts` puts `libs.vaadin.core` and the testsuite project on the test classpath.
2. JUnit discovers `AllTest` in that module's `src/test`; its `@Nested inner class All : AllTests()` pulls in every abstract test class from `karibu-dsl-testsuite`.
3. `AllTest` also carries a test asserting the Vaadin version it expects, so a misresolved classpath fails as a wrong-version error rather than as a wall of API failures.
4. Each fixture brackets itself with `MockVaadin.setup()` / `MockVaadin.tearDown()`; there is no servlet container and no browser.
5. The two v23 testrun modules do the same but subclass *both* aggregators — `AllTests` and `AllTests24` — so the core DSL is re-run against the pre-release Vaadin too. That pair is the regression net for upcoming releases.

## Where to start reading

`karibu-dsl/src/main/kotlin/com/github/mvysny/karibudsl/v10/VaadinDsl.kt` — about 110 lines
holding `@VaadinDsl`, both `init()` overloads, `DummyHasComponents` and `buildSingleComponent`.
Every other file in the library is that pattern applied to one Vaadin component; `KComposite.kt`
next, for how a user composes their own.
