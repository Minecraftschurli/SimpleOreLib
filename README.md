# SimpleOreLib

### build.gradle
```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/Minecraftschurli/SimpleOreLib"
        credentials {
            username = <gpruser>
            password = <gprtoken>
        }
    }
}

dependencies {
    compile fg.deobf("minecraftschurli:SimpleOreLib:[CURRENT_MC_VERSION]-[CURRENT_SIMPLE_ORE_LIB_VERSION]")
}
```
