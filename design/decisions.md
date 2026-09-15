# Decisions

Why this project is the way it is and not otherwise — FAQ-shaped: each entry is a question and
its current answer. Rewrite the answer when it changes; delete the entry when nobody asks any
more. An entry is earned by what it would cost to reverse — half the code base — or by research
the next person would otherwise redo. Not an entry: a Vaadin version bump, the testing library,
the CI host, `onClick` over `addClickListener`, which file a DSL lives in — a comment at the site
of the choice, or nothing; nothing about `design/` itself. Cite by slug, `D_<slug>`, never by
position; `grep '^## D_' design/decisions.md` is the index. The first entry is the ruler: every
later one trims to its length.

---

## D_extension_functions — Why extension functions on `HasComponents` rather than builder classes?

Every DSL is one extension function — `(@VaadinDsl HasComponents).button(text, icon, id, block):
Button = init(Button(text, icon), id, block)` — that constructs the Vaadin component, `add()`s it
to the receiver and returns it typed. The builder *is* the Vaadin component: no `ButtonBuilder`,
no wrapper, no parallel type hierarchy anywhere in the DSL. Why not a wrapper hierarchy: Vaadin
adds API every minor release, and a wrapper has to grow a delegating method per addition or the app is
locked out of it — the library would be a permanently second-class copy of Vaadin's surface, and
the promise **What you get back is the Vaadin component** would go with it. Why not a plain
factory function without `init()`: it hands back a detached component, so `getParent()` is null
inside the configuration block and every call site has to re-state the nesting it just wrote.
The costs we carry: a DSL can only be offered where `HasComponents` is the receiver, so a
component with a typed slot (`Dialog.header`, `Grid.Column`) needs its own receiver type; and
`@VaadinDsl` has to be repeated on both the receiver and the block type for `@DslMarker` to bite,
which is ugly at every declaration.

## D_compile_only_vaadin — Why is `vaadin-core` a `compileOnly` dependency rather than an `api` one?

Both library modules take Vaadin as `compileOnly` — `karibu-dsl` the stable `libs.vaadin.core`,
`karibu-dsl-v23` the pre-release `libs.vaadinnext.core` (`D_v23_split`) — so only the testrun
modules and `example` put a real Vaadin on a runtime classpath. The app therefore picks its
version *and* its packaging — npm mode with webjars excluded, the Gradle Vaadin plugin, a BOM — and
Karibu-DSL never drags a second one in behind it. Why not `api(vaadin-core)`: the build tool
would resolve our pin against theirs, so an app on 25.2 taking a Karibu-DSL built against 25.3
gets an upgrade it never asked for, and an app that excluded webjars gets them back through our
node. Why not a version range: ranges make builds irreproducible and resolve differently in
Gradle and Maven. The cost: nothing in the library is checked against the Vaadin the *consumer*
runs, so calling a Vaadin method an older Vaadin lacks is a `NoSuchMethodError` at their runtime
— which is what the README's compatibility chart and the three testrun modules exist to catch.

## D_v23_split — Why ship `karibu-dsl-v23` as a second artifact rather than folding it into `karibu-dsl`?

The two compile against different Vaadins: `karibu-dsl` against `libs.vaadin.core`, the stable
line, and `karibu-dsl-v23` against `libs.vaadinnext.core`, the pre-release. That is the split's
whole job — a DSL for a component that exists only in the newest Vaadin can be written and
released without the core artifact's compile floor moving, and an app that never calls
`masterDetailLayout`, `sideNav` or `card` never resolves those classes. It is named `v23` because
Vaadin 23 is where the first such component landed; the name is now historical and the rule that
matters is the package one, loaded as an invariant in `AGENTS.md`. Why not one artifact: its
floor would be the pre-release Vaadin, pinning every consumer to it. Why not one artifact per
Vaadin version — `-v24`, `-v25` and on: each is a handful of files, the package name already
marks the version, and the release matrix would multiply for nothing. The cost: a user has to
know which artifact a DSL comes from, which is why `karibu-dsl/README.md` marks it per section.

## D_abstract_testsuite — Why are the tests abstract classes under `src/main` rather than ordinary test classes?

The suite has to run unchanged against several Vaadin versions, and JUnit runs only what is on
the test classpath of the module it sits in. So a test is an abstract class under a testsuite
module's `src/main/kotlin` — `src/main` because a testrun module consumes it as an ordinary
`testImplementation` project dependency, `abstract` so discovery skips it where it is defined.
`AllTests` / `AllTests24` list every one as a `@Nested inner class`; each testrun module's
`AllTest` subclasses the aggregator and its `build.gradle.kts` pins the Vaadin. Each `AllTest`
opens with a test asserting the Vaadin version it expects, so a misresolved classpath fails loudly
instead of testing the same Vaadin twice. Why not a copy of the tests per version: they drift the first
time someone fixes one copy. Why not Gradle's `java-test-fixtures`: fixtures are for the helpers
a test uses, not for the tests themselves, and the `@Nested` aggregator is what produces one
readable tree per Vaadin version. The cost: a new test class runs nowhere until it is listed in
the aggregator, and the failure mode is a silent pass — which is why that rule is loaded.
