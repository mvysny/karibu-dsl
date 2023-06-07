pluginManagement {
    repositories {
        maven { setUrl("https://maven.vaadin.com/vaadin-prereleases") }
        gradlePluginPortal()
    }
}
include(
	"karibu-dsl",
	"karibu-dsl-v23",
	"example",
	"karibu-dsl:karibu-dsl-testsuite",
	"karibu-dsl:karibu-dsl-testrun-vaadin14",
	"karibu-dsl-v23:karibu-dsl-testrun-vaadin23"
)

