### Creating new module

* File / New Module / Android Library
* move projects to different folders
* add `kotlinOptions { jvmTarget = "1.8" }` to core/build.gradle
* add `include ":coreNetwork"` and `project(":coreNetwork").projectDir = file("../CoreNetwork")` to app/settings.gradle
* add `implementation project(':coreNetwork')` to app/build.gradle
