# SimpleOreLib

### build.gradle
```gradle
repositories {
    maven {
        name = "GitHubPackages"
        url = "https://maven.pkg.github.com/Minecraftschurli/SimpleOreLib"
        credentials {
            username = gpr_user
            password = gpr_token
        }
    }
}
dependencies {
    compile fg.deobf("minecraftschurli:SimpleOreLib:<mcVersion>-<modVersion>")
}
```
