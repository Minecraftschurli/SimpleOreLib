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
    compile fg.deobf("minecraftschurli:SimpleOreLib:<mcVersion>-<modVersion>")
}
```
