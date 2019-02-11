dependencies {
    compile(project(":karibu-dsl-v8"))
    compile("com.vaadin:vaadin-compatibility-server:${properties["vaadin8_version"]}")
}

val configureBintray = ext["configureBintray"] as (artifactId: String) -> Unit
configureBintray("karibu-dsl-v8compat7")
