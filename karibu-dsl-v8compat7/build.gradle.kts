dependencies {
    api(project(":karibu-dsl-v8"))
    api("com.vaadin:vaadin-compatibility-server:${properties["vaadin8_version"]}")
}

kotlin {
    explicitApi()
}

val configureBintray = ext["configureBintray"] as (artifactId: String) -> Unit
configureBintray("karibu-dsl-v8compat7")
