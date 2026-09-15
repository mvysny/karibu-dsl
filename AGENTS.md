# Karibu-DSL — AGENTS.md

## What this is

This is a Kotlin extensions and DSL library for the [Vaadin](https://www.vaadin.com) framework.
Vaadin owns the components, the router and the server-client bridge; Karibu-DSL owns the *building
surface*: one type-safe builder function per Vaadin component, `KComposite` for composing your own,
and the extension functions Vaadin lacks. The component you get back is the real Vaadin component.

## Promises

- **The UI structure is visible in the code.** Nesting in the source is nesting in the component tree; a builder that reads bottom-up is not this library.
- **What you get back is the Vaadin component.** `button {}` returns a real `com.vaadin.flow.component.button.Button` — the DSL adds and configures, it never wraps or proxies, so nothing Vaadin can do becomes unreachable.

## Design docs

| File | Owns | Loaded |
|---|---|---|
| `README.md` | the pitch, the compatibility chart, the example apps | — |
| `karibu-dsl/README.md` | the user-facing tutorial: every DSL, by component | — |
| `AGENTS.md` (this) | promises, invariants, the module map, conventions, commands | every turn |
| `design/architecture.md` | how the pieces compose — the DSL surface, the module wiring, the test harness; normative | lazy |
| `design/decisions.md` | why this and not that — `D_` entries, FAQ-shaped | lazy |
| `CONTRIBUTING.md` | how to run and write tests, the release steps | — |
| doc comments | what one DSL function does and the snippet that uses it | at the symbol |

Every fact lives in exactly one of these; the others link to it.

## Invariants

- **A DSL function is `(@VaadinDsl HasComponents).x(…) = init(X(…), block)`.** One that constructs without `init()` hands back a detached component; the surface is in `design/architecture.md`.
- **A component's DSL lives in the package of the minimum Vaadin version that introduced it** — `v10` for the Vaadin 14 baseline, `v23` for Vaadin 23+. Moving one later breaks the import in every app. See `D_v23_split`.
- **Both library modules run under `explicitApi()`.** A public declaration without an explicit visibility modifier fails the build.
- **A test is an abstract class under `src/main/kotlin` of a testsuite module.** One written in a testrun module's `src/test` runs against a single Vaadin version only. See `D_abstract_testsuite`.

## Module map

- `karibu-dsl` — the core DSL, package `…karibudsl.v10`; published as `karibu-dsl`.
- `karibu-dsl/karibu-dsl-testsuite` — the abstract core tests, aggregated by `AllTests`.
- `karibu-dsl/karibu-dsl-testrun-vaadin14` — runs `AllTests` against the stable Vaadin.
- `karibu-dsl-v23` — DSLs for Vaadin 23+ components, package `…karibudsl.v23`; published as `karibu-dsl-v23`.
- `karibu-dsl-v23/tests` — the abstract v23 tests, aggregated by `AllTests24`.
- `karibu-dsl-v23/karibu-dsl-testrun-vaadin`, `…-vaadin-next` — run `AllTests` *and* `AllTests24` against stable and pre-release Vaadin.
- `example` — the Beverage Buddy demo on Vaadin Boot; not published.

## Conventions

- **Kotlin on JDK 21, default Kotlin formatter rules.** No linter config in the repo.
- **Dependency versions live in `gradle/libs.versions.toml`**, never inline in a `build.gradle.kts`; a Vaadin bump moves both `vaadin` and `vaadin_next`.
- **Tests: JUnit 5 with Karibu-Testing**, each fixture bracketed by `MockVaadin.setup()` / `tearDown()`; a new test class is wired into `AllTests` or `AllTests24` or it never runs.
- **New DSLs go into `v23`**, whichever Vaadin version introduced the component; existing package names stay as they are.
- **Release: bump `version` in the root `build.gradle.kts`**, tag, publish — the steps are in `CONTRIBUTING.md`. Never commit or push a release tag unless asked.

## Commands

- `./gradlew` — the default `clean build`; what CI runs on push and PR (`.github/workflows/gradle.yml`, on Linux/macOS/Windows × JDK 21/25).
- `./gradlew test` — the full suite, every Vaadin version.
- `./gradlew :karibu-dsl:karibu-dsl-testrun-vaadin14:test --tests '*GridTest*'` — one test class against one Vaadin.
- `./gradlew :example:run` — the Beverage Buddy demo on http://localhost:8080.
- `design/verify_design_tripwires.sh` — the doc-layer checks; wired into `check`, so `./gradlew` runs it.

## Skills this project follows

- **Doc comments carry the fact at its own level**, a KDoc snippet over prose; the `writing-kdoc` skill has the levels.
- **Component-oriented:** in `example`, self-sufficient components that reach the service directly, no MVC layers; the `cop` skill has the rules.

## Maintenance of this file

Loaded every turn; cap 34 KB, a module's own `AGENTS.md` 10 KB. Over it, in this order:
delete what has no home — status, history, class lists, what the code already says; trim
each line to its fact plus one clause and send the explanation home — why →
`design/decisions.md`, how across symbols → `design/architecture.md`, how in one symbol →
its doc comment, what upstream does → `design/research.md`; only then a module's own
`AGENTS.md`, peripheral modules first, never the core. Never paraphrase a lazy entry into a
line here. `design/verify_design_tripwires.sh` checks the caps and the cites.
