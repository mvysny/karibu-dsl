# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Karibu-DSL is a Kotlin DSL / extension library for building [Vaadin](https://vaadin.com) Flow UIs. It layers type-safe builder functions (`button {}`, `verticalLayout {}`, `grid<T> {}`) on top of the Vaadin Java API. The library targets Vaadin 25+ on JDK 21. Released to Maven Central under `com.github.mvysny.karibudsl`.

Package conventions: DSLs are namespaced by the minimum Vaadin/Flow version that introduced their components — `v10` (Vaadin 14+ baseline), `v23` (Vaadin 23+). Older package names are kept as-is; new components added in newer Vaadin versions go into `v23`.

## Build / test

Common commands (all from repo root; `./gradlew` is the Gradle wrapper):

- `./gradlew` — default: `clean build`, the same thing CI runs.
- `./gradlew test` — run the full multi-module test suite.
- `./gradlew :karibu-dsl:karibu-dsl-testrun-vaadin14:test` — tests for the `v10` DSL against current Vaadin.
- `./gradlew :karibu-dsl-v23:karibu-dsl-testrun-vaadin:test` — tests for the `v23` DSL against the current Vaadin release.
- `./gradlew :karibu-dsl-v23:karibu-dsl-testrun-vaadin-next:test` — same suite against Vaadin pre-release (`vaadin_next` version in `gradle/libs.versions.toml`).
- `./gradlew :karibu-dsl:karibu-dsl-testrun-vaadin14:test --tests '*GridTest*'` — run a single test class; use JUnit's `--tests` pattern.
- `./gradlew :example:run` — launch the bundled Beverage Buddy demo app on http://localhost:8080 (Vaadin Boot, embedded Jetty, `main()` in `example/src/main/kotlin/.../Main.kt`).

Release flow is documented in `CONTRIBUTING.md` (bump `version` in root `build.gradle.kts`, tag, `./gradlew clean build publish closeAndReleaseStagingRepositories`). Never commit or push release tags unless explicitly asked.

## Module layout and why the split matters

The repo is a Gradle multi-project (see `settings.gradle.kts`). Two published artifacts, plus test-support modules and an example app:

- `karibu-dsl/` → artifact `karibu-dsl`. Pure Vaadin 14+ DSLs (package `com.github.mvysny.karibudsl.v10`). `compileOnly` dependency on `vaadin-core` so downstream apps control Vaadin's npm/webjar mode.
- `karibu-dsl-v23/` → artifact `karibu-dsl-v23`. Depends on `karibu-dsl` and adds DSLs for components introduced in Vaadin 23+ (package `com.github.mvysny.karibudsl.v23`: `MasterDetailLayout`, `SideNav`, `TabSheets`, `Card`, `Markdown`, v23 dialog/login additions, etc.).
- Both library modules enable `kotlin { explicitApi() }` — every public declaration must have an explicit visibility modifier. Don't omit `public`/`internal`/`private` on top-level or API-surface declarations, or the build will fail.

Test infrastructure is unusual and worth understanding before adding tests:

- `karibu-dsl/karibu-dsl-testsuite/` defines tests under `src/main/kotlin` (not `src/test`) as **abstract JUnit 5 classes** (e.g. `AllTests`, `GridTest`, `AbstractVaadinDslTests`). Tests are written once here.
- `karibu-dsl-v23/tests/` does the same thing for v23-only tests, bundled as `AllTests24`.
- The `*-testrun-*` modules (`karibu-dsl-testrun-vaadin14`, `karibu-dsl-v23/karibu-dsl-testrun-vaadin`, `karibu-dsl-v23/karibu-dsl-testrun-vaadin-next`) each contain a single `AllTest.kt` that subclasses the abstract suites via `@Nested inner class`, pinning a specific Vaadin dependency in their `build.gradle.kts`. This is how the same test suite runs against multiple Vaadin versions.
- Consequence: to add a new test, edit the abstract test in `karibu-dsl-testsuite` (or `karibu-dsl-v23/tests`) and — if introducing a brand-new test class — wire it into the corresponding `AllTests` / `AllTests24` aggregator. The testrun modules rarely need editing.

Tests use Karibu-Testing (`com.github.mvysny.kaributesting`) with `MockVaadin.setup()` / `tearDown()` for the Vaadin session. JUnit Platform is configured project-wide.

## Dependency coordinates

Versions live in `gradle/libs.versions.toml`. The two Vaadin versions (`vaadin` = stable, `vaadin_next` = pre-release) deliberately differ so the `testrun-vaadin-next` module can catch regressions against upcoming Vaadin releases. When bumping Vaadin, update both, plus `kaributesting` and `kaributools` as needed (their tags are linked in the toml).
