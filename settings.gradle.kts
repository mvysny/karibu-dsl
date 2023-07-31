// workaround for https://github.com/gradle/gradle/issues/9830
pluginManagement {
    val vaadin_version: String by settings
    plugins {
        id("com.vaadin") version vaadin_version
    }
}

include(
	"karibu-dsl",
	"karibu-dsl-v23",
	"example",
	"karibu-dsl:karibu-dsl-testsuite",
	"karibu-dsl:karibu-dsl-testrun-vaadin14",
	"karibu-dsl-v23:tests",
	"karibu-dsl-v23:karibu-dsl-testrun-vaadin",
	"karibu-dsl-v23:karibu-dsl-testrun-vaadin-next"
)
